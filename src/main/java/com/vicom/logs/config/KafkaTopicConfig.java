package com.vicom.logs.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.vicom.logs.AppConstants;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic rawDataTopic() {
		return TopicBuilder.name(AppConstants.TOPIC_RAW_DATA_NAME)
				// .partitions(6).replicas(1)
				// .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}

	@Bean
	public NewTopic processedDataTopic() {
		return TopicBuilder.name(AppConstants.TOPIC_PROCESSED_DATA_NAME)
				// .partitions(6).replicas(1)
				// .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	@Bean
	public NewTopic loggerTopic() {
		return TopicBuilder.name("logger")
				// .partitions(6).replicas(1)
				// .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
}