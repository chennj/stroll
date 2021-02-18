package com.jrj.stroll.complier.dust.ast;

import java.util.ArrayList;
import java.util.Iterator;

import com.jrj.stroll.complier.dust.lexical.Token;

public class ASTreeLeaf extends ASTree{

	private static ArrayList<ASTree> empty = new ArrayList<ASTree>();
	
	protected Token token;
	
	public ASTreeLeaf(Token token)
	{
		this.token = token;
	}
	
	public Token token(){
		return token;
	}
	
	@Override
	public ASTree child(int i) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int numChildren() {
		return 0;
	}

	@Override
	public Iterator<ASTree> childIt() {
		return empty.iterator();
	}

	@Override
	public String location() {
		return "at line " + token.getLineNumber();
	}
	
	@Override
	public String toString(){
		return token.getText();
	}

}
