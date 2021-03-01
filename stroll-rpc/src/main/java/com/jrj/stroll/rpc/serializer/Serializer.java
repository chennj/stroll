package com.jrj.stroll.rpc.serializer;

import com.jrj.stroll.rpc.core.util.RpcException;
import com.jrj.stroll.rpc.serializer.impl.Hessian1Serializer;
import com.jrj.stroll.rpc.serializer.impl.HessianSerializer;
import com.jrj.stroll.rpc.serializer.impl.JacksonSerializer;
import com.jrj.stroll.rpc.serializer.impl.ProtostuffSerializer;

/**
 * 
 * @author chenn
 * @系列化接口
 *
 */
public abstract class Serializer {

	public abstract <T> byte[] serialize(T obj);
	public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);
	
	public enum SerializeEnum {
		HESSIAN(HessianSerializer.class),
		HESSIAN1(Hessian1Serializer.class),
		PROTOSTUFF(ProtostuffSerializer.class),
		JSON(JacksonSerializer.class);

		private Class<? extends Serializer> serializerClass;
		private SerializeEnum (Class<? extends Serializer> serializerClass) {
			this.serializerClass = serializerClass;
		}

		public Serializer getSerializer() {
			try {
				return serializerClass.newInstance();
			} catch (Exception e) {
				throw new RpcException(e);
			}
		}

		public static SerializeEnum match(String name, SerializeEnum defaultSerializer){
			for (SerializeEnum item : SerializeEnum.values()) {
				if (item.name().equals(name)) {
					return item;
				}
			}
			return defaultSerializer;
		}
	}
}
