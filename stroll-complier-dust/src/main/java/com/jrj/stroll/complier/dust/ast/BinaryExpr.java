package com.jrj.stroll.complier.dust.ast;

import java.util.List;

public class BinaryExpr extends ASTreeCompound{

	public BinaryExpr(List<ASTree> childList) {
		super(childList);
	}

	public ASTree left(){
		return child(0);
	}
	
	public String operator(){
		return ((ASTreeLeaf)child(1)).token().getText();
	}
	
	public ASTree right(){
		return child(2);
	}
}
