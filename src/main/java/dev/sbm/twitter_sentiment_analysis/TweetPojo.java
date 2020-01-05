package dev.sbm.twitter_sentiment_analysis;

import java.util.List;

public class TweetPojo {
	
	private String text;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	private List<String>  hashtags;
	private int retweetCount;
	private String language;
	private String sentiment;
	public List<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}
	public int getRetweet() {
		return retweetCount;
	}
	public void setRetweet(int retweet) {
		this.retweetCount = retweet;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSentiment() {
		return sentiment;
	}
	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
	public TweetPojo(String text, List<String> hashtags, int retweet, String language, String sentiment) {
		super();
		this.text = text;
		this.hashtags = hashtags;
		this.retweetCount = retweet;
		this.language = language;
		this.sentiment = sentiment;
	}
	@Override
	public String toString() {
		return "TweetPojo [text=" + text + ", hashtags=" + hashtags.toString() + ", retweetCount=" + retweetCount + ", language="
				+ language + ", sentiment=" + sentiment + "]";
	}
	
}
