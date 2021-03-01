package com.jrj.stroll.rpc.serializer.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.jrj.stroll.rpc.core.util.RpcException;
import com.jrj.stroll.rpc.serializer.Serializer;

public class Hessian1Serializer extends Serializer{

	@Override
	public <T> byte[] serialize(T obj){
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HessianOutput ho = new HessianOutput(os);
		try {
			ho.writeObject(obj);
			ho.flush();
			byte[] result = os.toByteArray();
			return result;
		} catch (IOException e) {
			throw new RpcException(e);
		} finally {
			try {
				ho.close();
			} catch (IOException e) {
				throw new RpcException(e);
			}
			try {
				os.close();
			} catch (IOException e) {
				throw new RpcException(e);
			}
		}
	}

	@Override
	public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		HessianInput hi = new HessianInput(is);
		try {
			Object result = hi.readObject();
			return result;
		} catch (IOException e) {
			throw new RpcException(e);
		} finally {
			try {
				hi.close();
			} catch (Exception e) {
				throw new RpcException(e);
			}
			try {
				is.close();
			} catch (IOException e) {
				throw new RpcException(e);
			}
		}
	}

}
