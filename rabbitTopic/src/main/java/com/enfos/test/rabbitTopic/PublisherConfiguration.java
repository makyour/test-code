package com.enfos.test.rabbitTopic;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author markyour
 *
 */
@Profile("publisher")
@Configuration
@EnableScheduling
public class PublisherConfiguration {
	
	@Value("${publisher.exchange}")
	private String exchangeName;// = "enfos.test.topic";

  /**
 * @return
 */
@Bean
  public TopicExchange senderTopicExchange() {
    return new TopicExchange(exchangeName);
  }


  /**
 * @param rabbitTemplate
 * @param senderTopicExchange
 * @return
 */
@Bean
  public Publisher EnfosTestPublisher(RabbitTemplate rabbitTemplate, TopicExchange senderTopicExchange) {
    return new Publisher(rabbitTemplate, senderTopicExchange);
  }

}

