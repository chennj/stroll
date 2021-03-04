package com.jrj.stroll.complier.dust.calc;

import java.util.HashMap;

/**
 * 基本环境对象
 * @author chenn
 *
 */
public class BasicEnv implements IEnvironment{

	protected HashMap<String, Object> values;
	
	public BasicEnv(){
		values = new HashMap<>();
	}
	@Override
	public void put(String name, Object value) {
		values.put(name, value);
	}

	@Override
	public Object get(String name) {
		return values.get(name);
	}
	
	@Override
	public IEnvironment where(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
