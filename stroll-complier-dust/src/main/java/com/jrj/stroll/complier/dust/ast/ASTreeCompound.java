package com.jrj.stroll.complier.dust.ast;

import java.util.Iterator;
import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.exception.DustException;

public class ASTreeCompound extends ASTree{

	protected List<ASTree> childList;
	
	public ASTreeCompound(List<ASTree> childList){
		this.childList = childList;
	}
	
	@Override
	public ASTree child(int i) {
		return childList.get(i);
	}

	@Override
	public int numChildren() {
		return childList.size();
	}

	@Override
	public Iterator<ASTree> childIt() {
		return childList.iterator();
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		String sep = "";
		for (ASTree t : childList){
			sb.append(sep);
			sep = " ";
			sb.append(t.toString());
		}
		return sb.append(')').toString();
	}
	
	@Override
	public String location() {
		for (ASTree t : childList){
			String s = t.location();
			if (null != s)
				return s;
		}
		return null;
	}

	@Override
	public Object eval(IEnvironment env) {
		throw new DustException("不能执行： " + toString(), this);
	}

	
}
