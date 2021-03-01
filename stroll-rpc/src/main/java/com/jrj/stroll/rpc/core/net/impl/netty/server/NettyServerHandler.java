package com.jrj.stroll.rpc.core.net.impl.netty.server;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrj.stroll.rpc.core.net.params.RpcRequest;
import com.jrj.stroll.rpc.core.net.params.RpcResponse;
import com.jrj.stroll.rpc.core.provider.ProviderFactory;
import com.jrj.stroll.rpc.core.util.ThrowableUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest>{

	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
	
	private ProviderFactory providerFactory;
    private ThreadPoolExecutor serverHandlerPool;
    
	public NettyServerHandler(ProviderFactory providerFactory, ThreadPoolExecutor serverHandlerPool) {
		this.providerFactory = providerFactory;
		this.serverHandlerPool = serverHandlerPool;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
		
		try {
            // do invoke
            serverHandlerPool.execute(new Runnable() {
                @Override
                public void run() {
                    // invoke + response
                    RpcResponse rpcResponse = providerFactory.invokeService(rpcRequest);

                    ctx.writeAndFlush(rpcResponse);
                }
            });
        } catch (Exception e) {
            // catch error
        	RpcResponse rpcResponse = new RpcResponse();
        	rpcResponse.setRequestId(rpcRequest.getRequestId());
        	rpcResponse.setErrorMsg(ThrowableUtil.toString(e));

            ctx.writeAndFlush(rpcResponse);
        }
	}

	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	logger.error(">>>>>>>>>>> rpc provider netty server caught exception", cause);
        ctx.close();
    }
}
