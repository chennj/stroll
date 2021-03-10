package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.calc.NestedEnv;

public class SwitchStmnt extends ASTreeCompound{

	public SwitchStmnt(List<ASTree> childList) {
		super(childList);
	}

	public String name(){
		return "switch_condition_result";
	}
	
	public ASTree condition(){
		return child(0);
	}
	
	public CaseOfBlock caseOfBlock(){
		return (CaseOfBlock) child(1);
	}
	
	@Override
	public Object eval(IEnvironment env) {
		Object o = condition().eval(env);
		((NestedEnv)env).putNew(name(), o);
		return caseOfBlock().eval(env);
	}
	
	@Override
	public String toString(){
		return "(switch " + condition() + "{"+ caseOfBlock() + "})";
	}
}
