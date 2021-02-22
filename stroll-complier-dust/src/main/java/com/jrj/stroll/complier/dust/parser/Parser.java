package com.jrj.stroll.complier.dust.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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
import com.jrj.stroll.complier.dust.ast.BinaryExpr;
import com.jrj.stroll.complier.dust.ast.Identifier;
import com.jrj.stroll.complier.dust.ast.NumberLiteral;
import com.jrj.stroll.complier.dust.ast.StringLiteral;
import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.lexical.Lexer;
import com.jrj.stroll.complier.dust.lexical.Token;

/**
 * LL(1)语法分析器
 * @author chenn
 *
 */
public class Parser {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String FACTORY_NAME = "create";
	
	protected static abstract class Element{
		protected abstract void parse(Lexer lexer, List<ASTree> ast) throws ParseException;
		protected abstract boolean match(Lexer lexer) throws ParseException;
	}
	
	protected static class Tree extends Element{

		protected Parser parser;
		
		public Tree(Parser parser){
			this.parser = parser;
		}
		
		@Override
		protected void parse(Lexer lexer, List<ASTree> ast) throws ParseException {
			ast.add(parser.parse(lexer));
		}

		@Override
		protected boolean match(Lexer lexer) throws ParseException {
			return parser.match(lexer);
		}
		
	}
	
	/**
	 * 
	 * @author chenn
	 * @or逻辑分析器
	 * 实现Parser中的or方法，他与铁路图（状态机）中的箭头分支对应<br>
	 * -- 该方法需要确定箭头的走向，可以向前预读一个单词，以确定箭头走向<br>
	 * -- 通过 or 方法的参数传递的 Parser 对象将被 OrTree 对象接收，作为可用的分支选项<br>
	 * -- OrTree 对象的parse方法在调用后，将首先依次调用与各个分支选项对应的 Parser 对象的 <br>
	 *    match 方法，预读一个单词，并将读取的单词与自身表示的模式的头部进行匹配，如果匹配<br>
	 *    成功，OrTree 就会选择该 Parser 对象，并调用该对象的 parse 方法，继续执行语法分析。<br>
	 * -- match 方法在遇到第一个符合条件的 Parser 对象后就会停止匹配，不再检查之后的选项。
	 *
	 */
	protected static class OrTree extends Element{

		/**
		 * 产生式分析器数组
		 */
		protected Parser[] parsers;
		
		/**
		 * 构造函数
		 * @param parsers
		 */
		public OrTree(Parser[] parsers){
			this.parsers = parsers;
		}

		/**
		 * 选择正确的产生式分析器，并加入语法树
		 */
		@Override
		protected void parse(Lexer lexer, List<ASTree> ast) throws ParseException {
			Parser p = choose(lexer);
			if (null == p){
				throw new ParseException(lexer.peek(0));
			} else {
				ast.add(p.parse(lexer));
			}
		}
		
		/**
		 * 预读匹配，判断是否由匹配的产生式分析器
		 */
		@Override
		protected boolean match(Lexer lexer) throws ParseException {
			return choose(lexer) != null;
		}
		
		/**
		 * 根据预读匹配，选择正确的产生式分析器，没有返回null
		 * @param lexer
		 * @return
		 */
		protected Parser choose(Lexer lexer) {
			for (Parser p : parsers){
				if (p.match(lexer)){
					return p;
				}
			}
			return null;
		}
		
		/**
		 * 动态添加产生式分析器
		 * @param p
		 */
		protected void insert(Parser p){
			Parser[] newParsers = new Parser[parsers.length + 1];
			newParsers[0] = p;
			System.arraycopy(parsers, 0, newParsers, 1, parsers.length);
			parsers = newParsers;
		}		
	}
	
	/**
	 * 
	 * @author chenn
	 * @Repeat逻辑分析器
	 * 实现 Parser 的 repeat 逻辑
	 *
	 */
	protected static class Repeat extends Element{

		protected Parser parser;
		protected boolean onlyOnce;
		protected Repeat(Parser p, boolean once) {
			this.parser = p;
			this.onlyOnce = once;
		}
		
		@Override
		protected void parse(Lexer lexer, List<ASTree> ast) throws ParseException {
			while (parser.match(lexer)){
				ASTree t = parser.parse(lexer);
				if (t.getClass() != ASTreeCompound.class || t.numChildren() > 0){
					ast.add(t);
				} 
				if (onlyOnce){
					break;
				}
			}
		}

		@Override
		protected boolean match(Lexer lexer) throws ParseException {
			return parser.match(lexer);
		}
	}
	
	/**
	 * 
	 * @author chenn
	 * @Token语法树基类
	 *
	 */
	protected static abstract class AToken extends Element{
		
		protected Factory factory;
		protected AToken(Class<? extends ASTreeLeaf> type){
			if (null == type){
				type = ASTreeLeaf.class;
			}
			factory = Factory.get(type, Token.class);
		}
		@Override
		protected void parse(Lexer lexer, List<ASTree> ast) throws ParseException{
			Token t = lexer.read();
			if (test(t)){
				ASTree leaf = factory.make(t);
				ast.add(leaf);
			} else {
				throw new ParseException(t);
			}
		}
		@Override
		protected boolean match(Lexer lexer) throws ParseException{
			return test(lexer.peek(0));
		}
		
		protected abstract boolean test(Token t);
	}
	
	protected static class IdToken extends AToken{

		HashSet<String> reserved;
		protected IdToken(Class<? extends ASTreeLeaf> type, HashSet<String> reserved) {
			super(type);
			this.reserved = reserved != null ? reserved : new HashSet<String>();
		}
		
		@Override
		protected boolean test(Token t) {
			return t.isIdentifier() && !reserved.contains(t.getText());
		}
		
	}
	
	protected static class NumToken extends AToken{

		protected NumToken(Class<? extends ASTreeLeaf> type) {
			super(type);
		}

		@Override
		protected boolean test(Token t) {
			return t.isNumber();
		}
		
	}
	
	protected static class StrToken extends AToken{

		protected StrToken(Class<? extends ASTreeLeaf> type) {
			super(type);
		}

		@Override
		protected boolean test(Token t) {
			return t.isString();
		}
		
	}
	
	protected static class Leaf extends Element{
		
		protected String[] tokens;
		protected Leaf(String[] pat){
			tokens = pat;
		}
		
		@Override
		protected void parse(Lexer lexer, List<ASTree> ast) throws ParseException {
			Token t = lexer.read();
			if (t.isIdentifier()){
				for (String token : tokens){
					if (token.equals(t.getText())){
						find(ast, t);
						return;
					}
				}
			}
			if (tokens.length > 0){
				throw new ParseException(tokens[0] + " expected.", t);
			} else {
				throw new ParseException(t);
			}
		}

		@Override
		protected boolean match(Lexer lexer) throws ParseException {
			Token t = lexer.peek(0);
			if (t.isIdentifier()){
				for (String token : tokens){
					if (token.equals(t.getText())){
						return true;
					}
				}
			}
			return false;
		}
			
		protected void find(List<ASTree> ast, Token t) {
			ast.add(new ASTreeLeaf(t));
		}
		
	}
	
	protected static class Skip extends Leaf{

		protected Skip(String[] pat) {
			super(pat);
		}
		
		@Override
		protected void find(List<ASTree> ast, Token t){}
	}
	
	public static class Precedence{
		
		int value;
		/**
		 * 操作符是否左结合
		 */
		boolean leftAssock;		
		public Precedence(int v, boolean a){
			this.value = v;
			this.leftAssock = a;
		}
	}
	
	public static class Operators extends HashMap<String, Precedence>{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public static boolean LEFT = true;
		public static boolean RIGHT = false;
		public void add (String name, int prec, boolean leftAssoc){
			put(name, new Precedence(prec, leftAssoc));
		}
	}
	
	protected static class Expr extends Element{
		
		protected Factory factory;
		protected Operators ops;
		protected Parser parser;
		protected Expr(Class<? extends ASTree> clz, Parser exp, Operators map){
			factory = Factory.getForASTreeCompound(clz);
			ops = map;
			parser = exp;
		}
		
		@Override
		protected void parse(Lexer lexer, List<ASTree> ast) throws ParseException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected boolean match(Lexer lexer) throws ParseException {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	/**
	 * 
	 * @author chenn
	 * @分析器工厂<br>
	 * -- 生产抽象语法树
	 *
	 */
	protected static abstract class Factory{
		
		/**
		 * 将由继承类完成
		 * @param arg
		 * @return
		 * @throws Exception
		 */
		protected abstract ASTree make0(Object arg) throws Exception;
		
		/**
		 * 
		 * @param arg
		 * @return
		 */
		protected ASTree make(Object arg){
			try {
				return make0(arg);
			} catch (IllegalArgumentException e1){
				throw e1;
			} catch (Exception e2){
				// 编译过程中断
				throw new RuntimeException(e2);
			}
		}
		
		/**
		 * 建立 ASTreeCompound的工厂
		 * @param clz
		 * @return
		 */
		protected static Factory getForASTreeCompound(Class<? extends ASTree> clz){
			// 建立带list参数的构造函数的类（clz）的工厂
			Factory f = get(clz, List.class);
			if (null == f){
				f = new Factory(){

					@Override
					protected ASTree make0(Object arg) throws Exception {
						@SuppressWarnings("unchecked")
						List<ASTree> results = (List<ASTree>)arg;
						if (results.size() == 1){
							return results.get(0);
						} else {
							return new ASTreeCompound(results);
						}
					}
					
				};
			}
			return f;
		}
		
		protected static Factory get(Class<? extends ASTree> clz, Class<?> argType){
			if (null == clz){
				return null;
			}
			try {
				final Method m = clz.getMethod(FACTORY_NAME, new Class<?>[]{argType});
				return new Factory(){
					protected ASTree make0(Object arg) throws Exception{
						// 调用clz的static方法，方法名：create
						return (ASTree)m.invoke(null, arg);
					}
				};
			} catch(NoSuchMethodException e){}
			try {
				// 实例化clz
				final Constructor<? extends ASTree> c = clz.getConstructor(argType);
				return new Factory(){
					protected ASTree make0(Object arg) throws Exception{
						return c.newInstance(arg);
					}
				};
			}catch(NoSuchMethodException e){
				throw new RuntimeException(e);
			}
		}
	}

	public Parser(Class<? extends ASTree> clz) {
		// TODO Auto-generated constructor stub
	}

	public ASTree parse(Lexer lexer) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean match(Lexer lexer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static Parser rule(){
		return rule(null);
	}
	
	public static Parser rule(Class<? extends ASTree> clz){
		return new Parser(clz);
	}

	public Parser sep(String... strings) {
		// TODO Auto-generated method stub
		return null;
	}

	public Parser ast(Parser expr0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Parser number(Class<NumberLiteral> class1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Parser identifier(Class<Identifier> class1, HashSet<String> reserved) {
		// TODO Auto-generated method stub
		return null;
	}

	public Parser string(Class<StringLiteral> class1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Parser or(Parser...parsers) {
		// TODO Auto-generated method stub
		return null;
	}

	public Parser expression(Class<BinaryExpr> class1, Parser factor, Operators operators) {
		// TODO Auto-generated method stub
		return null;
	}

	public Parser option(Parser statement0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Parser repeat(Parser option) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
