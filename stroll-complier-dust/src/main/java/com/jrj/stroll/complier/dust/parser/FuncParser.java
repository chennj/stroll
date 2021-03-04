package com.jrj.stroll.complier.dust.parser;

import static com.jrj.stroll.complier.dust.parser.Parser.rule;

import org.springframework.stereotype.Component;

import com.jrj.stroll.complier.dust.ast.Arguments;
import com.jrj.stroll.complier.dust.ast.DefStmnt;
import com.jrj.stroll.complier.dust.ast.ParameterList;

/**
 * 扩展 - 使之支持函数
 * @author chenn
 *
 */
@Component
public class FuncParser extends BasicParser{

	Parser param 		= rule().identifier(reserved);
	Parser params 		= rule(ParameterList.class)
			.ast(param)
			.repeat(rule().sep(",").ast(param));
	Parser paramList	= rule().sep("(").maybe(params).sep(")");
	Parser def			= rule(DefStmnt.class)
			.sep("def").identifier(reserved).ast(paramList).ast(block);
	Parser args			= rule(Arguments.class)
			.ast(expr)
			.repeat(rule().sep(",").ast(expr));
	Parser postfix		= rule()
			.sep("(").maybe(args).sep(")");
	
	public FuncParser(){
		reserved.add(")");
		primary.repeat(postfix);
		simple.option(args);
		program.insertChoice(def);
	}
}
