package com.jrj.stroll.complier.dust.ast;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.lexical.Token;

public class NumberLiteral extends ASTreeLeaf{

	public NumberLiteral(Token token) {
		super(token);
	}

	public int value(){
		return token().getNumber();
	}

	@Override
	public Object eval(IEnvironment env) {
		return value();
	}
	
}
