package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;

public class PrimaryExpr extends ASTreeCompound{

	public PrimaryExpr(List<ASTree> childList) {
		super(childList);
	}

	public static ASTree create(List<ASTree> list){
		return list.size() == 1 ? list.get(0) : new PrimaryExpr(list);
	}
	
	public ASTree operand(){
		return child(0);
	}
	
	public Postfix postfix(int nest){
		return (Postfix)child(numChildren() - nest - 1);
	}
	
	public boolean hasPostfix(int nest){
		return numChildren() - nest > 1;
	}

	public Object evalSubExpr(IEnvironment env, int nest){
		if (hasPostfix(nest)){
			Object target = evalSubExpr(env, nest + 1);
			return ((Postfix)postfix(nest)).eval(env, target);
		} else {
			return ((ASTree)operand()).eval(env);
		}
	}
	
	@Override
	public Object eval(IEnvironment env) {
		return evalSubExpr(env, 0);
	}
	
	
}
