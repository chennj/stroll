package com.jrj.stroll.complier.dust.parser;

import static com.jrj.stroll.complier.dust.parser.Parser.rule;

import com.jrj.stroll.complier.dust.ast.ClassBody;
import com.jrj.stroll.complier.dust.ast.ClassStmnt;
import com.jrj.stroll.complier.dust.ast.Dot;
import com.jrj.stroll.complier.dust.lexical.Token;

/**
 * 扩展-支持类
 * @author chenn
 *
 */
public class ClassParser extends ClosureParser{

	Parser member = rule().or(def,simple);
	Parser classBody = rule(ClassBody.class)
			.option(rule().sep(Token.EOL))
			.sep("{")
			.option(member)
			.repeat(rule().sep(";",Token.EOL).option(member))
			.sep("}");
	Parser defClass = rule(ClassStmnt.class)
			.sep("class")
			.identifier(reserved)
			.option(rule().sep("extends").identifier(reserved))
			.ast(classBody);
	
	public ClassParser()
	{
		postfix.insertChoice(rule(Dot.class).sep(".").identifier(reserved));
		program.insertChoice(defClass);
	}
}
