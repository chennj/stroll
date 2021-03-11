package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;

public class CaseOfDefault extends ASTreeCompound{

	public CaseOfDefault(List<ASTree> childList) {
		super(childList);
	}
	
	public ASTree caseOfBlock(){
		return child(0);
	}
	
	@Override
	public Object eval(IEnvironment env) {
		ASTree b = caseOfBlock();
		return b.eval(env);
	}
	
	@Override
	public String toString(){
		return "(default {"+ caseOfBlock() + "})";
	}
}
