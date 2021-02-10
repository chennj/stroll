package com.jrj.stroll.complier.dust.util;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.lexical.Lexer;
import com.jrj.stroll.complier.dust.lexical.Token;

/**
 * 
 * @author chenn
 *
 */
@Component
public class LexerRunner {
	
	public String run(String code) throws ParseException
	{
		ArrayList<String> tokes = new ArrayList<>();
		
		Lexer l = new Lexer(code);
		for (Token t; (t=l.read()) != Token.EOF;){
			tokes.add(t.getText());
		}
		System.out.println(tokes);
		return tokes.toString();
	}
}
