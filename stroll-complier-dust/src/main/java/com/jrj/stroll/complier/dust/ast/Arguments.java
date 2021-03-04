package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.Function;
import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.calc.NestedEnv;
import com.jrj.stroll.complier.dust.exception.DustException;

public class Arguments extends Postfix{

	public Arguments(List<ASTree> childList) {
		super(childList);
	}

	public int size(){
		return this.numChildren();
	}

	@Override
	public Object eval(IEnvironment callerEnv, Object value) {
		
		if (!(value instanceof Function)){
			throw new DustException("bad function", this);
		}
		
		Function func = (Function)value;
		ParameterList params = func.parameters();
		if (params.size() != size()){
			throw new DustException("bad number of arguments", this);
		}
		
		IEnvironment newEnv = func.makeEnv();
		int num = 0;
		for (ASTree t : this){
			params.eval(newEnv, num++, t.eval(callerEnv));
		}
		
		//动态作用域
		//((NestedEnv)newEnv).setOuter(callerEnv);
		
		return func.body().eval(newEnv);
	}
}
