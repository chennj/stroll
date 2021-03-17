package com.jrj.stroll.complier.dust.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.ast.ASTreeCompound;
import com.jrj.stroll.complier.dust.ast.ASTreeLeaf;
import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.lexical.Lexer;
import com.jrj.stroll.complier.dust.lexical.Token;

/**
 * 
 * @author chenn
 * @LL(1)语法分析器
 * -- 使用面向组合子的设计模式：这是一个由组合子逻辑（combinatory logic）<br>
 *    衍生而来的概念，组合子是一种高阶函数，他将接收若干函数作为参数，将其<br>
 *    组合后返回能够执行复杂语法分析的函数称为解析器组合子。<br>
 * -- Y-combinator(fixed-point combinator)：用于表述递归计算。<br>
 * -- Parser 将多个能对简单语法执行语法分析的对象（Parser的内部嵌套类）<br>
 *    组合之后，获得一个能够对更复杂的语法进行分析的新的对象。<br>
 * -- Parser 的嵌套子类用于表现 Parser 对象需要处理的语法规则模式。语言<br>
 *    处理器在将语法规则转换为 Parser 对象时，会调用 number 或 ast 等方<br>
 *    法来构造模式。这些方法将创建 Parser 中嵌套子类的对象。并将他们添<br>
 *    加至 Parser 对象的 elemtns 字段指向的 ArrayList 对象中。<br>
 *    例如，ast 方法会创建一个Tree对象，并将其添加到 ArrayList 中<br>
 * -- Parser 采用LL下降分析法，部分使用了算符优先分析法
 *
 */
public class Parser {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String FACTORY_NAME = "create";
	
	protected List<Element> elements;
	
	protected Factory factory;
	
	/**
	 * 
	 * @author chenn
	 * @执行语法分析的基本组件基类
	 * -- 它的子类是执行语法分析的基本组件，我们将组合这些对象来实现希望的语法分析<br>
	 * -- 它的每一个子类都只执行非常简单的分析
	 *
	 */
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
		 * @throws ParseException 
		 */
		protected Parser choose(Lexer lexer) throws ParseException {
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
	 * @Token语法分析基类
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
	
	/**
	 * Identifier Token 语法分析器
	 * @author chenn
	 *
	 */
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
	
	/**
	 * Number Token 语法分析器
	 * @author chenn
	 *
	 */
	protected static class NumToken extends AToken{

		protected NumToken(Class<? extends ASTreeLeaf> type) {
			super(type);
		}

		@Override
		protected boolean test(Token t) {
			return t.isNumber();
		}
		
	}
	
	/**
	 * Datetime Token 语法分析器
	 * @author chenn
	 *
	 */
	protected static class DatetimeToken extends AToken{

		protected DatetimeToken(Class<? extends ASTreeLeaf> type) {
			super(type);
		}

		@Override
		protected boolean test(Token t) {
			return t.isDatetime();
		}
		
	}
	
	/**
	 * String Token 语法分析器
	 * @author chenn
	 *
	 */
	protected static class StrToken extends AToken{

		protected StrToken(Class<? extends ASTreeLeaf> type) {
			super(type);
		}

		@Override
		protected boolean test(Token t) {
			return t.isString();
		}
		
	}
	
	/**
	 * 不在匹配字符列表中的符号
	 * @author chenn
	 *
	 */
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
				throw new ParseException(tokens[0] + " 预期.", t);
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
	
	/**
	 * 不参与分析的符号
	 * @author chenn
	 *
	 */
	protected static class Skip extends Leaf{

		protected Skip(String[] pat) {
			super(pat);
		}
		
		@Override
		protected void find(List<ASTree> ast, Token t){}
	}
	
	/**
	 * 运算符优先级类
	 * @author chenn
	 *
	 */
	public static class Precedence{
		
		int value;
		/**
		 * 操作符是否左结合
		 */
		boolean leftAssoc;		
		public Precedence(int v, boolean a){
			this.value = v;
			this.leftAssoc = a;
		}
	}
	
	/**
	 * 运算符表
	 * @author chenn
	 *
	 */
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
	
	/**
	 * 表达式分析器
	 * @author chenn
	 *
	 */
	protected static class Expr extends Element{
		
		protected Factory factory;
		protected Operators ops;
		protected Parser factor;
		protected Expr(Class<? extends ASTree> clz, Parser exp, Operators map){
			factory = Factory.getForASTreeCompound(clz);
			ops = map;
			factor = exp;
		}
		
		@Override
		protected void parse(Lexer lexer, List<ASTree> ast) throws ParseException {
			ASTree right = factor.parse(lexer);
			Precedence prec;
			while ((prec=nextOperator(lexer)) != null){
				right = doShift(lexer, right, prec.value);
			}
			ast.add(right);
		}

		@Override
		protected boolean match(Lexer lexer) throws ParseException {
			return factor.match(lexer);
		}
		
		private Precedence nextOperator(Lexer lexer) throws ParseException {
			Token t = lexer.peek(0);
			if (t.isIdentifier()){
				return ops.get(t.getText());
			}
			return null;
		}
		
		private ASTree doShift(Lexer lexer, ASTree left, int prec) throws ParseException {
			ArrayList<ASTree> list = new ArrayList<ASTree>();
			list.add(left);
			list.add(new ASTreeLeaf(lexer.read()));
			ASTree right = factor.parse(lexer);
			Precedence next;
			while ((next = nextOperator(lexer)) != null && rightIsExpr(prec, next)){
				right = doShift(lexer, right, next.value);
			}
			list.add(right);
			return factory.make(list);
		}

		private boolean rightIsExpr(int prec, Precedence nextPrec) {
			if (nextPrec.leftAssoc){
				return prec < nextPrec.value;
			} else {
				return prec <= nextPrec.value;
			}
		}
	}
	
	/**
	 * 
	 * @author chenn
	 * @语法树工厂<br>
	 * -- 生产语法树
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

	protected Parser(Parser p){
		elements = p.elements;
		factory = p.factory;
	}
	
	public Parser(Class<? extends ASTree> clz) {
		reset(clz);
	}

	public ASTree parse(Lexer lexer) throws ParseException {
		ArrayList<ASTree> results = new ArrayList<>();
		for (Element e : elements){
			e.parse(lexer, results);
		}
		return factory.make(results);
	}

	public boolean match(Lexer lexer) throws ParseException {
		if (elements.size() == 0){
			return true;
		} else {
			Element e = elements.get(0);
			return e.match(lexer);
		}
	}
	
	public static Parser rule(){
		return rule(null);
	}	
	public static Parser rule(Class<? extends ASTree> clz){
		return new Parser(clz);
	}
	
	public Parser reset(Class<? extends ASTree> clz){
		elements = new ArrayList<Element>();
		factory = Factory.getForASTreeCompound(clz);
		return this;
	}	
	public Parser reset(){
		elements = new ArrayList<>();
		return this;
	}

	public Parser number(){
		return number(null);
	}	
	public Parser number(Class<? extends ASTreeLeaf> clz) {
		elements.add(new NumToken(clz));
		return this;
	}
	
	public Parser datetime(){
		return datetime(null);
	}	
	public Parser datetime(Class<? extends ASTreeLeaf> clz) {
		elements.add(new DatetimeToken(clz));
		return this;
	}

	public Parser identifier(HashSet<String> reserved){
		return identifier(null,reserved);
	}
	public Parser identifier(Class<? extends ASTreeLeaf> clz, HashSet<String> reserved) {
		elements.add(new IdToken(clz, reserved));
		return this;
	}

	public Parser string(){
		return string(null);
	}
	public Parser string(Class<? extends ASTreeLeaf> clz) {
		elements.add(new StrToken(clz));
		return this;
	}
	
	public Parser token(String... pat){
		elements.add(new Leaf(pat));
		return this;
	}

	public Parser sep(String... pat) {
		elements.add(new Skip(pat));
		return this;
	}

	public Parser ast(Parser p) {
		elements.add(new Tree(p));
		return this;
	}

	public Parser or(Parser...parsers) {
		elements.add(new OrTree(parsers));
		return this;
	}
	
	public Parser maybe(Parser p){
		Parser p2 = new Parser(p);
		p2.reset();
		elements.add(new OrTree(new Parser[]{p, p2}));
		return this;
	}

	public Parser option(Parser p) {
		elements.add(new Repeat(p, true));
		return this;
	}

	public Parser repeat(Parser p) {
		elements.add(new Repeat(p, false));
		return this;
	}

	public Parser expression(Parser subexp, Operators operators) {
		return expression(null, subexp, operators);
	}
	
	public Parser expression(Class<? extends ASTree> clz, Parser subexp, Operators operators) {
		elements.add(new Expr(clz, subexp, operators));
		return this;
	}
	
	public Parser insertChoice(Parser p){
		Element e = elements.get(0);
		if (e instanceof OrTree){
			((OrTree)e).insert(p);
		} else {
			Parser otherwise = new Parser(this);
			reset(null);
			or(p, otherwise);
		}
		return this;
	}
	
}
