package com.jrj.stroll.complier.dust.calc;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.ast.NullStmnt;
import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.lexical.Lexer;
import com.jrj.stroll.complier.dust.lexical.Token;
import com.jrj.stroll.complier.dust.parser.BasicParser;

@Component
public class BasicInterpreter {

	public static final int TRUE = 1;
	public static final int FALSE = 0;
	
	public void run(String code, List<String> result) throws ParseException{
		
		BasicParser p = new BasicParser();
		IEnvironment env = new BasicEnv();
		
		Lexer lexer = new Lexer(code);
		while (lexer.peek(0) != Token.EOF){
			ASTree t = p.parse(lexer);
			if (!(t instanceof NullStmnt)){
				Object r = t.eval(env);
				System.out.println("=>" + r);
				result.add("=>"+r);
			}
		}
	}
}
