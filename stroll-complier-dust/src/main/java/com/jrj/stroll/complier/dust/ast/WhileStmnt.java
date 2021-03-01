package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;
import static com.jrj.stroll.complier.dust.calc.BasicEvaluator.*;

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

	@Override
	public Object eval(IEnvironment env) {
		Object result = 0;
		for (;;){
			Object c = condition().eval(env);
			if (c instanceof Integer && ((Integer)c).intValue() == FALSE){
				return result;
			} else {
				result = body().eval(env);
			}
		}
	}
	
	
}
