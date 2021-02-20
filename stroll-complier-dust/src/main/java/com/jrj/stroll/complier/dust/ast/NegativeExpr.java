package com.jrj.stroll.complier.dust.ast;

import java.util.List;

public class NegativeExpr extends ASTreeCompound{

	public NegativeExpr(List<ASTree> childList) {
		super(childList);
	}

	public ASTree operand(){
		return child(0);
	}
	
	@Override
	public String toString(){
		return "-"+ operand();
	}
}
