package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.exception.DustException;

/**
 * 
 * @author chenn
 *
 */
public class Dot extends Postfix{

	public Dot(List<ASTree> childList) {
		super(childList);
	}

	public String name(){
		return ((ASTreeLeaf)child(0)).token().getText();
	}

	@Override
	public Object eval(IEnvironment env, Object value) {
		throw new DustException("不能执行：" + toString(), this);
	}

	@Override
	public String toString(){
		return "." + name();
	}
}
