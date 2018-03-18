package com.phonepe.assignment.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.assignment.data.Consumer;
import com.phonepe.assignment.data.Digraph;
import com.phonepe.assignment.data.Message;
import com.phonepe.assignment.db.ConsumerRepository;

@Service
public class QueueService {
	
	private static final Log log = LogFactory.getLog(QueueService.class);
	private static final int DEFAULT_TIMEOUT = 500;
	private static final String QUEUE_NAME = "messages";
	private static Long messageCount = new Long(0);
	private static Integer size = new Integer(10);
	
	@Resource(name="redisTemplate")
	private ListOperations<String, Message> listOps;
	@Autowired
	private ConsumerRepository consumerRepo;
	@Autowired
	private RetryTemplate retryTemplate;
	private RestTemplate restTemplate;
	private ExecutorService executorService;

	public QueueService() {
		this.restTemplate = new RestTemplate();
		this.executorService = Executors.newFixedThreadPool(2);
		this.processMessage();
	}
	
	public String addMessage(Message message) {
		synchronized (messageCount) {
			synchronized (size) {
				if(messageCount.equals(size))
					return "Queue is full.";
				messageCount = listOps.rightPush(QUEUE_NAME, message);
				return "Message added to Queue.";
			}
		}
	}
	
	private void processMessage() {
		for(int i=0;i<2;i++)
			executorService.submit(new Runnable() {
				
				@Override
				public void run() {
					while(true) {
						Message m = listOps.leftPop(QUEUE_NAME, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
						if(m != null) {
							List<Consumer> consumers = getSequenceForConsumption(consumerRepo.getConsumersForTopic(m.getTopic()));
							for(int i=consumers.size()-1;i>=0;i--) {
								invokeCallbackUrls(consumers.get(i), m);
							}
							synchronized (messageCount) {
								if(messageCount > 0)
									messageCount--;
							}
						}
					}
				}
			});
	}
	
	public ConsumerRepository getConsumerRepository() {
		return this.consumerRepo;
	}
	
	private List<Consumer> getSequenceForConsumption(Map<String, Consumer> consumers){
		Digraph<Consumer> digraph = new Digraph<>();
		for(Consumer consumer: consumers.values()) {
			digraph.add(consumer);
			for(String edge : consumer.getDependencies()) {
				digraph.add(consumer, consumers.get(edge));
			}
		}
		return digraph.topSort();
	}
	
	private void invokeCallbackUrls(Consumer consumer, Message m) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			try {
				retryTemplate.execute(new CallbackWithRetry(restTemplate, consumer.getCallbackUrl(), mapper.writeValueAsString(m)));
			}catch(RuntimeException e) {
				log.error("Runtime Exception while invoking callback. Exception: "+e.getMessage());
			}
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("Message: "+m+" consumed by: "+consumer);	
	}
	
	public void setRetry(int retry) {
		retryTemplate.setRetryPolicy(new SimpleRetryPolicy(retry));
	}
	
	public void setSize(int newSize) {
		synchronized (size) {
			size = newSize;
		}
	}

}

class CallbackWithRetry implements RetryCallback<Void, RuntimeException>{
	
	private static Log log = LogFactory.getLog(CallbackWithRetry.class);
	private RestTemplate restTemplate;
	private String url;
	private String message;
	
	public CallbackWithRetry(RestTemplate restTemplate, String url, String message) {
		this.restTemplate = restTemplate;
		this.url = url;
		this.message = message;
	}

	@Override
	public Void doWithRetry(RetryContext context) throws RuntimeException {
		log.info("Sending message:" + this.message + " to callback url - " + this.url);
		this.restTemplate.postForObject(this.url, this.message, String.class);
		return null;
	}
	
}
