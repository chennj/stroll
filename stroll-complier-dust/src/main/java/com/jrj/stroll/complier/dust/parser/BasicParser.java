package com.jrj.stroll.complier.dust.parser;

import java.util.HashSet;

import com.jrj.stroll.complier.dust.ast.BinaryExpr;
import com.jrj.stroll.complier.dust.ast.Identifier;
import com.jrj.stroll.complier.dust.ast.NegativeExpr;
import com.jrj.stroll.complier.dust.ast.NumberLiteral;
import com.jrj.stroll.complier.dust.ast.PrimaryExpr;
import com.jrj.stroll.complier.dust.ast.StringLiteral;

import static com.jrj.stroll.complier.dust.parser.Parser.rule;

/**
 * 语法分析器
 * @author chenn
 *
 */
public class BasicParser {

	HashSet<String> reserved = new HashSet<String>();
	Operators operators = new Operators();
	
	Parser expr0 = rule();
	Parser primary = Parser.rule(PrimaryExpr.class)
			.or
			(
				rule().sep("(").ast(expr0).sep(")"),
				rule().number(NumberLiteral.class),
				rule().identifier(Identifier.class, reserved),
				rule().string(StringLiteral.class)
			);
	Parser factor = rule()
			.or(rule(NegativeExpr.class).sep("-").ast(primary),primary);
	Parser expr = expr0.expression(BinaryExpr.class, factor, operators);
	Parser statement0 = rule();
	
}
