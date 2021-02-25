package com.jrj.stroll.rpc.serializer;

/**
 * 
 * @author chenn
 * @系列化接口
 *
 */
public abstract class Serializer {

	public abstract <T> byte[] serialize(T obj);
	public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);
}
