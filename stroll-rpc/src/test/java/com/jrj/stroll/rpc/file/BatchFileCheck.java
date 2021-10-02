package com.jrj.stroll.rpc.file;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 批量文件MD5生成器
 * @author cnj
 *
 */
@Service
public final class BatchFileCheck {

	protected static final Logger logger = LoggerFactory.getLogger(BatchFileCheck.class);
	
	public static final int POOL_CORE_SIZE 	= 100;
	public static final int POOL_MAX_SIZE 	= 200;
	public static final int POOL_KEEPALIVE 	= 1000 * 5;
	
	private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static ExecutorService pool;
	
	static{
		pool = new ThreadPoolExecutor(
				POOL_CORE_SIZE, 
				POOL_MAX_SIZE, 
				POOL_KEEPALIVE, 
				TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<Runnable>(),
				new ThreadFactory() {
		            public Thread newThread(Runnable r) {
		                Thread th = new Thread(r,"filebatchmd5"+r.hashCode());
		                return th;
		            }
				},
				new ThreadPoolExecutor.AbortPolicy());
	}
	
	private String checksum(String filepath, MessageDigest md) throws IOException {

        try (InputStream fis = new FileInputStream(filepath)) {
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                md.update(buffer, 0, nread);
            }
        }

        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();

    }

	public HashMap<String,String> syncExec(final List<String> files)
	{
		final HashMap<String, String> result = new HashMap<>();
		
		List<CompletableFuture<Object>> taskList = new ArrayList<>();
		for (String file : files)
		{
					
			CompletableFuture<Object> future = CompletableFuture
					.supplyAsync(()->{	
						
						Map<String,String> m = new HashMap<>();
						try{
							MessageDigest md = MessageDigest.getInstance("MD5");
					        String hex = checksum(file, md);
							m.put(file, hex);
						} catch(Exception e) {
							m.put(file, e.getMessage());
						}
						
						return m;
					},pool)
					.handle((f, e) -> {
	                    if (e != null) {
	                    	Map<String,String> m = new HashMap<>();
	                    	return m.put(file, e.getMessage());
	                    }
	                    return f;
	                });
			taskList.add(future);
		}
		
		CompletableFuture.allOf(taskList.toArray(new CompletableFuture[taskList.size()]));
		
		for (CompletableFuture<Object> f : taskList){
			try {
				HashMap<String, String> m = (HashMap<String, String>)f.get();
				result.putAll(m);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void asyncExec(final List<String> files)
	{
		for (String file : files)
		{			
			CompletableFuture
				.supplyAsync(()->{	
					
					Map<String,String> m = new HashMap<>();
					try{
						MessageDigest md = MessageDigest.getInstance("MD5");
				        String hex = checksum(file, md);
						m.put(file, hex);
					} catch(Exception e) {
						m.put(file, e.getMessage());
					}
					
					return m;
				},pool)
				.handle((f, e) -> {
                    if (e != null) {
                    	Map<String,String> m = new HashMap<>();
                    	return m.put(file, e.getMessage());
                    }
                    return f;
                })
				.thenAccept((o)->{
					HashMap<String, String> m = (HashMap<String, String>)o;
					logger.info(sf.format(new Date()).toString() + ":" + Arrays.asList(m));
				});
		}
		
	}
	
	@Test
	public void testsync()
	{
		BatchFileCheck bf = new BatchFileCheck();
		List<String>  langList = Arrays.asList(
				"D:/temp/pe-01.jpg", 
				"D:/temp/pe-02.jpg", 
				"D:/temp/pe-03.jpg", 
				"D:/temp/pe-04.jpg",
				"D:/temp/pe-05.jpg",
				"D:/temp/server.cpp");
		HashMap<String, String> m = bf.syncExec(langList);
		
		System.out.print("result:"+Arrays.asList(m));
	}
	
	@Test
	public void testAsync()
	{
		BatchFileCheck bf = new BatchFileCheck();
		List<String>  langList = Arrays.asList(
				"D:/temp/pe-01.jpg", 
				"D:/temp/pe-02.jpg", 
				"D:/temp/pe-03.jpg", 
				"D:/temp/pe-04.jpg",
				"D:/temp/pe-05.jpg",
				"D:/temp/server.cpp");
		bf.asyncExec(langList);
		
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
