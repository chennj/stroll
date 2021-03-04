package com.jrj.stroll.complier.dust.calc;

import java.util.HashMap;

/**
 * @作用域
 * -- 根据变量的有效范围可分为空间范围和时间范围，本语言尚不支持在函数内部
 *    定义函数，因此仅有两种作用域，全局和局部<br>
 * -- 为每一种作用域准备一个单独的环境，通常，变量的作用域使用嵌套结构实现。<br>
 * -- 生存周期可以通过 NestedEnv 对象的创建及清除（如垃圾回收）时机来控制。<br>
 * -- 首先查找与最内层作用域对应的环境，如果没有找到，再接着向外逐层查找。<br>
 * @author chenn
 *
 */
public class NestedEnv implements IEnvironment{

	protected HashMap<String, Object> values;
	protected IEnvironment outer;
	
	public NestedEnv(){
		this(null);
	}
	public NestedEnv(IEnvironment env) {
		values = new HashMap<>();
		outer = env;
	}
	
	public void setOuter(IEnvironment env){
		this.outer = env;
	}
	
	public void putNew(String name, Object value){
		values.put(name, value);
	}
	
	@Override
	public void put(String name, Object value) {
		IEnvironment e = where(name);
		if (e == null){
			e = this;
		}
		e.put(name, value);
	}

	@Override
	public Object get(String name) {
		Object v = values.get(name);
		if (null == v && outer != null){
			return outer.get(name);
		} else {
			return v;
		}
	}
	
	@Override
	public IEnvironment where(String name) {
		if (values.get(name) != null){
			return this;
		} else if (outer == null){
			return null;
		} else {
			return outer.where(name);
		}
	}

}
