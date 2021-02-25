package com.jrj.stroll.rpc.core.registry.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.jrj.stroll.rpc.core.registry.AServiceRegistry;

/**
 * 本地服务注册
 * @author chenn
 *
 */
public class LocalServiceRegistry extends AServiceRegistry{

	/**
     * registry data
     */
    private Map<String, TreeSet<String>> registryData;


    /**
     * @param param ignore, not use
     */
    @Override
    public void start(Map<String, String> param) {
        registryData = new HashMap<String, TreeSet<String>>();
    }

    @Override
    public void stop() {
        registryData.clear();
    }


    @Override
    public boolean registry(Set<String> keys, String value) {
        if (keys==null || keys.size()==0 || value==null || value.trim().length()==0) {
            return false;
        }
        for (String key : keys) {
            TreeSet<String> values = registryData.get(key);
            if (values == null) {
                values = new TreeSet<>();
                registryData.put(key, values);
            }
            values.add(value);
        }
        return true;
    }

    @Override
    public boolean remove(Set<String> keys, String value) {
        if (keys==null || keys.size()==0 || value==null || value.trim().length()==0) {
            return false;
        }
        for (String key : keys) {
            TreeSet<String> values = registryData.get(key);
            if (values != null) {
                values.remove(value);
            }
        }
        return true;
    }

    @Override
    public Map<String, TreeSet<String>> discovery(Set<String> keys) {
        if (keys==null || keys.size()==0) {
            return null;
        }
        Map<String, TreeSet<String>> registryDataTmp = new HashMap<String, TreeSet<String>>();
        for (String key : keys) {
            TreeSet<String> valueSetTmp = discovery(key);
            if (valueSetTmp != null) {
                registryDataTmp.put(key, valueSetTmp);
            }
        }
        return registryDataTmp;
    }

    @Override
    public TreeSet<String> discovery(String key) {
        return registryData.get(key);
    }
}
