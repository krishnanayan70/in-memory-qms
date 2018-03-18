package com.phonepe.assignment.data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class Consumer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8291901737610859870L;
	@Id
    public String id;
	private String name;
	private String topic;
	private String callbackUrl;
	private Set<String> dependencies;
	private Date createdOn;
	private Date updatedOn;
	
	public Consumer() {
		this.dependencies = new HashSet<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public Set<String> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Set<String> deps) {
		this.dependencies.addAll(deps);
	}
	
	public void addDependencies(Set<String> dependencies) {
		this.dependencies.addAll(dependencies);
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Override
	public String toString() {
		return "Consumer [name=" + name + ", topic=" + topic + ", callbackUrl=" + callbackUrl + ", dependencies="
				+ dependencies + "]";
	}

}
