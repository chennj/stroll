package com.jrj.stroll.complier.dust.ast;

import java.util.List;

public class PrimaryExpr extends ASTreeCompound{

	public PrimaryExpr(List<ASTree> childList) {
		super(childList);
	}

	public static ASTree create(List<ASTree> list){
		return list.size() == 1 ? list.get(0) : new PrimaryExpr(list);
	}
}
