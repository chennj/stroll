package com.jrj.stroll.rpc.core.provider.support.spring;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.jrj.stroll.rpc.core.provider.ProviderFactory;
import com.jrj.stroll.rpc.core.provider.annotation.RpcService;
import com.jrj.stroll.rpc.core.registry.AServiceRegistry;
import com.jrj.stroll.rpc.core.util.RpcException;
import com.jrj.stroll.rpc.serializer.Serializer;

/**
 * rpc provider (for spring)
 * 
 * @author chenn
 *
 */
public class RpcSpringProviderFactory extends ProviderFactory implements ApplicationContextAware, InitializingBean,DisposableBean{

	private String serialize = Serializer.SerializeEnum.HESSIAN.name();

    private String ip;          	// for registry
    private int port;				// default port
    private String accessToken;

    private Class<? extends AServiceRegistry> serviceRegistryClass;                          // class.forname
    private Map<String, String> serviceRegistryParam;
    
    public void setSerialize(String serialize) {
        this.serialize = serialize;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setServiceRegistryClass(Class<? extends AServiceRegistry> serviceRegistryClass) {
        this.serviceRegistryClass = serviceRegistryClass;
    }

    public void setServiceRegistryParam(Map<String, String> serviceRegistryParam) {
        this.serviceRegistryParam = serviceRegistryParam;
    }
    
 // util
    private void prepareConfig(){

        // prepare config
        Serializer.SerializeEnum serializeEnum = Serializer.SerializeEnum.match(serialize, null);
        Serializer serializer = serializeEnum!=null?serializeEnum.getSerializer():null;

        // init config
        super.initConfig(serializer, ip, port, accessToken, serviceRegistryClass, serviceRegistryParam);
    }
    
	@Override
	public void destroy() throws Exception {
		super.stop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.prepareConfig();
        super.start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (serviceBeanMap!=null && serviceBeanMap.size()>0) {
            for (Object serviceBean : serviceBeanMap.values()) {
                // valid
                if (serviceBean.getClass().getInterfaces().length ==0) {
                    throw new RpcException("rpc, service(RpcService) must inherit interface.");
                }
                // add service
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);

                String iface = serviceBean.getClass().getInterfaces()[0].getName();
                String version = rpcService.version();

                super.addService(iface, version, serviceBean);
            }
        }
	}

}
