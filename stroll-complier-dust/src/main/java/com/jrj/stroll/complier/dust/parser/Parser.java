package com.jrj.stroll.complier.dust.parser;

import java.util.HashSet;

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.lexical.Lexer;

/**
 * 语法分析器
 * @author chenn
 *
 */
public class Parser {

	private Class<?> clz;
	
	/**
	 * 创建Parser对象
	 * @return
	 */
	public static Parser rule(){
		return new Parser();
	}
	
	/**
	 * 创建Parser对象
	 * @param c
	 * @return
	 */
	public static Parser rule(Class<?> c){
		Parser p = new Parser();
		p.clz = c;
		return p;
	}
	
	/**
	 * 执行语法分析
	 * @param l
	 */
	public ASTree parse(Lexer l){
		return null;
	}
	
	/**
	 * 向语法规则中添加终结符--整形字面量
	 */
	public Parser number(){
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--整形字面量
	 * @param c
	 */
	public Parser number(Class<?> c){
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--除保留字 r 外的标识符
	 * @param r
	 */
	public Parser identifier(HashSet<String> r){
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--除保留字 r 外的标识符
	 * @param c
	 * @param r
	 */
	public Parser identifier(Class<?> c, HashSet<String> r){
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--字符串字面量
	 */
	public Parser string(){
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--字符串字面量
	 */
	public Parser string(Class<?> c){
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--与pat匹配的标识符
	 * @param pat
	 */
	public Parser token(String... pat){
		return this;
	}
	
	/**
	 * 向语法规则中添加未包含在抽象语法树的终结符-与pat匹配的标识符
	 * @param pat
	 */
	public Parser sep(String... pat){
		return this;
	}
	
	/**
	 * 向语法规则中添加非终结符 p
	 * @param p
	 */
	public Parser ast(Parser p){
		return this;
	}
	
	/**
	 * 向语法规则中添加可省略的非终结符 p
	 * @param p
	 */
	public Parser option(Parser p){
		return this;
	}
	
	/**
	 * 向语法规则中添加可省略的非终结符 p--如果省略，<br/>
	 * 则作为一颗仅有根节点的抽象语法树处理
	 * @param p
	 */
	public Parser maybe(Parser p){
		return this;
	}
	
	/**
	 * 向语法规则中添加若干个由or关系连接的非终结符 p
	 * @param p
	 */
	public Parser or(Parser... p){
		return this;
	}
	
	/**
	 * 向语法规则中添加至少重复0次的非终结符 p
	 * @param p
	 */
	public Parser repeat(Parser p){
		return this;
	}
	
	/**
	 * 向语法规则中添加双目运算表达式
	 * @param p 因子
	 * @param op 运算符表
	 */
	public Parser expression(Parser p, Operators op){
		return this;
	}
	
	/**
	 * 向语法规则中添加双目运算表达式
	 * @param p 因子
	 * @param op 运算符表
	 */
	public Parser expression(Class<?> c, Parser p, Operators op){
		return this;
	}
	
	/**
	 * 清空语法规则
	 */
	public Parser reset(){
		return this;
	}
	
	/**
	 * 清空语法规则，将节点类赋值为c
	 */
	public Parser reset(Class<?> c){
		return this;
	}
	
	/**
	 * 为语法规则起始处的or添加新的分支选项
	 * @param p
	 */
	public Parser insertChoice(Parser p){
		return this;
	}
}
