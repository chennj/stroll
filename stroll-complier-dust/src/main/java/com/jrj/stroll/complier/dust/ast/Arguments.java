package com.jrj.stroll.complier.dust.ast;

import java.util.List;

import com.jrj.stroll.complier.dust.calc.Function;
import com.jrj.stroll.complier.dust.calc.IEnvironment;
import com.jrj.stroll.complier.dust.calc.NativeFunction;
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
		
		if (!(value instanceof Function || value instanceof NativeFunction)){
			throw new DustException("未知或不存在的函数", this);
		}
		
		if (value instanceof NativeFunction){
			NativeFunction func = (NativeFunction)value;
			int nParams = func.numOfParameters();
			if (size() != nParams){
				throw new DustException("参数的数目有误", this);
			}
			Object[] args = new Object[nParams];
			int num = 0;
			for (ASTree a : this){
				args[num++] = a.eval(callerEnv);
			}
			return func.invoke(args, this);
		}
		
		Function func = (Function)value;
		ParameterList params = func.parameters();
		if (params.size() != size()){
			throw new DustException("参数的数目有误", this);
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
