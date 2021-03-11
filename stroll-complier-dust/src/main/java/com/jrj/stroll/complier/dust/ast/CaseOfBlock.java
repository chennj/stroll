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
	
	public ASTree caseOf(int i){
		return child(i);
	}
	
	@Override
	public Object eval(IEnvironment env){
		int size = size();
		for (int i = 0; i < size; i++){
			ASTree o = caseOf(i);
			if (i == size-1 && o instanceof CaseOfDefault){
				return o.eval(env);
			}
			Object c = ((CaseOf)o).condition();
			Object c1 = env.get("switch_condition_result");
			if (String.valueOf(c).equals(String.valueOf(c1))){
				return o.eval(env);
			}
		}
		return 0;
	}
}
