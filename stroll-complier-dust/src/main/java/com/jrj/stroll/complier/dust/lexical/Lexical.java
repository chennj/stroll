package com.jrj.stroll.complier.dust.lexical;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
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
public class Lexical {
	
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
			+ "([0-9]+)|"
			+ "(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")|"
			+ "[A-Z_a-z][A-Z_a-z0-9]*|"
			+ "==|"
			+ "<=|"
			+ ">=|"	
			+ "&&|"	
			+ "\\|\\||"	
			+ "\\p{Punct}"
			+ ")?";
	
	private Pattern pattern = Pattern.compile(regexPat);
	private ArrayList<Token> queue = new ArrayList<Token>();
	private boolean hasMore;
	private LineNumberReader reader;
	
	public Lexical(Reader r){
		hasMore = true;
		reader = new LineNumberReader(r);
	}
	
	public Token read() throws ParseException{
		
		if (fillQueue(0)){
			return queue.remove(0);
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
			queue.add(new IdToken(lineNo, Token.EOL));
		}
	}
	
	protected void addToken(int lineNo, Matcher matcher){
		
		String m = matcher.group(1);
		if (null != m){
			//如果不是一个空格
			if (null == matcher.group(2)){
				//如果不是注释
				Token token;
				if (null != matcher.group(3)){
					token = new NumToken(lineNo, Integer.parseInt(m));
				} else if (null != matcher.group(4)){
					token = new StrToken(lineNo, toStringLiteral(m));
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
}
