package com.jrj.stroll.complier.dust.ast;

import java.util.List;

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
	
	public String toString(){
		return "(function " + name() + " " + parameters() + " " + body() + ")";
	}
}
