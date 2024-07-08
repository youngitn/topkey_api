package com.topkey.api.converter;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import com.google.gson.Gson;
import com.topkey.api.util.StandardResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 當回傳物件為StandardResponse
 */
@Slf4j
public class GsonMessageConverter implements MessageConverter {

	private final Gson gson = new Gson();

	@Override
	public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
		log.info("object--------------------->" + object.toString());

		return new Message(gson.toJson(object).getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		log.info(message.toString());
		String json = new String(message.getBody());
		log.info(json);
		// 根据消息类型进行反序列化
		//return json; // 这里可以根据需要指定具体的类型
		return gson.fromJson(json, StandardResponse.class);
	}
}
