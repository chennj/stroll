package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;

public abstract class Postfix extends ASTreeCompound{

	public Postfix(List<ASTree> childList) {
		super(childList);
	}
	
	public abstract Object eval(IEnvironment env, Object value);

}
