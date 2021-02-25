package com.jrj.stroll.rpc.core.provider;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrj.stroll.rpc.core.registry.AServiceRegistry;
import com.jrj.stroll.rpc.serializer.Serializer;

/**
 * 
 * @author chenn
 * @服务提供者
 * -- 发布RPC服务<br>
 * -- 接收/处理RPC服务
 *
 */
public class ProviderFactory {

	protected static final Logger logger = LoggerFactory.getLogger(ProviderFactory.class);
	
	private Serializer serializer;
	
	private String ip;					// for registry
	private int port;					// default port
	private String accessToken;
	
	public ProviderFactory(){
		
	}
	
	private Class<? extends AServiceRegistry> serviceRegistryClass;
	private Map<String, String> serviceRegistryParam;
	
	public Serializer getSerializer() {
		return serializer;
	}

	public int getPort() {
		return port;
	}
}
