package com.jrj.stroll.rpc.core.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrj.stroll.rpc.core.net.params.ACallback;
import com.jrj.stroll.rpc.core.provider.ProviderFactory;

/**
 * 
 * @author chenn
 *
 */
public abstract class AServer {

	protected static final Logger logger = LoggerFactory.getLogger(AServer.class);
	
	private ACallback startedCallback;
	private ACallback stopedCallback;
	
	public void setStartedCallback(ACallback startedCallback) {
		this.startedCallback = startedCallback;
	}
	public void setStopedCallback(ACallback stopedCallback) {
		this.stopedCallback = stopedCallback;
	}
	
	/**
	 * start server
	 * @param providerFactory
	 * @throws Exception
	 */
	public abstract void start(final ProviderFactory providerFactory) throws Exception;
	
	/**
	 * stop server
	 *
	 * @throws Exception
	 */
	public abstract void stop() throws Exception;
	
	
	/**
	 * callback when started
	 */
	public void onStarted() {
		if (startedCallback != null) {
			try {
				startedCallback.run();
			} catch (Exception e) {
				logger.error(">>>>>>>>>>> rpc, server startedCallback error.", e);
			}
		}
	}
	
	/**
	 * callback when stoped
	 */
	public void onStoped() {
		if (stopedCallback != null) {
			try {
				stopedCallback.run();
			} catch (Exception e) {
				logger.error(">>>>>>>>>>> rpc, server stopedCallback error.", e);
			}
		}
	}
}
