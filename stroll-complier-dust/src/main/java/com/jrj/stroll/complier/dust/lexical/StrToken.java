package com.jrj.stroll.complier.dust.lexical;

public class StrToken extends Token{

	private String literal;
	
	protected StrToken(int line, String str) {
		super(line);
		literal = str;
	}

	@Override
	public boolean isString() {
		return true;
	}

	@Override
	public String getText() {
		return literal;
	}

	
}
