package dev.sbm.twitter_sentiment_analysis;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;

import com.google.gson.Gson;
import com.twitter.twittertext.Extractor;
import com.twitter.twittertext.TwitterTextParseResults;
import com.twitter.twittertext.TwitterTextParser;

import dev.sbm.twitter_sentiment_analysis.pojo.Tweet;
import dev.sbm.twitter_sentiment_analysis.util.SentimentAnalysisUtils;
import twitter4j.Status;

/**
 * Hello world!
 *
 */
public class TwitterSentimentAnalysis {
	public static void main(String[] args) throws InterruptedException {
		if (args.length < 4) {
			System.err.println("Usage: TwitterSentimentAnalysis <consumer key> <consumer secret> "
					+ "<access token> <access token secret> [<filters>]");
			System.exit(1);
		}
		String[] filters = Arrays.copyOfRange(args, 4, args.length);

		String consumerKey = args[0];
		String consumerSecret = args[1];
		String accessToken = args[2];
		String accessTokenSecret = args[3];

		System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
		System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
		System.setProperty("twitter4j.oauth.accessToken", accessToken);
		System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);

		SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName("TwitterSentimentAnalysis");
//		sparkConf.set("es.nodes.discovery","true");
		sparkConf.set("es.index.auto.create", "true");
		sparkConf.set("es.nodes", "127.0.0.1");
		sparkConf.set("es.port","9200");
		JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, new Duration(5000));

		JavaReceiverInputDStream<Status> tweetStream = TwitterUtils.createStream(javaStreamingContext, filters);

		
		tweetStream.print();
		
		
		tweetStream.foreachRDD((rdd,time)->{
			JavaRDD<String> jsonData = rdd.filter(tt->tt.getLang().equalsIgnoreCase("en")).map(t->{
				Tweet tweet = new Tweet();
				tweet.setUser(t.getUser().getName());
				tweet.setCreated_at(t.getCreatedAt().toInstant().toString());
				tweet.setLocation(t.getUser().getLocation());
				tweet.setText(t.getText());
				tweet.setHashtags( Arrays.stream(t.getHashtagEntities()).map(tags->tags.getText()).toArray(String[]::new));
				tweet.setRetweetCount(t.getRetweetCount());
				tweet.setLanguage(t.getLang());
				String tweetText = t.getText();
				TwitterTextParseResults parseTweet = TwitterTextParser.parseTweet(tweetText);
				String extractedText = tweetText.substring(parseTweet.validTextRange.start, parseTweet.validTextRange.end);
				
				tweet.setSentiment(SentimentAnalysisUtils.detectSentiment(extractedText));
				return new Gson().toJson(tweet);
			});
			JavaEsSpark.saveJsonToEs(jsonData, "twitter/tweet");
			});

		
//		tweetStream.filter(t->t.getLang().equalsIgnoreCase("en") && !t.getText().startsWith("RT")).map(t->{
//			HashtagEntity[] hashtagEntities = t.getHashtagEntities();
//			List<String> hashTags = Arrays.stream(hashtagEntities).map(tags->tags.getText()).collect(Collectors.toList());
//			return new TweetPojo(t.getText(), hashTags, t.getRetweetCount(), t.getLang(), SentimentAnalysisUtils.detectSentiment(t.getText())).toString();
//		}).foreachRDD(tt->tt.saveAsTextFile("/home/sbm/eclipse-workspace/twitter-sentiment-analysis/jan07"));
//		
		
		javaStreamingContext.start();
		javaStreamingContext.awaitTermination();
	}
}
