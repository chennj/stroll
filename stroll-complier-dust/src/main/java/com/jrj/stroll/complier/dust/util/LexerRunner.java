package com.jrj.stroll.complier.dust.util;

import java.util.ArrayList;
import java.util.List;

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
	
	public String run(String code, List<String> result) throws ParseException
	{
		ArrayList<String> tokes = new ArrayList<>();
		
		Lexer l = new Lexer(code);
		for (Token t; (t=l.read()) != Token.EOF;){
			String r = t.getText();
			tokes.add(r);
			System.out.println("=>"+r);
			result.add("=>"+r);
		}
		
		return tokes.toString();
	}
	
	public Lexer lexer(String code) throws ParseException
	{
		return new Lexer(code);
	}
	
	public static void main(String[] args) throws Exception{
		
		ArrayList<String> result = new ArrayList<>();
		String code = 
				"奇数 取值 0\n"+
				"偶数 取值  0\n"+
				"变量1 取值  1\n"+
				"循环 当 变量1 小于 10 \n"+
				"{\n"+
				"	if i % 2 等于 0 {\n"+
				"		even 取值 even 加 i\n"+
				"	} else {\n"+
				"		odd 取值 odd 加  i\n"+
				"	}\n"+
				"	i 取值 i 加 1\n"+
				"}";
		new LexerRunner().run(code, result);
	}
}
