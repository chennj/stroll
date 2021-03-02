package com.jrj.stroll.rpc.core.registry.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author chenn
 *
 */
public class RegistryBaseClient {

	private static Logger logger = LoggerFactory.getLogger(RegistryBaseClient.class);
	
	private String adminAddress;
    private String accessToken;
    private String biz;
    private String env;
    
    private List<String> adminAddressArr;
    
    public RegistryBaseClient(String adminAddress, String accessToken, String biz, String env)
    {
    	this.adminAddress = adminAddress;
        this.accessToken = accessToken;
        this.biz = biz;
        this.env = env;

        // valid
        if (adminAddress==null || adminAddress.trim().length()==0) {
            throw new RuntimeException("registry adminAddress empty");
        }
        if (biz==null || biz.trim().length()<4 || biz.trim().length()>255) {
            throw new RuntimeException("registry biz empty Invalid[4~255]");
        }
        if (env==null || env.trim().length()<2 || env.trim().length()>255) {
            throw new RuntimeException("registry biz env Invalid[2~255]");
        }

        // parse
        adminAddressArr = new ArrayList<>();
        if (adminAddress.contains(",")) {
            adminAddressArr.addAll(Arrays.asList(adminAddress.split(",")));
        } else {
            adminAddressArr.add(adminAddress);
        }
    }
}
