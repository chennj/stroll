package com.jrj.stroll.complier.dust.lexical;

public class IdToken extends Token{

	private String text;
	
	protected IdToken(int line, String id) {
		super(line);
		text = id;
	}

	@Override
	public boolean isIdentifier() {
		return true;
	}

	@Override
	public String getText() {
		return text;
	}
	
	
}
