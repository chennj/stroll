package com.jrj.stroll.complier.dust.lexical;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jrj.stroll.complier.dust.exception.ParseException;

/**
 * 词法分析器 <br/>
 * -- 分割单词<br/>
 * @author chenn
 *
 */
public class Lexer {
		
	/**
	 * document
	 * -- http://www.regular-expressions.info/unicode.html#prop
	 * -- https://www.cnblogs.com/zerotomax/p/6498623.html
	 * 
	 * -- \p{Punct}: 匹配标点符号 !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~ 
	 */
	public static String regexPat = 
			"\\s*("
			+ "(//.*)|"
			+ "([0-9]+(\\.\\d+)?)|"
			+ "(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")|"
			+ "[A-Z_a-z_\u4E00-\u9FA5][A-Z_a-z_\u4E00-\u9FA5_0-9]*|"
			+ "==|\u7b49\u4e8e|"
			+ "<=|\u5c0f\u4e8e\u6216\u7b49\u4e8e|"
			+ ">=|\u5927\u4e8e\u6216\u7b49\u4e8e|"	
			+ "&&|\u5e76\u4e14|"	
			+ "\\|\\||\u6216\u8005|"	
			+ "\\p{Punct}"
			+ ")?";
	
	private Pattern pattern = Pattern.compile(regexPat);
	private ArrayList<Token> queue = new ArrayList<Token>();
	private boolean hasMore;
	private LineNumberReader reader;
	
	public Lexer(Reader r){
		hasMore = true;
		reader = new LineNumberReader(r);
	}
	
	public Lexer(String src){
		hasMore = true;
		reader = new LineNumberReader(new StringReader(src));
	}
	
	public Token read() throws ParseException{
		
		if (fillQueue(0)){
			return queue.remove(0);
		} else {
			return Token.EOF;
		}
	}
	
	public Token peek(int i) throws ParseException{
		
		if (fillQueue(i)){
			return queue.get(i);
		} else {
			return Token.EOF;
		}
	}
	
	private boolean fillQueue(int i) throws ParseException{
		
		while (i >= queue.size()){
			if (hasMore)
				readLine();
			else 
				return false;
		}
		return true;
	}
	
	protected void readLine() throws ParseException{
		
		String line;
		try {
			line = reader.readLine();
		} catch (IOException e){
			throw new ParseException(e);
		}
		
		if (null == line){
			hasMore = false;
			return;
		}
		
		int lineNo = reader.getLineNumber();
		Matcher matcher = pattern.matcher(line);
		matcher.useTransparentBounds(true).useAnchoringBounds(false);
		int pos = 0;
		int endPos = line.length();
		while (pos < endPos){
			matcher.region(pos, endPos);
			if (matcher.lookingAt()){
				addToken(lineNo, matcher);
				pos = matcher.end();
			} else {
				throw new ParseException("bad token at line " + lineNo);
			}
		}
		queue.add(new IdToken(lineNo, Token.EOL));
	}
	
	protected void addToken(int lineNo, Matcher matcher){
		
		String m = matcher.group(1);
		if (null != m){
			//如果不是一个空格
			if (null == matcher.group(2)){
				//如果不是注释
				Token token;
				if (null != matcher.group(3) || null != matcher.group(4)){
					token = new NumToken(lineNo, Double.parseDouble(m));
				} else if (null != matcher.group(5)){
					try {
						SimpleDateFormat sf;;
						if (m.indexOf(":")>0){
							sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						} else {
							sf = new SimpleDateFormat("yyyy-MM-dd");
						}
						Timestamp date = new Timestamp(sf.parse(toStringLiteral(m)).getTime());
						token = new DatetimeToken(lineNo, date);
					} catch (java.text.ParseException e) {
						token = new StrToken(lineNo, toStringLiteral(m));
					}
				} else {
					token = new IdToken(lineNo, m);
				}
				queue.add(token);
			}
		}
	}

	protected String toStringLiteral(String s) {

		StringBuilder sb = new StringBuilder();
		int len = s.length() - 1;
		for (int i=1; i < len; i++){
			char c = s.charAt(i);
			if (c == '\\' && i+1 < len){
				int c2 = s.charAt(i+1);
				if (c2 == '"' || c2 == '\\'){
					c = s.charAt(++i);
				} else if (c2 == 'n'){
					++i;
					c = '\n';
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected static class NumToken extends Token{

		private Double value;
		
		protected NumToken(int line, Double val) {
			super(line);
			value = val;
		}

		@Override
		public boolean isNumber() {
			return true;
		}

		@Override
		public Double getNumber() {
			return value;
		}

		@Override
		public String getText() {
			return Double.toString(value);
		}
		
	}

	protected static class IdToken extends Token{

		private String text;
		
		protected IdToken(int line, String id) {
			super(line);
			text = id;
		}

		@Override
		public boolean isIdentifier() {
			return true;
		}

		@Override
		public String getText() {
			return text;
		}
		
	}
	
	protected static class StrToken extends Token{

		private String literal;
		
		protected StrToken(int line, String str) {
			super(line);
			literal = str;
		}

		@Override
		public boolean isString() {
			return true;
		}

		@Override
		public String getText() {
			return literal;
		}
		
	}
	
	protected static class DatetimeToken extends Token{

		private Timestamp value;
		
		protected DatetimeToken(int line, Timestamp dateTime) {
			super(line);
			value = dateTime;
		}
		
		@Override
		public boolean isDatetime(){
			return true;
		}
		
		@Override
		public Timestamp getDatetime(){
			return value;
		}
		
		@Override
		public String getText() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
		}
	}
	
}
