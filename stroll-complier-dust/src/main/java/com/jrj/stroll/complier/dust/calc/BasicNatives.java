package com.jrj.stroll.complier.dust.calc;

import java.lang.reflect.Method;

import javax.swing.JOptionPane;

import com.jrj.stroll.complier.dust.exception.DustException;

public class BasicNatives {

	public IEnvironment environment(IEnvironment env){
		appendNatives(env);
		return env;
	}
	
	protected void appendNatives(IEnvironment env) {
		// ------------
		append(env, "print", BasicNatives.class, "print", Object.class);
		append(env, "read", BasicNatives.class, "read");
		append(env, "length", BasicNatives.class, "length", String.class);
		append(env, "toInt", BasicNatives.class, "toInt", Object.class);
		append(env, "currentTime", BasicNatives.class, "currentTime");
		// ------------
		append(env, "计算斐波那契数", BasicNatives.class, "fib", Object.class);
		append(env, "规则一", BasicNatives.class, "excuRules01", Object.class);
		append(env, "规则二", BasicNatives.class, "excuRules02", Object.class);
		append(env, "规则三", BasicNatives.class, "excuRules03", Object.class);
	}
	
	protected void append(IEnvironment env, String name, Class<?> clazz, String methodName, Class<?>...params)
	{
		Method m;
		try {
			m = clazz.getMethod(methodName, params);
		} catch (Exception e){
			throw new DustException("未能找到函数: " + methodName);
		}
		
		env.put(name, new NativeFunction(methodName, m));
	}
	
	public static int print(Object o){
		System.out.println(o.toString());
		return 0;
	}
	
	public static String read(){
		return JOptionPane.showInputDialog(null);
	}
	
	public static int length(String s){
		return s.length();
	}
	
	public static int toInt(Object value){
		if (value instanceof String){
			return Integer.valueOf((String)value);
		} else if (value instanceof Integer){
			return ((Integer)value).intValue();
		} else {
			throw new NumberFormatException(value.toString());
		}
	}
	
	private static long startTime = System.currentTimeMillis();
	
	public static int currentTime(){
		return (int)(System.currentTimeMillis() - startTime);
	}
	
	public static int fib(Object n){
		int in = Integer.valueOf(n.toString());
		if (in < 2){
			return in;
		} else {
			return fib(in-2) + fib(in-1);
		}
	}
	
	public static String excuRules01(Object o){
		System.out.println();
		return "执行了规则一("+(o==null ? "":o.toString())+")";
	}
	
	public static String excuRules02(Object o){
		System.out.println();
		return "执行了规则二("+(o==null ? "":o.toString())+")";
	}
	
	public static String excuRules03(Object o){
		System.out.println();
		return "执行了规则三("+(o==null ? "":o.toString())+")";
	}
}
