package dev.sbm.twitter_sentiment_analysis.pojo;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
	private String user;

	private String created_at;

	private String location;

	private String text;

	private String[] hashtags;

	private int retweetCount;

	private String language;

	private String sentiment;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String[] getHashtags() {
		return hashtags;
	}

	public void setHashtags(String[] hashtags) {
		this.hashtags = hashtags;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
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

	public Tweet(String user, String created_at, String location, String text, String[] hashtags, int retweetCount,
			String language, String sentiment) {
		super();
		this.user = user;
		this.created_at = created_at;
		this.location = location;
		this.text = text;
		this.hashtags = hashtags;
		this.retweetCount = retweetCount;
		this.language = language;
		this.sentiment = sentiment;
	}

	public Tweet() {
	}

}
