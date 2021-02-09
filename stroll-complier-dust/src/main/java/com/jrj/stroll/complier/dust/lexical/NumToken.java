package com.jrj.stroll.complier.dust.lexical;

/**
 * 数字单词
 * @author chenn
 *
 */
public class NumToken extends Token{

	private int value;
	
	protected NumToken(int line, int val) {
		super(line);
		value = val;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public int getNumber() {
		return value;
	}

	@Override
	public String getText() {
		return Integer.toString(value);
	}

	
}
