package com.jrj.stroll.rpc.core.provider;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrj.stroll.rpc.core.net.AServer;
import com.jrj.stroll.rpc.core.net.impl.netty.server.NettyServer;
import com.jrj.stroll.rpc.core.net.params.ACallback;
import com.jrj.stroll.rpc.core.net.params.RpcRequest;
import com.jrj.stroll.rpc.core.net.params.RpcResponse;
import com.jrj.stroll.rpc.core.registry.AServiceRegistry;
import com.jrj.stroll.rpc.core.util.IpUtil;
import com.jrj.stroll.rpc.core.util.NetUtil;
import com.jrj.stroll.rpc.core.util.RpcException;
import com.jrj.stroll.rpc.core.util.ThrowableUtil;
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
	
	public void initConfig(
			  Serializer serializer,
			  String ip,
			  int port,
			  String accessToken,
			  Class<? extends AServiceRegistry> serviceRegistryClass,
			  Map<String, String> serviceRegistryParam){
		
		this.serializer = serializer;
		this.ip = ip;
		this.port = port;
		this.accessToken = accessToken;
		this.serviceRegistryClass = serviceRegistryClass;
		this.serviceRegistryParam = serviceRegistryParam;

		if (this.serializer==null) {
			throw new RpcException("rpc provider serializer missing.");
		}
		if (this.ip == null) {
			this.ip = IpUtil.getIp();
		}
		if (this.port <= 0) {
			this.port = 7080;
		}
		if (NetUtil.isPortUsed(this.port)) {
			throw new RpcException("rpc provider port["+ this.port +"] is used.");
		}
		if (this.serviceRegistryClass != null) {
			if (this.serviceRegistryParam == null) {
				throw new RpcException("rpc provider serviceRegistryParam is missing.");
			}
		}
	}
	
	// ---------------------- start / stop ----------------------
	
	private AServer server;
	private AServiceRegistry serviceRegistry;
	private String serviceAddress;
	
	public void start() throws Exception {
		
		// start server
		serviceAddress = IpUtil.getIpPort(this.ip, port);
		server = new NettyServer();
		server.setStartedCallback(new ACallback() {		
			// serviceRegistry started
			@Override
			public void run() throws Exception {
				// start registry
				if (serviceRegistryClass != null) {
					serviceRegistry = serviceRegistryClass.newInstance();
					serviceRegistry.start(serviceRegistryParam);
					if (serviceData.size() > 0) {
						serviceRegistry.registry(serviceData.keySet(), serviceAddress);
					}
				}
			}
		});
		
		server.setStopedCallback(new ACallback() {		// serviceRegistry stoped
			@Override
			public void run() {
				// stop registry
				if (serviceRegistry != null) {
					if (serviceData.size() > 0) {
						serviceRegistry.remove(serviceData.keySet(), serviceAddress);
					}
					serviceRegistry.stop();
					serviceRegistry = null;
				}
			}
		});
		server.start(this);
	}
	
	public void  stop() throws Exception {
		// stop server
		server.stop();
	}
	
	// ---------------------- rpc server invoke ----------------------
	/**
	 * init local rpc service map
	 */
	private Map<String, Object> serviceData = new HashMap<String, Object>();
	public Map<String, Object> getServiceData() {
		return serviceData;
	}

	/**
	 * make service key
	 *
	 * @param iface
	 * @param version
	 * @return
	 */
	public static String makeServiceKey(String iface, String version){
		String serviceKey = iface;
		if (version!=null && version.trim().length()>0) {
			serviceKey += "#".concat(version);
		}
		return serviceKey;
	}
	
	/**
	 * add service
	 *
	 * @param iface
	 * @param version
	 * @param serviceBean
	 */
	public void addService(String iface, String version, Object serviceBean){
		String serviceKey = makeServiceKey(iface, version);
		serviceData.put(serviceKey, serviceBean);

		logger.info(">>>>>>>>>>> rpc, provider factory add service success. serviceKey = {}, serviceBean = {}", serviceKey, serviceBean.getClass());
	}
	
	/**
	 * invoke service
	 *
	 * @param xxlRpcRequest
	 * @return
	 */
	public RpcResponse invokeService(RpcRequest rpcRequest) {

		//  make response
		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.setRequestId(rpcRequest.getRequestId());

		// match service bean
		String serviceKey = makeServiceKey(rpcRequest.getClassName(), rpcRequest.getVersion());
		Object serviceBean = serviceData.get(serviceKey);

		// valid
		if (serviceBean == null) {
			rpcResponse.setErrorMsg("The serviceKey["+ serviceKey +"] not found.");
			return rpcResponse;
		}

		if (System.currentTimeMillis() - rpcRequest.getCreateMillisTime() > 3*60*1000) {
			rpcResponse.setErrorMsg("The timestamp difference between admin and executor exceeds the limit.");
			return rpcResponse;
		}
		if (accessToken!=null && accessToken.trim().length()>0 && !accessToken.trim().equals(rpcRequest.getAccessToken())) {
			rpcResponse.setErrorMsg("The access token[" + rpcRequest.getAccessToken() + "] is wrong.");
			return rpcResponse;
		}

		try {
			// invoke
			Class<?> serviceClass = serviceBean.getClass();
			String methodName = rpcRequest.getMethodName();
			Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
			Object[] parameters = rpcRequest.getParameters();

            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
			Object result = method.invoke(serviceBean, parameters);

			rpcResponse.setResult(result);
		} catch (Throwable t) {
			// catch error
			logger.error("rpc provider invokeService error.", t);
			rpcResponse.setErrorMsg(ThrowableUtil.toString(t));
		}

		return rpcResponse;
	}
}
