package com.jrj.stroll.complier.dust.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.lexical.Lexer;
import com.jrj.stroll.complier.dust.lexical.Token;
import com.jrj.stroll.complier.dust.parser.BasicParser;
import com.jrj.stroll.complier.dust.util.LexerRunner;

@Controller
@RequestMapping("parser")
public class ParserController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BasicParser basicParser;
	
	@Autowired
	private LexerRunner lexerRunner;
	
	@RequestMapping(value = {"","/code-dialog"})
	public String codeDialog(HttpServletRequest request, HttpServletResponse response) {

		return "parser/code_dialog";
	}
	
	@ResponseBody
	@RequestMapping(value = "/runner",method = RequestMethod.POST)
	public String runner(@RequestBody Map<String,Object> payload) {

		logger.info("\nenter runner -- data:\n"+payload);
		
		JsonObject result = new JsonObject();
		
		if (null == payload || payload.size() == 0){
			result.addProperty("msg", "no json");
		} else {
			try {
				String code = payload.get("code").toString();
				if (null == code || code.trim().length() == 0){
					result.addProperty("msg", "no code in the json");
				} else {
					String pret = "";
					Lexer lexer = lexerRunner.lexer(code);
					Token t;
					while ((t = lexer.peek(0)) != Token.EOF){
						ASTree ast = basicParser.parse(lexer);
						pret += ast.toString() + "<br>";
						System.out.println("=>"+ast.toString());
					}
					result.addProperty("msg", pret);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.addProperty("msg", e.getMessage());
			}
		}
				
		return result.toString();
	}
}
