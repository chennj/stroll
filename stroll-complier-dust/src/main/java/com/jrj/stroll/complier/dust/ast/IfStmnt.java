package com.jrj.stroll.complier.dust.ast;

import java.util.List;

public class IfStmnt extends ASTreeCompound{

	public IfStmnt(List<ASTree> childList) {
		super(childList);
	}

	public ASTree condition(){
		return child(0);
	}
	
	public ASTree thenBlock(){
		return child(1);
	}
	
	public ASTree elseBlock(){
		return numChildren() > 2 ? child(2) : null;
	}
	
	@Override
	public String toString(){
		return "(if " + condition() + " " + thenBlock() + " else " + elseBlock() + ")";
	}
}
