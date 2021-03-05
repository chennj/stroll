package com.jrj.stroll.complier.dust.web.controller;

import java.util.ArrayList;
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

import com.jrj.stroll.complier.dust.ast.ASTree;
import com.jrj.stroll.complier.dust.calc.BasicEvaluator;
import com.jrj.stroll.complier.dust.calc.ChnEvaluator;
import com.jrj.stroll.complier.dust.calc.FuncEvaluator;
import com.jrj.stroll.complier.dust.exception.DustException;
import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.lexical.Lexer;
import com.jrj.stroll.complier.dust.lexical.Token;
import com.jrj.stroll.complier.dust.parser.BasicParser;
import com.jrj.stroll.complier.dust.parser.FuncParser;
import com.jrj.stroll.complier.dust.util.LexerRunner;

@Controller
@RequestMapping("parser")
public class ParserController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LexerRunner lexerRunner;
	
	@Autowired
	private BasicParser basicParser;
	
	@Autowired
	private BasicEvaluator basicEvaluator;
	
	@Autowired
	private FuncParser funcParser;
	
	@Autowired
	private FuncEvaluator funcEvaluator;
	
	@Autowired
	private ChnEvaluator chnEvaluator;
	
	@RequestMapping(value = {"","/code-dialog"})
	public String codeDialog(HttpServletRequest request, HttpServletResponse response) {

		return "parser/code_dialog";
	}
	
	// ------------------------ lexical ---------------------------------------------
	@ResponseBody
	@RequestMapping(value = "/lexer",method = RequestMethod.POST)
	public String runner(@RequestBody Map<String,Object> payload) {

		logger.info("\nenter lexer");
		
		String result="";
		
		if (null == payload || payload.size() == 0){
			result = "no json";
		} else {
			try {
				String code = payload.get("code").toString();
				if (null == code || code.trim().length() == 0){
					result = "no code in the json";
				} else {
					logger.info("\nprogram code ---- \n"+code);
					ArrayList<String> rets = new ArrayList<>();
					lexerRunner.run(payload.get("code").toString(), rets);
					for (String s : rets){
						result += s + "<br>";
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = e.getMessage();
			}
		}
				
		return result;
	}
	
	// ------------------------ syntax basic -----------------------------------------------
	
	@ResponseBody
	@RequestMapping(value = "/parse",method = RequestMethod.POST)
	public String parse(@RequestBody Map<String,Object> payload) {

		logger.info("\nenter parse");
		
		String result;
		
		if (null == payload || payload.size() == 0){
			result = "no json";
		} else {
			try {
				String code = payload.get("code").toString();
				if (null == code || code.trim().length() == 0){
					result = "no code in the json";
				} else {
					logger.info("\nprogram code ---- \n"+code);
					String pret = "";
					Lexer lexer = lexerRunner.lexer(code);
					Token t;
					while ((t = lexer.peek(0)) != Token.EOF){
						ASTree ast = basicParser.parse(lexer);
						pret += "=>" + ast.toString() + "<br/>";
						System.out.println("=>"+ast.toString());
					}
					result = pret;
				}
			} catch (DustException e) {
				e.printStackTrace();
				result = e.getMessage();
			} catch (ParseException e1){
				e1.printStackTrace();
				result = e1.getMessage();
			}
		}
				
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/eval",method = RequestMethod.POST)
	public String eval(@RequestBody Map<String,Object> payload){
		
		logger.info("\nenter eval");
		
		String result="";
		
		if (null == payload || payload.size() == 0){
			result = "no json";
		} else {
			try {
				String code = payload.get("code").toString();
				if (null == code || code.trim().length() == 0){
					result = "no code in the json";
				} else {
					logger.info("\nprogram code ---- \n"+code);
					ArrayList<String> rets = new ArrayList<>();
					basicEvaluator.run(code, rets);
					for (String s : rets){
						result += s + "<br>";
					}
				}
			} catch (DustException e) {
				e.printStackTrace();
				result = e.getMessage();
			} catch (ParseException e1){
				e1.printStackTrace();
				result = e1.getMessage();
			}
		}
				
		return result;
	}
	
	// ------------------------- syntax added function -------------------------------
	@ResponseBody
	@RequestMapping(value = "/parse_f",method = RequestMethod.POST)
	public String parsef(@RequestBody Map<String,Object> payload) {

		logger.info("\nenter parsef ");
		
		String result;
		
		if (null == payload || payload.size() == 0){
			result = "no json";
		} else {
			try {
				String code = payload.get("code").toString();
				if (null == code || code.trim().length() == 0){
					result = "no code in the json";
				} else {
					logger.info("\nprogram code ---- \n"+code);
					String pret = "";
					Lexer lexer = lexerRunner.lexer(code);
					Token t;
					while ((t = lexer.peek(0)) != Token.EOF){
						ASTree ast = funcParser.parse(lexer);
						pret += "=>" + ast.toString() + "<br/>";
						System.out.println("=>"+ast.toString());
					}
					result = pret;
				}
			} catch (DustException e) {
				e.printStackTrace();
				result = e.getMessage();
			} catch (ParseException e1){
				e1.printStackTrace();
				result = e1.getMessage();
			}
		}
				
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/eval_f",method = RequestMethod.POST)
	public String evalf(@RequestBody Map<String,Object> payload){
		
		logger.info("\nenter evalf");
		
		String result="";
		
		if (null == payload || payload.size() == 0){
			result = "no json";
		} else {
			try {
				String code = payload.get("code").toString();
				if (null == code || code.trim().length() == 0){
					result = "no code in the json";
				} else {
					logger.info("\nprogram code ---- \n"+code);
					ArrayList<String> rets = new ArrayList<>();
					funcEvaluator.run(code, rets);
					for (String s : rets){
						result += s + "<br>";
					}
				}
			} catch (DustException e) {
				e.printStackTrace();
				result = e.getMessage();
			} catch (ParseException e1){
				e1.printStackTrace();
				result = e1.getMessage();
			}
		}
				
		return result;
	}
	
	// ------------------------- syntax added chinese -------------------------------
	@ResponseBody
	@RequestMapping(value = "/eval_ch",method = RequestMethod.POST)
	public String evalch(@RequestBody Map<String,Object> payload){
		
		logger.info("\nenter evalch");
		
		String result="";
		
		if (null == payload || payload.size() == 0){
			result = "no json";
		} else {
			try {
				String code = payload.get("code").toString();
				if (null == code || code.trim().length() == 0){
					result = "no code in the json";
				} else {
					logger.info("\nprogram code ---- \n"+code);
					ArrayList<String> rets = new ArrayList<>();
					chnEvaluator.run(code, rets);
					for (String s : rets){
						result += s + "<br>";
					}
				}
			} catch (DustException e) {
				e.printStackTrace();
				result = e.getMessage();
			} catch (ParseException e1){
				e1.printStackTrace();
				result = e1.getMessage();
			}
		}
				
		return result;
	}
}
