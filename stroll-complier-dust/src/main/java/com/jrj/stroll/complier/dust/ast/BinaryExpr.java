package com.jrj.stroll.complier.dust.ast;

import java.sql.Timestamp;
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
		long a,b;
		if (!(left instanceof Double) && !(right instanceof Double))
		{
			if (!(left instanceof Timestamp) || !(right instanceof Timestamp)){
				throw new DustException("日期运算类型错误：",this);
			}
		}
		
		if (left instanceof Timestamp && right instanceof Double){
			a = ((Timestamp)left).getTime();
			b = ((Double)right).longValue();
		} else if (right instanceof Timestamp && left instanceof Double){
			a = ((Timestamp)right).getTime();
			b = ((Double)left).longValue();			
		} else {
			a = ((Timestamp)left).getTime();
			b = ((Timestamp)right).getTime();			
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
