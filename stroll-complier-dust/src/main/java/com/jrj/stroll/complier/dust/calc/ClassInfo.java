package com.jrj.stroll.complier.dust.calc;

import com.jrj.stroll.complier.dust.ast.ClassStmnt;

/**
 * 
 * @author chenn
 *
 */
public class ClassInfo {

	protected ClassStmnt definition;
	protected IEnvironment environment;
	protected ClassInfo superClass;
	
	public ClassInfo(ClassStmnt cs, IEnvironment env)
	{
		
	}
}
