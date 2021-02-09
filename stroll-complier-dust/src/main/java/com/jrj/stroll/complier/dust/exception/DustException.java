package com.jrj.stroll.complier.dust.exception;

import com.jrj.stroll.complier.dust.ast.ASTree;

/**
 * 
 * @author chenn
 *
 */
public class DustException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DustException(String m){
		super(m);
	}

	public DustException(String m, ASTree t){
		super(m + " " + t.location());
	}
}
