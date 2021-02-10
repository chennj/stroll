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
import com.jrj.stroll.complier.dust.exception.ParseException;
import com.jrj.stroll.complier.dust.util.LexerRunner;

@Controller
@RequestMapping("lexical")
public class LexerController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LexerRunner lexerRunner;
	
	@RequestMapping("/code-dialog")
	public String codeDialog(HttpServletRequest request, HttpServletResponse response) {

		return "lexical/code_dialog";
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
					String msg = lexerRunner.run(payload.get("code").toString());
					result.addProperty("msg", msg);
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
