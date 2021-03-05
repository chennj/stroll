package com.jrj.stroll.complier.dust.ast;

import java.util.Iterator;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.calc.IEval;
import com.jrj.stroll.complier.dust.exception.DustException;

/**
 * 语法树<br/>
 * -- 抽象基类<br/>
 * -- 整个语法树使用 Composite Pattern 对象结构设计<br/>
 * -- 更详细的在package-info.java中说明
 * @author chenn
 *
 */
public abstract class ASTree implements Iterable<ASTree>, IEval{

	public abstract ASTree child(int i);
	public abstract int numChildren();
	public abstract Iterator<ASTree> childIt();
	public abstract String location();
	
	@Override
	public abstract Object eval(IEnvironment env) throws DustException;
	
	@Override
	public Iterator<ASTree> iterator(){
		return childIt();
	}
}
