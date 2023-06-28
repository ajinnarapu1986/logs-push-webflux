package com.vicom.logs.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.vicom.logs.model.LogMessage;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaLogMessageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLogMessageService.class);

	@Autowired
	private KafkaTemplate<String, LogMessage> kafkaTemplate;

	public List<String> sendMessage(List<LogMessage> logMessages) {
		LOGGER.info(String.format("Message sent -> %s", logMessages.size()));

		List<String> response = new ArrayList<>();
		
		logMessages.forEach(logMessage -> {
			//LOGGER.info("I am a Kafka Producer :: {}", logMessage);

			// kafkaTemplate.send(AppConstants.TOPIC_RAW_DATA_NAME, logMessage);

			ListenableFuture<SendResult<String, LogMessage>> future = kafkaTemplate.send("logger", logMessage);

			future.addCallback(new ListenableFutureCallback<SendResult<String, LogMessage>>() {

				@Override
				public void onSuccess(SendResult<String, LogMessage> result) {
					log.info("-------------------CONSUMER");
					log.info("TOPIC NAME: " + result.getRecordMetadata().topic());
					log.info("PARTITION: " + result.getRecordMetadata().partition());
					response.add(result.getProducerRecord().value().toString());
				}

				@Override
				public void onFailure(Throwable ex) {
					log.error("Error to publish in topic: " + ex.getMessage());
					response.add(ex.getLocalizedMessage());
				}
			});
		});
		return response;
	}
}
