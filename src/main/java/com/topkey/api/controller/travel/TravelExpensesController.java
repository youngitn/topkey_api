package com.topkey.api.controller.travel;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
import com.topkey.api.exception.ValidateException;
import com.topkey.api.util.StandardResponse;

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
	private RabbitTemplate rabbitTemplate;

	@Operation(summary = "出差旅費拋轉ERP")
	@PostMapping("/expensesTransfer")
	public ResponseEntity<StandardResponse> expensesTransfer(@RequestBody ArrayList<Expense> expense) {

		if (expense == null || expense.isEmpty()) {
            return new ResponseEntity<>(new StandardResponse("ERROR", "Expenses list cannot be null or empty", null), HttpStatus.BAD_REQUEST);
        }
		rabbitTemplate.setReplyTimeout(10000); // 10秒超时
		rabbitTemplate.setReceiveTimeout(10000);
		// 生成唯一的 CorrelationData ID
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		// 全局例外拋轉 來自AppWideExceptionHandler
//        if(expense.get(0).getVnedln().equals("1"))
//        	throw new ValidateException("Expenses list cannot be null or empty");
		log.info("expensesTransfer 發送message:" + expense.toString());
//		 TaskResult result = (TaskResult) rabbitTemplate.convertSendAndReceive(
//	                "taskExchange", "taskRoutingKey", taskMessage, message -> {
//	                    message.getMessageProperties().setReplyTo("callbackQueue");
//	                    return message;
//	                }, correlationData);

//		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                MessageProperties messageProperties = message.getMessageProperties();
//                messageProperties.setReplyTo("callbackQueue"); // 设置reply-to属性
//                return message;
//            }
//        };
		StandardResponse res = null;
		try {

			// rabbitTemplate.convertAndSend("topic-exchange", "travel.expense", expense);
			res = (StandardResponse) rabbitTemplate.convertSendAndReceive(
					RabbitMQConfig.topicExchangeName,
					RabbitMQConfig.travelExpenseRoutingKey, expense, correlationData);
		} catch (Exception e) {
			log.error("消息發送失敗", e);
			return new ResponseEntity<>(new StandardResponse("ERROR", "生產者發送失敗: " + e.getMessage(), expense),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (res == null) {
			 return new ResponseEntity<>(new StandardResponse("ERROR", "No response from consumer", expense), HttpStatus.REQUEST_TIMEOUT);
        }
		log.info("消費者回傳處理結果代碼:" + res.getCode());

		if (res != null && "OK".equals(res.getCode())) {
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
		}

	}

}