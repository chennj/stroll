package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.Function;
import com.jrj.stroll.complier.dust.calc.IEnvironment;

/**
 * 闭包
 * @author chenn
 *
 */
public class Fun extends ASTreeCompound{

	public Fun(List<ASTree> childList) {
		super(childList);
	}

	public ParameterList parameters(){
		return (ParameterList)child(0);
	}
	
	public BlockStmnt body(){
		return (BlockStmnt)child(1);
	}
	
	@Override
	public Object eval(IEnvironment env){
		return new Function(parameters(), body(), env);
	}
	
	@Override
	public String toString(){
		return "(fun " + parameters() + " " + body() + ")";
	}
}
