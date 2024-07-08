package com.topkey.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;
import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    //private CountDownLatch latch = new CountDownLatch(1);
    private static final Logger log = LoggerFactory.getLogger(Receiver.class);
//    public void receiveMessage(byte[] message) {
//
//        String receivedMessage = new String(message);
//        System.out.println("Received <" + receivedMessage + ">");
//        log.info("Received <" + receivedMessage + ">");
//    }
//    public void receiveMessage(String message) {
//        System.out.println("Received <" + message + ">");
//        log.info("Received <" + message + ">");
//       // latch.countDown();
//    }

//    public CountDownLatch getLatch() {
//        return latch;
//    }

//    @RabbitListener(queues = "spring-boot")
//    public void handleLogMessage(Message<String> message) {
//    	
//        log.info("Message received: {} - {} - {} - {}",
//                message.getHeaders().get("amqp_appId"),
//                
//                message.getHeaders().get("amqp_level"),
//                message.getHeaders().get("amqp_timestamp"),
//                message.getPayload());
//    }
//
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @org.springframework.amqp.rabbit.annotation.Queue(value = "spring-boot-queue1", durable = "false"),
//            exchange = @Exchange(value = "spring-boot-exchange", type = "topic"),
//            key = "foo.baa.#"))
//    public void handleQueue1Message(Message<String> message) {
//        log.info("Queue1 Message received: {} - {} - {} - {}",
//                message.getHeaders().get("amqp_appId"),
//                message.getHeaders().get("amqp_level"),
//                message.getHeaders().get("amqp_timestamp"),
//                message.getPayload());
//    }

}