package com.jrj.stroll.complier.dust.calc;

import com.jrj.stroll.complier.dust.ast.BlockStmnt;
import com.jrj.stroll.complier.dust.ast.ParameterList;

/**
 * 
 * @author chenn
 *
 */
public class Function {

	protected ParameterList parameters;
	protected BlockStmnt body;
	protected IEnvironment env;
	
	public Function(ParameterList parameters, BlockStmnt body, IEnvironment env)
	{
		this.parameters = parameters;
		this.body = body;
		this.env = env;
	}
	
	public ParameterList parameters(){
		return this.parameters;
	}
	
	public BlockStmnt body(){
		return this.body;
	}
	
	public IEnvironment makeEnv(){
		return new NestedEnv(env);
	}
	
	@Override
	public String toString(){
		return "<func:" + hashCode() + ">";
	}
}
