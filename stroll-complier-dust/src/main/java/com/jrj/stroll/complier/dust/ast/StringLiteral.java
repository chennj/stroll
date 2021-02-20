package com.jrj.stroll.complier.dust.ast;

import com.jrj.stroll.complier.dust.lexical.Token;

public class StringLiteral extends ASTreeLeaf{

	public StringLiteral(Token token) {
		super(token);
	}

	public String value(){
		return token().getText();
	}
}
