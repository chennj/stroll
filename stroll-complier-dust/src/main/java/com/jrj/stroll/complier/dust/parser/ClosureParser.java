package com.jrj.stroll.complier.dust.parser;

import static com.jrj.stroll.complier.dust.parser.Parser.rule;

import org.springframework.stereotype.Component;

/**
 * 扩展 - 使之支持闭包
 * @author chenn
 *
 */
@Component
public class ClosureParser extends FuncParser{

	public ClosureParser()
	{
		primary.insertChoice(
			rule().sep("fun").ast(paramList).ast(block)
		);
	}
}
