package com.enfos.test.rabbitTopic;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author markyour
 *
 */
public class Publisher {

  private Logger logger = LoggerFactory.getLogger(Publisher.class);

  private final RabbitTemplate rabbitTemplate;

  private final TopicExchange topicExchange;

  private int messageNumber = 0;

  private static List<String> ROUTING_KEYS = Arrays.asList(
          "custX.finance.report",
          "custX.finance.invoice",
          "custX.aoc.survey",
          "custX.aoc.sample",
          "custZ.finance.report",
          "custZ.finance.invoice",
          "custZ.aoc.survey",
          "custZ.aoc.sample",
          "custZ.user.deleted");

  private Random random = new Random();

  /**
 * @param rabbitTemplate
 * @param topicExchange
 */
@Autowired
  public Publisher(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
    this.rabbitTemplate = rabbitTemplate;
    this.topicExchange = topicExchange;
  }

/**
 * 
 */
@Scheduled(fixedDelay = 1000, initialDelay = 500)
  public void sendMessage() {
    String routingKey = randomRoutingKey();
    String message = String.format("Event no. %d of type '%s'", ++messageNumber, routingKey);
    rabbitTemplate.convertAndSend(topicExchange.getName(), routingKey, message);
    logger.info("Published message '{}'", message);
  }

  /**
 * @return
 */
private String randomRoutingKey() {
    return ROUTING_KEYS.get(random.nextInt(ROUTING_KEYS.size()));
  }

}
