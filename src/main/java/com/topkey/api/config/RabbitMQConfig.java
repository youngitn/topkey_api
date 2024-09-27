package com.topkey.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.topkey.api.converter.GsonMessageConverter;

import brave.Tracing;
import brave.spring.rabbit.SpringRabbitTracing;
import io.micrometer.observation.ObservationRegistry;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	public static final String topicExchangeName = "topic-exchange";
	public static final String directExchangeName = "direct-exchange";
	
	public static final String directQueueName = "direct-queue";
	public static final String travelExpenseQueue = "travel-expense-queue";
	public static final String dlxExchangeName = "dlx-exchange";// 死信交換機
	public static final String dlxQueue = "dlx-queue";// 死信queue
	
	public static final String dlxRoutingKey = "dlq.routing.Key";
	public static final String directRoutingKey = "direct.routing.key";
	public static final String travelExpenseRoutingKey = "travel.expense.routing.Key";
	public static final String callbackRoutingKey = "callback.routing.Key";

	@Bean(name = "travelExpenseQueue")
	Queue travelExpenseQueue() {
		/**定義queue屬性**/
		return QueueBuilder.
				durable(travelExpenseQueue)
				.deadLetterExchange(dlxExchangeName)
				.deadLetterRoutingKey(dlxRoutingKey).ttl(10000).build();
		/**這裡提供另一種透過arg定義queue屬性**/
//		Map<String, Object> args = new HashMap<>();
//		args.put("x-dead-letter-exchange", dlxExchangeName); // 指定死信交换机
//		args.put("x-dead-letter-routing-key", dlxRoutingKey); // 指定死信路由键
//		return new Queue(travelExpenseQueue, false, false, false, args);
	}

	@Bean(name = "directQueue")
	Queue directQueue() {
		return new Queue(directQueueName, false);
	}

    @Bean(name = "callbackQueue")
    Queue callbackQueue() {
		return new Queue("callbackQueue", true);
	}

    @Bean(name = "dlxQueue")
    Queue dlxQueue() {

		return new Queue(dlxQueue, false);
	}

	@Bean(name = "topicExchange")
	TopicExchange topicExchange() {
		return new TopicExchange(topicExchangeName);
	}

	@Bean(name = "directExchange")
	DirectExchange directExchange() {
		return new DirectExchange(directExchangeName);
	}

    @Bean
    DirectExchange dlxExchange() {
		return new DirectExchange(dlxExchangeName);
	}

    @Bean
    GsonMessageConverter gsonMessageConverter() {
		return new GsonMessageConverter();
	}

    /**
     * SpringRabbitTracing 是 Brave 用於與 Spring Rabbit 整合的核心類。
     * 你需要將它註冊到 Spring 上下文中，讓它可以自動處理消息的追蹤。
     * @param tracing
     * @return
     */
    @Bean
    SpringRabbitTracing springRabbitTracing(Tracing tracing) {
	    return SpringRabbitTracing
	            .newBuilder(tracing)
	            .build();
	}

    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
                               MessageConverter gsonMessageConverter, SpringRabbitTracing tracing) {
		
		//使用 SpringRabbitTracing 來生成帶有追蹤功能的 RabbitTemplate
		final RabbitTemplate rabbitTemplate = tracing.newRabbitTemplate(connectionFactory);
		//RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(gsonMessageConverter);

		return rabbitTemplate;
	}
	
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, 
//                                         MessageConverter messageConverter,
//                                         ObservationRegistry observationRegistry) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter);
//        // 自動觀察 RabbitMQ 發送過程
//        rabbitTemplate.setObservationRegistry(observationRegistry);
//        return rabbitTemplate;
//    }
	
	//消費者才需要
//	@Bean
//	public ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer() {
//	    // This config is for listener to get tracing id from header
//	    return container -> container.setObservationEnabled(true);
//	}

	/**
	 * 將que和 exchange綁定, 1. 送出message到exchange 2. 之後exchange根據key會路由到不同que
	 */

	// 綁定交換機
	@Bean
	Binding travelExpenseBinding(@Qualifier("travelExpenseQueue") Queue travelExpenseQueue,
			@Qualifier("topicExchange") TopicExchange topicExchange) {
		return BindingBuilder.bind(travelExpenseQueue).to(topicExchange).with(travelExpenseRoutingKey);
	}

	@Bean
	Binding directBinding(@Qualifier("directQueue") Queue directQueue,
			@Qualifier("directExchange") DirectExchange directExchange) {
		return BindingBuilder.bind(directQueue).to(directExchange).with(directRoutingKey);
	}

    @Bean
    Binding dlxBinding(Queue dlxQueue, DirectExchange dlxExchange) {
		return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(dlxRoutingKey);
	}

    @Bean
    Binding callbackBinding(@Qualifier("callbackQueue") Queue callbackQueue,
                         @Qualifier("topicExchange") TopicExchange topicExchange) {
		return BindingBuilder.bind(callbackQueue).to(topicExchange).with(callbackRoutingKey);
	}
	


}

//    static final String topicExchangeName = "spring-boot-exchange";
//
//    static final String queueName = "spring-boot";
//    
//    static final String directExchangeName = "direct-exchange";
//    
//    static final String directQueueName = "direct-queue";
//
//    @Bean(name = "topicQueue")
//    Queue topicQueue() {
//        return new Queue(queueName, false);
//    }
//
//    @Bean(name = "directQueue")
//    Queue directQueue() {
//        return new Queue(directQueueName, false);
//    }
//
//    @Bean(name = "topicExchange")
//    TopicExchange topicExchange() {
//        return new TopicExchange(topicExchangeName);
//    }
//
//    @Bean(name = "directExchange")
//    DirectExchange directExchange() {
//        return new DirectExchange(directExchangeName);
//    }
//    /**
//     * 將que和 exchange綁定,
//     * 1. 送出message到exchange
//     * 2. 之後exchange根據key會路由到不同que
//     */
//    @Bean
//    Binding topicBinding(@Qualifier("topicQueue") Queue topicQueue, @Qualifier("topicExchange") TopicExchange topicExchange) {
//        return BindingBuilder.bind(topicQueue).to(topicExchange).with("foo.bar.#");
//    }
//
//    @Bean
//    Binding directBinding(@Qualifier("directQueue") Queue directQueue, @Qualifier("directExchange") DirectExchange directExchange) {
//        return BindingBuilder.bind(directQueue).to(directExchange).with("direct.routing.key");
//    }
//    /**
//     * 指定handler
//     * @param receiver
//     * @return
//     */
//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                             MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName, directQueueName);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }

//}
