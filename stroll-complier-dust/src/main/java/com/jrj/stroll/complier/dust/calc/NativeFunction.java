package com.jrj.stroll.complier.dust.calc;

import java.lang.reflect.Method;

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.exception.DustException;

/**
 * 调用 java 原生函数
 * @author chenn
 *
 */
public class NativeFunction {

	protected Method method;
	protected String name;
	protected int numParams;
	
	public NativeFunction(String n, Method m){
		name = n;
		method = m;
		numParams = m.getParameterTypes().length;
	}
	
	public int numOfParameters() {
		return numParams;
	}

	public Object invoke(Object[] args, ASTree tree) {
		try {
			return method.invoke(null, args);
		} catch(Exception e){
			throw new DustException("错误或未知的函数调用：" + name, tree);
		}
	}

	@Override
	public String toString(){
		return "<native: " + this.hashCode() + ">";
	}
}
