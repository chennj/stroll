package com.jrj.stroll.complier.dust.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("lexical")
public class LexicalController {

	@RequestMapping("/code-dialog")
	public String codeDialog(HttpServletRequest request, HttpServletResponse response) {

		return "lexical/code_dialog";
	}
	
	@ResponseBody
	@RequestMapping("/runner")
	public String runner(HttpServletRequest request, HttpServletResponse response) {

		return "lexical/code_dialog";
	}
}
