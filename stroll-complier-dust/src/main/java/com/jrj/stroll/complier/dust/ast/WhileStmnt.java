package com.jrj.stroll.complier.dust.ast;

import java.util.List;

public class WhileStmnt extends ASTreeCompound{

	public WhileStmnt(List<ASTree> childList) {
		super(childList);
	}

	public ASTree condition(){
		return child(0);
	}
	
	public ASTree body(){
		return child(1);
	}
	
	@Override
	public String toString(){
		return "(while " + condition() + " " + body() + ")";
	}
}
