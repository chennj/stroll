package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.Function;
import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.calc.NestedEnv;

/**
 * 函数
 * @author chenn
 *
 */
public class DefStmnt extends ASTreeCompound{

	public DefStmnt(List<ASTree> childList) {
		super(childList);
	}

	public String name(){
		return ((ASTreeLeaf)child(0)).token().getText();
	}
	
	public ParameterList parameters(){
		return (ParameterList)child(1);
	}
	
	public BlockStmnt body(){
		return (BlockStmnt)child(2);
	}
	
	@Override
	public Object eval(IEnvironment env) {
		((NestedEnv)env).putNew(name(), new Function(parameters(), body(), env));
		return name();
	}

	@Override
	public String toString(){
		return "(function " + name() + " " + parameters() + " " + body() + ")";
	}
}
