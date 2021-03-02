package com.jrj.stroll.rpc.core.net.impl.netty.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.jrj.stroll.rpc.core.net.AServer;
import com.jrj.stroll.rpc.core.provider.ProviderFactory;
import com.jrj.stroll.rpc.core.util.RpcException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty rpc server
 * @author chenn
 *
 */
public class NettyServer extends AServer {

	private Thread thread;
	
	@Override
	public void start(ProviderFactory providerFactory) throws Exception {
		
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				
				// param
				final ThreadPoolExecutor serverHandlerPool = new ThreadPoolExecutor(
						60,
						300,
						60L,
						TimeUnit.SECONDS,
						new LinkedBlockingQueue<Runnable>(1000),
						new RejectedExecutionHandler() {
							@Override
							public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
								throw new RpcException("rpc NettyServer Thread pool is EXHAUSTED!");
							}
						});		// default maxThreads 300, minThreads 60
				
				EventLoopGroup bossGroup = new NioEventLoopGroup();
				EventLoopGroup workerGroup = new NioEventLoopGroup();
				
				try {
					// start server
					ServerBootstrap bootstrap = new ServerBootstrap();
					bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
							.childHandler(new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel channel) throws Exception {
									channel.pipeline()
									.addLast(new NettyServerHandler(providerFactory, serverHandlerPool));
								}
							})
							.option(ChannelOption.SO_TIMEOUT, 100)
							.option(ChannelOption.SO_BACKLOG, 128)
							.option(ChannelOption.TCP_NODELAY, true)
							.option(ChannelOption.SO_REUSEADDR, true)
							.childOption(ChannelOption.SO_KEEPALIVE, true);

					// bind
					ChannelFuture future = bootstrap.bind(providerFactory.getPort()).sync();

                    logger.info(">>>>>>>>>>> rpc remoting server start success, nettype = {}, port = {}", NettyServer.class.getName(), providerFactory.getPort());
					onStarted();

					// wait util stop
					future.channel().closeFuture().sync();

				} catch (Exception e) {
					if (e instanceof InterruptedException) {
						logger.info(">>>>>>>>>>> rpc remoting server stop.");
					} else {
						logger.error(">>>>>>>>>>> rpc remoting server error.", e);
					}
				} finally {

					// stop
					try {
						serverHandlerPool.shutdown();	// shutdownNow
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					try {
						workerGroup.shutdownGracefully();
						bossGroup.shutdownGracefully();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}

				}

			}
			
		});
		
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void stop() throws Exception {
		
		// destroy server thread
        if (thread!=null && thread.isAlive()) {
            thread.interrupt();
        }

		// on stop
        onStoped();
        logger.info(">>>>>>>>>>> rpc remoting server destroy success.");
	}

}
