package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;

public class CaseOfBlock extends ASTreeCompound{

	public CaseOfBlock(List<ASTree> childList) {
		super(childList);
	}
	
	public int size(){
		return this.numChildren();
	}
	
	public CaseOf caseOf(int i){
		return (CaseOf) child(i);
	}
	
	@Override
	public Object eval(IEnvironment env){
		for (int i = 0; i < size(); i++){
			Object c = caseOf(i).condition();
			Object c1 = env.get("switch_condition_result");
			if (String.valueOf(c).equals(String.valueOf(c1))){
				return caseOf(i).eval(env);
			}
		}
		return 0;
	}
}
