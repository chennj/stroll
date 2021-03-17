package com.jrj.stroll.complier.dust.ast;

import java.sql.Timestamp;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.lexical.Token;

public class DatetimeLiteral extends ASTreeLeaf{

	public DatetimeLiteral(Token token) {
		super(token);
	}

	public Timestamp value(){
		return token().getDatetime();
	}

	@Override
	public Object eval(IEnvironment env) {
		return value();
	}
	
}
