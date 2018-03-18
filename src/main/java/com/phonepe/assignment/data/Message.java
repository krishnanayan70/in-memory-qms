package com.phonepe.assignment.data;

import java.io.Serializable;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -152637027040711712L;
	private String topic;
	private String content;

	public Message() {
	}
	
	public Message(String message) {
		String[] m = message.split(",");
		if(m.length == 2) {
			this.topic = m[0].split("=")[1];
			this.content = m[1].split("=")[1];
		}
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "topic=" + topic + ",content=" + content;
	}

}
