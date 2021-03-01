package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.IEnvironment;

public class BlockStmnt extends ASTreeCompound{

	public BlockStmnt(List<ASTree> childList) {
		super(childList);
	}

	@Override
	public Object eval(IEnvironment env) {
		Object result = 0;
		for (ASTree t : this){
			if (!(t instanceof NullStmnt)){
				result = t.eval(env);
			}
		}
		return result;
	}
	
}
