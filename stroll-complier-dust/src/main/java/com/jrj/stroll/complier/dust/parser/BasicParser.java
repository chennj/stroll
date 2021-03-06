package com.jrj.stroll.complier.dust.parser;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.ast.BinaryExpr;
import com.jrj.stroll.complier.dust.ast.BlockStmnt;
import com.jrj.stroll.complier.dust.ast.CaseOf;
import com.jrj.stroll.complier.dust.ast.CaseOfBlock;
import com.jrj.stroll.complier.dust.ast.CaseOfDefault;
import com.jrj.stroll.complier.dust.ast.Name;
import com.jrj.stroll.complier.dust.ast.IfStmnt;
import com.jrj.stroll.complier.dust.ast.NegativeExpr;
import com.jrj.stroll.complier.dust.ast.NullStmnt;
import com.jrj.stroll.complier.dust.ast.NumberLiteral;
import com.jrj.stroll.complier.dust.ast.PrimaryExpr;
import com.jrj.stroll.complier.dust.ast.StringLiteral;
import com.jrj.stroll.complier.dust.ast.SwitchStmnt;
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
public class BasicParser {

	HashSet<String> reserved 	= new HashSet<String>();
	Operators operators 		= new Operators();
	
	Parser expr0 		= rule();
	Parser primary 		= rule(PrimaryExpr.class)
			.or
			(
				rule().sep("(").ast(expr0).sep(")"),
				rule().number(NumberLiteral.class),
				rule().identifier(Name.class, reserved),
				rule().string(StringLiteral.class)
			);
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
	Parser simple 		= rule(PrimaryExpr.class).ast(expr);
	Parser caseof		= rule(CaseOf.class)
			.sep("caseof")
			.or
			(
				rule().number(NumberLiteral.class),
				rule().identifier(Name.class, reserved),
				rule().string(StringLiteral.class)
			)
			.option(rule().sep(Token.EOL))
			.ast(block);
	Parser caseofdef	= rule(CaseOfDefault.class)
			.sep("default")
			.option(rule().sep(Token.EOL))
			.ast(block);
	Parser caseofblock = rule(CaseOfBlock.class)
			.option(rule().sep(Token.EOL))
			.sep("{")
			.option(rule().sep(Token.EOL))
			.ast(caseof)
			.repeat(rule().sep(";", Token.EOL).option(caseof))
			.option(rule().ast(caseofdef).sep(";",Token.EOL))
			.sep("}");
	Parser statement 	= statement0
			.or
			(
				rule(IfStmnt.class).sep("if").ast(expr).ast(block).option(rule().sep("else").ast(block)),
				rule(WhileStmnt.class).sep("while").ast(expr).ast(block),
				rule(SwitchStmnt.class).sep("switch").ast(expr).option(rule().sep(Token.EOL)).ast(caseofblock),
				simple
			);
	Parser program 		= rule()
			.or(statement,rule(NullStmnt.class).sep(";",Token.EOL));
	
	/**
	 * 构造函数
	 */
	public BasicParser(){
		reserved.add(";");
		reserved.add("}");
		reserved.add(Token.EOL);
		
		operators.add("=", 	1, Operators.RIGHT);
		operators.add("&&", 1, Operators.LEFT);
		operators.add("||", 1, Operators.LEFT);
		operators.add("==", 2, Operators.LEFT);
		operators.add(">", 	2, Operators.LEFT);
		operators.add("<", 	2, Operators.LEFT);
		operators.add("+", 	3, Operators.LEFT);
		operators.add("-", 	3, Operators.LEFT);
		operators.add("*", 	4, Operators.LEFT);
		operators.add("/", 	4, Operators.LEFT);
		operators.add("%", 	4, Operators.LEFT);
	}
	
	public ASTree parse(Lexer lexer) throws ParseException{
		return program.parse(lexer);
	}
}
