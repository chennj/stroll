package com.jrj.stroll.complier.dust.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.ast.ASTreeCompound;
import com.jrj.stroll.complier.dust.ast.ASTreeLeaf;
import com.jrj.stroll.complier.dust.ast.Identifier;
import com.jrj.stroll.complier.dust.ast.NumberLiteral;
import com.jrj.stroll.complier.dust.ast.StringLiteral;
import com.jrj.stroll.complier.dust.lexical.Lexer;

/**
 * 语法分析器
 * @author chenn
 *
 */
public class Parser {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected static int REGEX_NONE = -1;
	protected static int REGEX_ASTREE = 0;
	protected static int REGEX_REPEAT = 1;
	protected static int REGEX_MEYBE = 2;
	protected static int REGEX_OR = 3;
	protected static int REGEX_EXPR = 4;
	protected static int REGEX_OPTION = 5;
	
	/**
	 * 非终结符类型
	 */
	protected int mode = REGEX_NONE;
	
	private Class<?> root;
	
	/**
	 * 非终结符
	 */
	private List<Parser> NTList = new ArrayList<>();
	
	/**
	 * 终结符
	 */
	private List<Node> TList = new ArrayList<>();
	
	private Operators operators;
	
	private HashSet<String> reserved;
	
	/**
	 * 创建Parser对象
	 * @return
	 */
	public static Parser rule(){
		Parser p = new Parser();
		p.root = ASTreeCompound.class;
		return new Parser();
	}
	
	/**
	 * 创建Parser对象
	 * @param c
	 * @return
	 */
	public static Parser rule(Class<?> c){
		Parser p = new Parser();
		p.root = c;
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
		TList.add(new Node(NumberLiteral.class,null));
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--整形字面量
	 * @param c
	 */
	public Parser number(Class<? extends ASTree> c){
		TList.add(new Node(c,null));
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--除保留字 r 外的标识符
	 * @param r
	 */
	public Parser identifier(HashSet<String> r){
		for (String s : r){
			switch (s){
			case "NUMBER":
				TList.add(new Node(NumberLiteral.class,null));
				break;
			case "STRING":
				TList.add(new Node(StringLiteral.class,null));
				break;
			case "IDENTIFIER":
				TList.add(new Node(Identifier.class,null));
				break;
			default:
				logger.error("未知的终结符："+s);
				break;
			}
		}
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--除保留字 r 外的标识符
	 * @param c
	 * @param r
	 */
	public Parser identifier(Class<? extends ASTree> c, HashSet<String> r){
		for (String s : r){
			TList.add(new Node(c,s));
		}
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--字符串字面量
	 */
	public Parser string(){
		TList.add(new Node(StringLiteral.class,null));
		return this;
	}
	
	/**
	 * 向语法规则中添加终结符--字符串字面量
	 */
	public Parser string(Class<? extends ASTree> c){
		TList.add(new Node(c,null));
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
		for (String s : pat){
			TList.add(new Node(StringLiteral.class,s));
		}
		return this;
	}
	
	/**
	 * 向语法规则中添加非终结符 p
	 * @param p
	 */
	public Parser ast(Parser p){
		this.mode = REGEX_ASTREE;
		NTList.add(p);
		return this;
	}
	
	/**
	 * 向语法规则中添加可省略的非终结符 p
	 * @param p
	 */
	public Parser option(Parser p){
		this.mode = REGEX_OPTION;
		NTList.add(p);
		return this;
	}
	
	/**
	 * 向语法规则中添加可省略的非终结符 p--如果省略，<br/>
	 * 则作为一颗仅有根节点的抽象语法树处理
	 * @param p
	 */
	public Parser maybe(Parser p){
		this.mode = REGEX_MEYBE;
		NTList.add(p);
		return this;
	}
	
	/**
	 * 向语法规则中添加若干个由or关系连接的非终结符 p
	 * @param p
	 */
	public Parser or(Parser... parsers){
		this.mode = REGEX_OR;
		for (Parser p : parsers){
			NTList.add(p);
		}
		return this;
	}
	
	/**
	 * 向语法规则中添加至少重复0次的非终结符 p
	 * @param p
	 */
	public Parser repeat(Parser p){
		this.mode = REGEX_REPEAT;
		NTList.add(p);
		return this;
	}
	
	/**
	 * 向语法规则中添加双目运算表达式
	 * @param p 因子
	 * @param op 运算符表
	 */
	public Parser expression(Parser p, Operators op){
		this.mode = REGEX_EXPR;
		this.operators = op;
		NTList.add(p);
		return this;
	}
	
	/**
	 * 向语法规则中添加双目运算表达式
	 * @param p 因子
	 * @param op 运算符表
	 */
	public Parser expression(Class<?> c, Parser p, Operators op){
		this.root = c;
		this.mode = REGEX_EXPR;
		this.operators = op;
		NTList.add(p);
		return this;
	}
	
	/**
	 * 清空语法规则
	 */
	public Parser reset(){
		NTList.clear();
		TList.clear();
		return this;
	}
	
	/**
	 * 清空语法规则，将节点类赋值为c
	 */
	public Parser reset(Class<?> c){
		this.root = c;
		NTList.clear();
		TList.clear();
		return this;
	}
	
	/**
	 * 为语法规则起始处的or添加新的分支选项
	 * @param p
	 */
	public Parser insertChoice(Parser p){
		return this;
	}
	
	class Node{
		
		Class<? extends ASTree> type;
		Object data;
		
		public Node(Class<? extends ASTree> type,Object data){
			this.type = type;
			this.data = data;
		}

		public Class<? extends ASTree> getType() {
			return type;
		}

		public Object getData() {
			return data;
		}
		
	}
}
