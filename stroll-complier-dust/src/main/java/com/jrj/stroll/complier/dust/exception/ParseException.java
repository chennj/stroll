package com.jrj.stroll.complier.dust.exception;

import java.io.IOException;

import com.jrj.stroll.complier.dust.lexical.Token;

public class ParseException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParseException(Token t){
		this ("",t);
	}

	public ParseException(String msg, Token t) {
		super("syntax error around " + location(t) + ". " + msg);
	}
	
	public ParseException(IOException e)
	{
		super(e);
	}
	
	public ParseException(String msg)
	{
		super(msg);
	}
	
	private static String location(Token t){
		
		if (t == Token.EOF){
			return "the last line";
		} else {
			return "\"" + t.getText() + "\" at line " + t.getLineNumber();
		}
	}
}
