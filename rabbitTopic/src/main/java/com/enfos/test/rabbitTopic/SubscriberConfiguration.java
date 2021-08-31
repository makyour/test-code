/**
 * 
 */
package com.enfos.test.rabbitTopic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * @author markyour
 *
 */
@Profile("subscriber")
@Configuration
public class SubscriberConfiguration implements ApplicationListener<ApplicationReadyEvent> {

  private Logger logger = LoggerFactory.getLogger(SubscriberConfiguration.class);
  
  @Value("${subscriber.exchange}")
  private String exchangeName;// = "enfos.test.topic";

  @Value("${subscriber.queue}")
  private String queueName;

  @Value("${subscriber.routingKey}")
  private String routingKey;

 /* @Bean
  public ConnectionFactory connectionFactory() {
      PooledConnectionFactory connectionFactory = new PooledConnectionFactory();
      connectionFactory.setAddresses(address);
      connectionFactory.setUsername(username);
      connectionFactory.setPassword(password);
      return connectionFactory;*/
      
  /**
 * @return
 */
@Bean
  public TopicExchange receiverExchange() {
    return new TopicExchange(exchangeName);
  }

  /**
 * @return
 */
@Bean
  public Queue eventReceivingQueue() {
    if (queueName == null) {
      throw new IllegalStateException("Please specify the name of the queue to listen to with the property 'subscriber.queue'");
    }
    //Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String,Object> arguments)
    //Construct a new queue, given a name, durability flag, and auto-delete flag, and arguments.
    return new Queue(queueName,false,false,true);
  }

  /**
 * @param eventReceivingQueue
 * @param receiverExchange
 * @return
 */
@Bean
  public Binding binding(Queue eventReceivingQueue, TopicExchange receiverExchange) {
    if (routingKey == null) {
      throw new IllegalStateException("Please specify the routing key for the events to listen to with the property 'subscriber.routingKey' (see EnfosTestPublisher for available routing keys).");
    }
    return BindingBuilder
            .bind(eventReceivingQueue)
            .to(receiverExchange)
            .with(routingKey);
  }

  /**
 * @param connectionFactory
 * @param listenerAdapter
 * @return
 */
@Bean
  public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                  MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  /**
 * @param Subscriber
 * @return
 */
@Bean
  public MessageListenerAdapter listenerAdapter(Subscriber Subscriber) {
    return new MessageListenerAdapter(Subscriber, "receive");
  }

  /**
 * @return
 */
@Bean
  public Subscriber eventReceiver() {
    return new Subscriber();
  }

  /**
 *
 */
@Override
  public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    logger.info("SUBSCRIBING TO EVENTS MATCHING KEY '{}' FROM QUEUE '{}'!", routingKey, queueName);
  }
}

