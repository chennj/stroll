package com.jrj.stroll.complier.dust.ast;

import com.jrj.stroll.complier.dust.lexical.Token;

public class Identifier extends ASTreeLeaf{

	public Identifier(Token token) {
		super(token);
	}
	
	public String name(){
		return token().getText();
	}
}
