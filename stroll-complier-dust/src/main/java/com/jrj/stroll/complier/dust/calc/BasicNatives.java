package com.jrj.stroll.complier.dust.calc;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import com.jrj.stroll.complier.dust.exception.DustException;

public class BasicNatives {

	protected static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
		append(env, "计算斐波那契数", BasicNatives.class, "fib", Double.class);
		append(env, "规则一", BasicNatives.class, "excuRules01", Object.class);
		append(env, "规则二", BasicNatives.class, "excuRules02", Object.class);
		append(env, "规则三", BasicNatives.class, "excuRules03", Object.class);
		append(env, "转换为日期格式", BasicNatives.class, "toDate", Object.class);
		append(env, "现在", BasicNatives.class, "now");
	}
	
	protected void append(IEnvironment env, String name, Class<?> clazz, String methodName, Class<?>...params)
	{
		Method m;
		try {
			m = clazz.getMethod(methodName, params);
		} catch (Exception e){
			throw new DustException("未能找到函数: " + name);
		}
		
		env.put(name, new NativeFunction(name, m));
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
	
	public static double fib(Double n){
		//int in = Integer.valueOf(n.toString());
		if (n < 2){
			return n;
		} else {
			return fib(n-2) + fib(n-1);
		}
	}
	
	public static String excuRules01(Object o){
		String s = o==null ? "":o.toString();
		System.out.println("参数："+s);
		return "执行了规则一("+s+")";
	}
	
	public static String excuRules02(Object o){
		String s = o==null ? "":o.toString();
		System.out.println("参数："+s);
		return "执行了规则二("+s+")";
	}
	
	public static String excuRules03(Object o){
		String s = o==null ? "":o.toString();
		System.out.println("参数："+s);
		return "执行了规则三("+s+")";
	}
	
	public static String toDate(Object o){
		try {
			String s = sf.format(o);
			System.out.println("时间："+s);
			return s;
		} catch (Exception e){
			return e.getMessage();
		}
	}
	
	public static Timestamp now(){
		return new Timestamp(System.currentTimeMillis());
	}
}
