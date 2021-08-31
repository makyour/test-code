/**
 * 
 */
package com.enfos.test.rabbitTopic;

/**
 * @author markyour
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscriber {

  private Logger logger = LoggerFactory.getLogger(Subscriber.class);

  /**
   *  Message Handler Callback
   *  
 * @param message
 */
public void receive(String message) {
    logger.info("Received message '{}'", message);
    System.out.println(" [x] Received '" + message + "'");
  }


}
