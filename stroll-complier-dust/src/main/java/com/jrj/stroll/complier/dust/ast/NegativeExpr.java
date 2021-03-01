package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.exception.DustException;

public class NegativeExpr extends ASTreeCompound{

	public NegativeExpr(List<ASTree> childList) {
		super(childList);
	}

	public ASTree operand(){
		return child(0);
	}
	
	@Override
	public String toString(){
		return "-"+ operand();
	}

	@Override
	public Object eval(IEnvironment env) {
		Object v = operand().eval(env);
		if (v instanceof Integer){
			return new Integer(-((Integer)v).intValue());
		} else {
			throw new DustException("bad type for -", this);
		}
	}
	
	
}
