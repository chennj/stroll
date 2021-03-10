/**
 * 
 */
/**
 * @author chenn
 *
 */
package com.jrj.stroll.complier.dust.ast;

/**
 * 本语言使用的文法简介
 * -- 使用BNF描述（巴科斯范式）
 * -- 上下文无关语法
 * -- 冒号左侧的都是非终结符（或称元变量），右侧大写和双引号括起来的表示终结符
 * -- 下面是一个四则运算的语法规则表示
 * -- factor		: NUMBER | "("expression")"
 * -- term 			: factor { ("*" | "/") factor }		等价于  factor | term ("*" | "/") factor
 * -- expression 	: term { ("+" | "-") term } 		等价于  term | expression ("+" | "-") term
 */

/**
 * 四则运算的语法树节点类设计
 *										-----------------
 *										| 	ASTree		|
 *										|---------------|<-----------------------------------
 *										| child(int i)	|									|
 *										| childIt()		|									|
 *										|---------------|									|
 *										  	   /_\											|
 *										   		|											|
 *							-----------------------------------------						|						   
 *							|										|						|
 *							|										|						|
 *					-----------------						-----------------				|
 *					| 	ASTreeLeaf	|						| ASTreeCompound|				|
 *					|---------------|						|---------------|<>-------------|
 *					| child(int i)	|						| child(int i)	|
 *					| childIt()		|						| childIt()		|
 *					|---------------|						|---------------|
 *						   /_\									   /_\
 *							|										|
 *			---------------------------------						|
 *			|								|						|
 *			|								|						|
 *	-----------------				-----------------		-----------------
 *	| NumberLiteral	|				| Identifier	|		| 	BinaryExpr	|
 *	|---------------|				|---------------|		|---------------|
 *	|---------------|				|---------------|		|---------------|
 */

/**
 * dust 语言的语法定义
 * -- param			: IDENTIFIER
 * 					形参
 * -- params		: param {"," param}
 * -- param_list	: "(" [ params ] ")"
 * -- def			: "def" IDENTIFIER param_list block
 * 					函数
 * -- caseof		: "caseof" expr block
 * -- caseofblock	: "{" caseof { (";" | EOL) [ caseof ] } "}"
 * -- primary 		: ( "(" expr ")" | NUMBER | IDENTIFIER | STRING ) { postfix }
 * 					| " fun " param_list block #闭包
 * -- factor 		: primary | "-" primary
 * 					表示一个 primary 或 再添加一个 ”减号“的组合
 * -- expr 			: factor { OP factor }
 * -- args			: expr {"," expr}
 * 					实参
 * -- postfix		: "(" [ args ] ")"
 * 					表示括号括起的表达式、整形字面量、标识符（变量名）、字符串字面量
 * 					表示两个 factor 之间夹有一个双目运算符的组合
 * -- block 		: "{" [ statement ] { (";" | EOL) [ statement ] } "}"
 * 					表示由{}括起来的 statement（语句）序列，statement 之间需要用分号或者换行符 EOL 分割，
 * 					因为 dust 语言支持空语句，因此规则中的 statement 两侧写有中括号 [] 
 * -- simple 		: expr [ args ]
 * 					表示简单表达式语句
 * -- statement 	: "if" expr block [ "else" block ]
 * 					| "while" expr block
 * 					| "switch" expr caseofblock
 * 					| simple
 * 					语句的组成规则：if、while、simple
 * -- program 		: [ def | statement ] (";" | EOL)
 * 					程序的组成：语句加“;”或则换行符，也可以是空语句
 */

