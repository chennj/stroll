package com.jrj.stroll.complier.dust.ast;

import java.util.List;

/**
 * 参数列表
 * @author chenn
 *
 */
public class ParameterList extends ASTreeCompound{

	public ParameterList(List<ASTree> childList) {
		super(childList);
	}

	public String name(int i){
		return ((ASTreeLeaf)child(i)).token().getText();
	}
	
	public int size(){
		return this.numChildren();
	}
}
