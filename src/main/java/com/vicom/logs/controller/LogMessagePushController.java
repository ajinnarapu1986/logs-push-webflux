package com.vicom.logs.controller;

import java.util.List;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vicom.logs.model.LogMessage;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/logs-publish")
@Slf4j
public class LogMessagePushController {

	@Autowired
	private KafkaTemplate<String, LogMessage> kafkaTemplate;

	/**
	 * 
	 * @param logMessages
	 * @return Message
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<String> create(@RequestBody List<LogMessage> logMessages) {

		return Mono.create(response -> {

			logMessages.forEach(logMessage -> {
				ListenableFuture<SendResult<String, LogMessage>> future = kafkaTemplate
						// .send(AppConstants.TOPIC_RAW_DATA_NAME, logMessage);
						.send("logger", logMessage);

				future.addCallback(new ListenableFutureCallback<SendResult<String, LogMessage>>() {

					@Override
					public void onSuccess(SendResult<String, LogMessage> result) {
						RecordMetadata rmd = result.getRecordMetadata();
						log.info("CONSUMER :: TOPIC NAME: " + rmd.topic() + "\t PARTITION: " + rmd.partition());
						response.success(result.getProducerRecord().value().toString());
					}

					@Override
					public void onFailure(Throwable ex) {
						log.error("Error to publish in topic: " + ex.getMessage());
						response.error(ex);
					}
				});
			});
		});
	}

}
