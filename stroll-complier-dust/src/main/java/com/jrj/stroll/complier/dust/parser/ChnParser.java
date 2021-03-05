package com.jrj.stroll.complier.dust.parser;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.ast.Arguments;
import com.jrj.stroll.complier.dust.ast.BinaryExpr;
import com.jrj.stroll.complier.dust.ast.BlockStmnt;
import com.jrj.stroll.complier.dust.ast.DefStmnt;
import com.jrj.stroll.complier.dust.ast.Name;
import com.jrj.stroll.complier.dust.ast.IfStmnt;
import com.jrj.stroll.complier.dust.ast.NegativeExpr;
import com.jrj.stroll.complier.dust.ast.NullStmnt;
import com.jrj.stroll.complier.dust.ast.NumberLiteral;
import com.jrj.stroll.complier.dust.ast.ParameterList;
import com.jrj.stroll.complier.dust.ast.PrimaryExpr;
import com.jrj.stroll.complier.dust.ast.StringLiteral;
import com.jrj.stroll.complier.dust.ast.WhileStmnt;
import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.lexical.Lexer;
import com.jrj.stroll.complier.dust.lexical.Token;
import com.jrj.stroll.complier.dust.parser.Parser.Operators;

import static com.jrj.stroll.complier.dust.parser.Parser.rule;

/**
 * 语法分析器
 * @author chenn
 *
 */
@Component
public class ChnParser {

	HashSet<String> reserved 	= new HashSet<String>();
	Operators operators 		= new Operators();
	
	Parser args0		= rule(Arguments.class);
	Parser postfix0		= rule();
	Parser expr0 		= rule();
	Parser primary 		= rule(PrimaryExpr.class)
			.or
			(
				rule().sep("(").ast(expr0).sep(")"),
				rule().number(NumberLiteral.class),
				rule().identifier(Name.class, reserved),
				rule().string(StringLiteral.class)
			)
			.repeat(postfix0);
	Parser factor 		= rule()
			.or(rule(NegativeExpr.class).sep("-").ast(primary),primary);
	Parser expr 		= expr0.expression(BinaryExpr.class, factor, operators);
	Parser statement0 	= rule();
	Parser block 		= rule(BlockStmnt.class)
			.option(rule().sep(Token.EOL))
			.sep("{")
			.option(statement0)
			.repeat(rule().sep(";", Token.EOL).option(statement0))
			.sep("}");
	Parser simple 		= rule(PrimaryExpr.class).ast(expr).option(args0);
	Parser statement 	= statement0
			.or
			(
				rule(IfStmnt.class).sep("如果").ast(expr).sep("那么").ast(block).option(rule().sep("否则").ast(block)),
				rule(WhileStmnt.class).sep("当满足条件").ast(expr).sep("循环执行").ast(block),
				simple
			);
	
	Parser param 		= rule().identifier(reserved);
	Parser params 		= rule(ParameterList.class)
			.ast(param)
			.repeat(rule().sep(",").ast(param));
	Parser paramList	= rule().sep("(").maybe(params).sep(")");
	Parser def			= rule(DefStmnt.class)
			.sep("定义").identifier(reserved).ast(paramList).ast(block);
	Parser args			= args0
			.ast(expr)
			.repeat(rule().sep(",").ast(expr));
	Parser postfix		= postfix0
			.sep("(").maybe(args).sep(")");

	Parser program 		= rule()
			.or(def, statement, rule(NullStmnt.class).sep(";",Token.EOL));
	
	/**
	 * 构造函数
	 */
	public ChnParser(){
		reserved.add(";");
		reserved.add("}");
		reserved.add(")");
		reserved.add(Token.EOL);
		
		operators.add("=", 		1, Operators.RIGHT);
		operators.add("取值", 	1, Operators.RIGHT);
		operators.add("&&", 	1, Operators.LEFT);
		operators.add("并且", 	1, Operators.LEFT);
		operators.add("||", 	1, Operators.LEFT);
		operators.add("或者", 	1, Operators.LEFT);
		operators.add("==", 	2, Operators.LEFT);
		operators.add("等于", 	2, Operators.LEFT);
		operators.add(">", 		2, Operators.LEFT);
		operators.add("大于", 	2, Operators.LEFT);
		operators.add("<", 		2, Operators.LEFT);
		operators.add("小于", 	2, Operators.LEFT);
		operators.add("+", 		3, Operators.LEFT);
		operators.add("加", 		3, Operators.LEFT);
		operators.add("-", 		3, Operators.LEFT);
		operators.add("减", 		3, Operators.LEFT);
		operators.add("*", 		4, Operators.LEFT);
		operators.add("乘", 		4, Operators.LEFT);
		operators.add("/", 		4, Operators.LEFT);
		operators.add("除", 		4, Operators.LEFT);
		operators.add("%", 		4, Operators.LEFT);
		operators.add("模", 		4, Operators.LEFT);
	}
	
	public ASTree parse(Lexer lexer) throws ParseException{
		return program.parse(lexer);
	}
}
