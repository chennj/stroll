package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;

/**
 * 
 * @author chenn
 *
 */
public class ClassStmnt extends ASTreeCompound{

	public ClassStmnt(List<ASTree> childList) {
		super(childList);
	}

	public String name(){
		return ((ASTreeLeaf)child(0)).token().getText();
	}
	
	public String superClass(){
		if (numChildren() < 3){
			return null;
		} else {
			return ((ASTreeLeaf)child(1)).token().getText();
		}
	}
	
	public ClassBody body(){
		return (ClassBody)child(numChildren() - 1);
	}
	
	
	@Override
	public Object eval(IEnvironment env) {
		// TODO Auto-generated method stub
		return super.eval(env);
	}

	@Override
	public String toString(){
		String parent = superClass();
		if (null == parent){
			parent = "*";
		}
		return "(Class " + name() + " " + parent + " " + body() + ")";
	}
}
