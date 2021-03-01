package com.jrj.stroll.rpc.serializer.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jrj.stroll.rpc.core.util.RpcException;
import com.jrj.stroll.rpc.serializer.Serializer;

public class JacksonSerializer extends Serializer{

	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public <T> byte[] serialize(T obj) {
		try {
			return objectMapper.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new RpcException(e);
		}
	}

	@Override
	public <T> Object deserialize(byte[] bytes, Class<T> clazz)  {
		try {
			return objectMapper.readValue(bytes, clazz);
		} catch (JsonParseException e) {
			throw new RpcException(e);
		} catch (JsonMappingException e) {
			throw new RpcException(e);
		} catch (IOException e) {
			throw new RpcException(e);
		}
	}

}
