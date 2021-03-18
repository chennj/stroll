package com.jrj.stroll.complier.dust.ast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.exception.DustException;
import static com.jrj.stroll.complier.dust.calc.BasicInterpreter.*;

public class BinaryExpr extends ASTreeCompound{
	
	public BinaryExpr(List<ASTree> childList) {
		super(childList);
	}

	public ASTree left(){
		return child(0);
	}
	
	public String operator(){
		return ((ASTreeLeaf)child(1)).token().getText();
	}
	
	public ASTree right(){
		return child(2);
	}

	@Override
	public Object eval(IEnvironment env) {
		String op = operator();
		if ("=".equals(op) || "取值".equals(op)){
			Object right = right().eval(env);
			return computeAssign(env,right);
		} else {
			Object left = left().eval(env);
			Object right = right().eval(env);
			return computOp(left, op, right);
		}
	}

	protected Object computeAssign(IEnvironment env, Object rvalue) {
		ASTree l = left();
		if (l instanceof Name){
			env.put(((Name)l).name(), rvalue);
			return rvalue;
		} else {
			throw new DustException("赋值错误", this);
		}
	}

	protected Object computOp(Object left, String op, Object right) {
		if (left instanceof Number && right instanceof Number){
			return computeNumber((Number)left, op, (Number)right);
		} 
		else if (left instanceof Timestamp || right instanceof Timestamp){
			return computeDatetime(left, op, right);
		}
		else {
			if (op.equals("+") || op.equals("加")){
				return String.valueOf(left) + String.valueOf(right);
			} else if (op.equals("==")){
				if (null == left){
					return right == null ? TRUE:FALSE;
				} else {
					return left.equals(right) ? TRUE:FALSE;
				}
			} else {
				throw new DustException("类型错误：", this);
			}
		}
	}

	protected Object computeNumber(Number left, String op, Number right) {
		double a = left.doubleValue();
		double b = right.doubleValue();
		switch (op){
		case "+":
		case "加":
			return a + b;
		case "-":
		case "减":
			return a - b;
		case "*":
		case "乘":
			return a * b;
		case "/":
		case "除":
			return a / b;
		case "%":
		case "模":
			return a % b;
		case "==":
		case "等于":
			return a == b ? TRUE : FALSE;
		case ">":
		case "大于":
			return a > b ? TRUE : FALSE;
		case ">=":
		case "大于或等于":
			return a >= b ? TRUE : FALSE;
		case "<":
		case "小于":
			return a < b ? TRUE : FALSE;
		case "<=":
		case "小于或等于":
			return a <= b ? TRUE : FALSE;
		case "&&":
		case "并且":
			return (a == TRUE && b == TRUE) ? TRUE : FALSE;
		case "||":
		case "或者":
			return (a == TRUE || b == TRUE) ? TRUE : FALSE;
		default:
			throw new DustException("运算符错误：",this);
		}
	}	
	
	protected Object computeDatetime( Object left, String op, Object right) {
		
		long a=0,b=0;
		
		try {
			if (left instanceof Timestamp){
				SimpleDateFormat sf;
				if (right instanceof Long){
					b = (Long)right;
				} else if (right instanceof Double){
					b = ((Double)right).longValue();
				} else {
					String sl = String.valueOf(right);
					if (sl.indexOf(":")>0){
						sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					} else {
						sf = new SimpleDateFormat("yyyy-MM-dd");
					}
					b = sf.parse(sl).getTime();
				}
				a = ((Timestamp)left).getTime();
			} else {
				SimpleDateFormat sf;
				if (left instanceof Long){
					a = (Long)left;
				} else if (left instanceof Double){
					a = ((Double)left).longValue();
				} else {
					String sr = String.valueOf(left);
					if (sr.indexOf(":")>0){
						sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					} else {
						sf = new SimpleDateFormat("yyyy-MM-dd");
					}
					a = sf.parse(sr).getTime();
				}
				b = ((Timestamp)right).getTime();
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new DustException("日期运算类型错误：",this);
		}

		switch (op){
		case "+":
		case "加":
			return a + b;
		case "-":
		case "减":
			return a - b;
		case "==":
		case "等于":
			return a == b ? TRUE : FALSE;
		case ">":
		case "大于":
			return a > b ? TRUE : FALSE;
		case ">=":
		case "大于或等于":
			return a >= b ? TRUE : FALSE;
		case "<":
		case "小于":
			return a < b ? TRUE : FALSE;
		case "<=":
		case "小于或等于":
			return a <= b ? TRUE : FALSE;
		default:
			throw new DustException("日期暂时不提供这种运算：",this);
		}
	}
}
