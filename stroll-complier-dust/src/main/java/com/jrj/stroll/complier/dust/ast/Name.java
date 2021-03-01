package com.jrj.stroll.complier.dust.ast;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.exception.DustException;
import com.jrj.stroll.complier.dust.lexical.Token;

public class Name extends ASTreeLeaf{

	public Name(Token token) {
		super(token);
	}
	
	public String name(){
		return token().getText();
	}

	@Override
	public Object eval(IEnvironment env) {
		Object value = env.get(name());
		if (null == value){
			throw new DustException("undefined name:" + name(), this);
		} else {
			return value;
		}
	}
	
}
