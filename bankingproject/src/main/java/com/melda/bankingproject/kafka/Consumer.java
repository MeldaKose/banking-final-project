package com.melda.bankingproject.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	private static Logger log = LogManager.getLogger(Consumer.class.getName()); 
	
	@KafkaListener(topics = "logs", groupId = "logs_group")
	public void listenTransfer(@Payload String message, 
			  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
	) {
		log.info(message);
	}
}
