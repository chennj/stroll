package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.exception.DustException;
import static com.jrj.stroll.complier.dust.calc.BasicEvaluator.*;

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
		if ("=".equals(op)){
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
			throw new DustException("bad assignment", this);
		}
	}

	protected Object computOp(Object left, String op, Object right) {
		if (left instanceof Integer && right instanceof Integer){
			return computeNumber((Integer)left, op, (Integer)right);
		} else {
			if (op.equals("+")){
				return String.valueOf(left) + String.valueOf(right);
			} else if (op.equals("==")){
				if (null == left){
					return right == null ? TRUE:FALSE;
				} else {
					return left.equals(right) ? TRUE:FALSE;
				}
			} else {
				throw new DustException("bad type", this);
			}
		}
	}

	protected Object computeNumber(Integer left, String op, Integer right) {
		int a = left.intValue();
		int b = right.intValue();
		switch (op){
		case "+":
			return a + b;
		case "-":
			return a - b;
		case "*":
			return a * b;
		case "/":
			return a / b;
		case "%":
			return a % b;
		case "==":
			return a == b ? TRUE : FALSE;
		case ">":
			return a > b ? TRUE : FALSE;
		case "<":
			return a < b ? TRUE : FALSE;
		default:
			throw new DustException("bad operator",this);
		}
	}	
	
}
