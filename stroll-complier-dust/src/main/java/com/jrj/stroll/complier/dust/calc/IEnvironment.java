package com.jrj.stroll.complier.dust.calc;

public interface IEnvironment {
	
	void put(String name, Object value);
	Object get(String name);
	IEnvironment where(String name);
}
