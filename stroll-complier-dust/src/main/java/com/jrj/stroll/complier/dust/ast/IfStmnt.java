package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;

import static com.jrj.stroll.complier.dust.calc.BasicInterpreter.*;

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

	@Override
	public Object eval(IEnvironment env) {
		Object c = condition().eval(env);
		if (c instanceof Integer && ((Integer)c).intValue() != FALSE){
			return thenBlock().eval(env);
		} else {
			ASTree b = elseBlock();
			if (b == null){
				return 0;
			} else {
				return b.eval(env);
			}
		}
	}
	
}
