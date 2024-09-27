package com.topkey.api.controller.travel;

import java.util.ArrayList;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topkey.api.config.RabbitMQConfig;
import com.topkey.api.dto.travel.Expense;
import com.topkey.api.util.StandardResponse;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/travel")
@RestController
@Tag(name = "Travel Expenses", description = "OA國內出差單旅費拋轉ERP")
public class TravelExpensesController {
	@Autowired
	private final MeterRegistry meterRegistry;
	@Autowired
	private final RabbitTemplate rabbitTemplate;
	@Autowired
	private final Tracer tracer;

	// 使用構造函數注入所有依賴
	public TravelExpensesController(MeterRegistry meterRegistry, RabbitTemplate rabbitTemplate, Tracer tracer,
			ObservationRegistry observationRegistry) {
		this.meterRegistry = meterRegistry;
		this.rabbitTemplate = rabbitTemplate;
		this.tracer = tracer;

	}

	@Operation(summary = "出差旅費拋轉ERP")
	@PostMapping("/expensesTransfer")
	@Observed
	public ResponseEntity<StandardResponse> expensesTransfer(@RequestBody ArrayList<Expense> expense) {
//		meterRegistry.counter("api_errors_total").increment();
//		log.error("************AAAAAAAAAAAAAAAAAAA******************");

//		Span newSpan = tracer.nextSpan().name("producer-span").start();

		if (expense == null || expense.isEmpty()) {
			return new ResponseEntity<>(new StandardResponse("ERROR", "Expenses list cannot be null or empty", null),
					HttpStatus.BAD_REQUEST);
		}
		rabbitTemplate.setReplyTimeout(5000); // 5秒超时
		rabbitTemplate.setReceiveTimeout(5000);
		// 生成唯一的 CorrelationData ID
		// CorrelationData correlationData = new
		// CorrelationData(UUID.randomUUID().toString());
		// 全局例外拋轉 來自AppWideExceptionHandler
//        if(expense.get(0).getVnedln().equals("1"))
//        	throw new ValidateException("Expenses list cannot be null or empty");
//		log.info("expensesTransfer 發送message:" + expense.toString());
//		 TaskResult result = (TaskResult) rabbitTemplate.convertSendAndReceive(
//	                "taskExchange", "taskRoutingKey", taskMessage, message -> {
//	                    message.getMessageProperties().setReplyTo("callbackQueue");
//	                    return message;
//	                }, correlationData);

		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				MessageProperties messageProperties = message.getMessageProperties();
				messageProperties.setReplyTo("callbackQueue"); // 设置reply-to属性
				return message;
			}
		};

		StandardResponse res = null;
		/**
		 * !!注意!! 這裡改為使用callbackRoutingKey 配合dev環境下消費者只會消費callbackRoutingKey來的資料
		 * 避免prod生產者被dev消費者誤消費.
		 */

//		try (Tracer.SpanInScope ws = tracer.withSpan(newSpan)) {
		try {
//			MessagePostProcessor processor = message -> {
//				MessageProperties properties = message.getMessageProperties();
//				Map<String, Object> headers = properties.getHeaders();
//
//				headers.put("X-B3-TraceId", tracer.currentSpan().context().traceId());
//				headers.put("X-B3-SpanId", tracer.currentSpan().context().spanId());
//				return message;
//			};

			// rabbitTemplate.convertAndSend("topic-exchange", "travel.expense", expense);
			res = (StandardResponse) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.topicExchangeName,
					RabbitMQConfig.callbackRoutingKey, expense);
		} catch (Exception e) {
			log.error("消息發送失敗", e);
			return new ResponseEntity<>(new StandardResponse("ERROR", "生產者發送失敗: " + e.getMessage(), expense),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
//		finally {
//			newSpan.end();
//		}

		if (res == null) {

//			 rabbitTemplate.convertSendAndReceive(
//						RabbitMQConfig.dlxExchangeName,
//						RabbitMQConfig.dlxRoutingKey, expense, correlationData);
			return new ResponseEntity<>(new StandardResponse("ERROR", "No response from consumer", expense),
					HttpStatus.REQUEST_TIMEOUT);
		}
		Span span = tracer.nextSpan().name("reply from consumer").start();
		span.event("消費完成回應");
		span.tag("expense", expense.toString());
		span.tag("消費者回傳處理結果代碼", res.getCode());

		log.info("消費者回傳處理結果代碼:" + res.getCode());
		span.end();
		if (res != null && "OK".equals(res.getCode())) {
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}

		// return new ResponseEntity<>(null, HttpStatus.OK);
	}

}