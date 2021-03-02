package com.jrj.stroll.complier.dust.ast;

import java.util.List;

public class Arguments extends Postfix{

	public Arguments(List<ASTree> childList) {
		super(childList);
	}

	public int size(){
		return this.numChildren();
	}
}
