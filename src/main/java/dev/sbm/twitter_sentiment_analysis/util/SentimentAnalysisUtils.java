package dev.sbm.twitter_sentiment_analysis.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalysisUtils {

	private static Properties prop;

	{
		prop = new Properties();
		prop.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
	}

	public static String detectSentiment(String messge) {

		StanfordCoreNLP stanfordCoreNLP = new StanfordCoreNLP(prop);
		Annotation annotations = stanfordCoreNLP.process(messge);
		List<CoreMap> list = annotations.get(CoreAnnotations.SentencesAnnotation.class);

		List<Double> sentiments = new ArrayList<Double>();
		List<Double> sizes = new ArrayList<Double>();

		int longest = 0;
		int mainSentiment = 0;

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			CoreMap tweetMsg = (CoreMap) iterator.next();
			Tree parseTree = tweetMsg.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
			int tweetSentiment = RNNCoreAnnotations.getPredictedClass(parseTree);
			String partText = tweetMsg.toString();
			if (partText.length() > longest) {
				mainSentiment = tweetSentiment;
				longest = partText.length();
			}

			sentiments.add(Double.valueOf(tweetSentiment));
			sizes.add(Double.valueOf(partText.length()));

		}

		List<Double> weightedSentiments = new ArrayList<Double>();

		for (int i = 0; i < sentiments.size(); i++) {
			weightedSentiments.add(sentiments.get(i) * sizes.get(i));
		}
		double weightedSentimentSum = weightedSentiments.stream().mapToDouble(Double::doubleValue).sum();
		double sizesSum = sizes.stream().mapToDouble(Double::doubleValue).sum();

		double weightedSentiment = weightedSentimentSum / sizesSum;

		if (weightedSentiment <= 0.0)
			return "NOT_UNDERSTOOD";
		else if (weightedSentiment < 1.6)
			return "NEGATIVE";
		else if (weightedSentiment <= 2.0)
			return "NEUTRAL";
		else if (weightedSentiment < 5.0)
			return "POSITIVE";
		else
			return "NOT_UNDERSTOOD";

	}
}
