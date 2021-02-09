package com.jrj.stroll.complier.dust.lexical;

import com.jrj.stroll.complier.dust.exception.DustException;

/**
 * 
 * 单词token <br/>
 * -- 通过正则表达式定义单词 <br/>
 * -- 支持三种类型，标识符、整形、字符串 <br/>
 * -- -- 标识符	：变量名，函数名或类名等名称，还有+-等运算符及括号<br/>
 * -- -- 整形	: 整数值的字符序列<br/>
 * -- -- 字符串	：双引号括起来的字符序列，可以使用转义符"\"<br/>
 * 
 * @author chenn
 *
 */
public abstract class Token {

	/**
	 * 文件结束标志
	 */
	public static final Token EOF = new Token(-1){};
	
	/**
	 * 行结束标志
	 */
	public static final String EOL = "\\n";
	
	private int lineNumber;
	
	protected Token(int line){
		
		this.lineNumber = line;
	}
	
	/**
	 * 返回行标
	 * @return
	 */
	public int getLineNumber(){
		return this.lineNumber;
	}
	
	/**
	 * 是否标识符
	 * @return
	 */
	public boolean isIdentifier(){
		return false;
	}
	
	/**
	 * 是否数字
	 * @return
	 */
	public boolean isNumber(){
		return false;
	}
	
	/**
	 * 是否字符串
	 * @return
	 */
	public boolean isString(){
		return false;
	}
	
	/**
	 * 获得数字
	 * @return
	 */
	public int getNumber(){
		throw new DustException("not number token");
	}
	
	/**
	 * 获取文本
	 * @return
	 */
	public String getText(){
		return "";
	}
}
