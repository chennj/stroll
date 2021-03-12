package com.jrj.stroll.complier.dust.calc;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.ast.NullStmnt;
import com.jrj.stroll.complier.dust.exception.DustException;
import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.lexical.Lexer;
import com.jrj.stroll.complier.dust.lexical.Token;
import com.jrj.stroll.complier.dust.parser.FuncParser;

/**
 * 
 * @author chenn
 *
 */
@Component
public class FuncInterpreter extends BasicInterpreter{
	
	@Override
	public void run(String code, List<String> result) throws DustException, ParseException{
		
		FuncParser p = new FuncParser();
		IEnvironment env = new BasicNatives().environment(new NestedEnv());
		
		Lexer lexer = new Lexer(code);
		Token tmp;
		while ((tmp = lexer.peek(0)) != Token.EOF){
			ASTree t = p.parse(lexer);
			if (!(t instanceof NullStmnt)){
				Object r = t.eval(env);
				System.out.println("=>" + r);
				result.add("=>"+r);
			}
		}
	}
}
