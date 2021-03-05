package com.jrj.stroll.complier.dust.calc;

import com.jrj.stroll.complier.dust.exception.DustException;

public interface IEval {

	Object eval(IEnvironment env) throws DustException;
}
