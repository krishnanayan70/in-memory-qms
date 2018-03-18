package com.phonepe.assignment.db;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.phonepe.assignment.data.Consumer;

@Repository
public class ConsumerRepository {
	
	private static final Log log = LogFactory.getLog(ConsumerRepository.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public void addConsumer(Consumer consumer) {
		log.info("Saving consumer in database : "+consumer);
		consumer.setCreatedOn(new Date());
		consumer.setUpdatedOn(new Date());
		mongoTemplate.insert(consumer, "consumers");
	}
	
	public String addDependencies(String consumerName, Set<String> newdependencies) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(consumerName));
		Consumer consumer = mongoTemplate.findOne(query, Consumer.class, "consumers");
		if(consumer == null) {
			return "Consumer Not defined. Unable to add dependencies.";
		}
		consumer.addDependencies(newdependencies);
		consumer.setUpdatedOn(new Date());
		mongoTemplate.save(consumer,"consumers");
		return "Added dependencies successfully";
	}
	
	public Map<String, Consumer> getConsumersForTopic(String topic){
		Map<String, Consumer> map = new HashMap<>();
		Query query = new Query();
		query.addCriteria(Criteria.where("topic").is(topic));
		List<Consumer> consumerWithSameTopic = mongoTemplate.find(query, Consumer.class, "consumers");
		for(Consumer consumer : consumerWithSameTopic) {
			map.put(consumer.getName(), consumer);
		}
		return map;
	}

}
