package com.jrj.stroll.complier.dust.ast;

import com.jrj.stroll.complier.dust.lexical.Token;

public class NumberLiteral extends ASTreeLeaf{

	public NumberLiteral(Token token) {
		super(token);
	}

	public int value(){
		return token().getLineNumber();
	}
}
