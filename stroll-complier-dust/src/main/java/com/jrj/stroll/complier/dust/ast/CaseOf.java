package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;

public class CaseOf extends ASTreeCompound{

	public CaseOf(List<ASTree> childList) {
		super(childList);
	}

	public ASTree condition(){
		return child(0);
	}
	
	public ASTree caseOfBlock(){
		return child(1);
	}
	
	@Override
	public Object eval(IEnvironment env) {
		ASTree b = caseOfBlock();
		return b.eval(env);
	}
	
	@Override
	public String toString(){
		return "(caseof " + condition() + "{"+ caseOfBlock() + "})";
	}
}
