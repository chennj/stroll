package com.jrj.stroll.complier.dust.ast;

import java.util.Iterator;

/**
 * 语法树<br/>
 * -- 抽象基类<br/>
 * -- 整个语法树使用 Composite Pattern 对象结构设计
 * @author chenn
 *
 */
public abstract class ASTree implements Iterable<ASTree>{

	public abstract ASTree children(int i);
	public abstract int numChildren();
	public abstract Iterator<ASTree> childrenIter();
	public abstract String location();
	
	public Iterator<ASTree> iterator(){
		return childrenIter();
	}
}
