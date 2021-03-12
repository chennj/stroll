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
		super("有语法错误，在 " + location(t) + " 附近。 " + msg);
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
			return "最后一行";
		} else {
			return "\"" + t.getText() + "\" 在第 " + t.getLineNumber() + " 行。";
		}
	}
}
