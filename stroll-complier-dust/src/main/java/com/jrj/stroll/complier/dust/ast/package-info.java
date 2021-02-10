/**
 * 
 */
/**
 * @author chenn
 *
 */
package com.jrj.stroll.complier.dust.ast;

/**
 * 文法
 * -- 使用BNF描述（巴科斯范式）
 * -- factor :		NUMBER | "("expression")"
 * -- term :		factor { ("*" | "/") factor }
 * -- expression :	term { ("+" | "-") term }
 */