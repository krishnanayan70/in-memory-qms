package com.phonepe.assignment.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phonepe.assignment.data.Consumer;
import com.phonepe.assignment.data.Message;
import com.phonepe.assignment.service.QueueService;

@Controller
public class QueueSystemController {
	
	@Autowired
	private QueueService queueService;
	
	@PostMapping("/consumer/subscribe")
	public @ResponseBody String subscribe(@RequestBody Consumer consumer) {
		queueService.getConsumerRepository().addConsumer(consumer);
		return "Subscription successful for consumer " + consumer;
	}
	
	@PostMapping("/consumer/{consumer}/add_dependency")
	public @ResponseBody String addDependency(@PathVariable("consumer") String consumer, @RequestBody Set<String> dependencies) {
		return queueService.getConsumerRepository().addDependencies(consumer, dependencies);
	}
	
	@PostMapping("/config/{name}/{value}")
	public @ResponseBody String configure(@PathVariable("name") String name, @PathVariable("value") String value) {
		long newValue = Long.parseLong(value);
		if("size".equals(name)) {
			queueService.setSize(newValue);
		}else if("retry".equals(name)) {
			queueService.setRetry((int)newValue);
		}else {
			return "Invalid configuration provided.";
		}
		return "Successfully updated config";
	}
	
	@PostMapping("/publish")
	public @ResponseBody String publish(@RequestBody Message message) {
		return queueService.addMessage(message);
	}
	
//	@PostMapping("/consumes") //To check callback url invocation and retry mechanism
//	public @ResponseBody String consume(@RequestBody String message) {
//		System.out.println("Message Recieved : "+message);
//		return "Successfully consumed.";
//	}

}
