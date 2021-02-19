package com.jrj.stroll.complier.dust.parser;

import java.util.HashMap;

/**
 * 运算符表
 * @author chenn
 *
 */
public class Operators {

	public static int LEFT = 1;
	public static int RIGHT = 2;

	private HashMap<String, Operator> opMap = new HashMap<>();

	public void add(String op, int priority, int direction){
		opMap.put(op, new Operator(priority,direction));
	}
	
	public Operator getOp(String op){
		return opMap.get(op);
	}
	
	public int getPriority(String op){
		return opMap.get(op).getPriority();
	}
	
	public int getDirection(String op){
		return opMap.get(op).getDirection();
	}
	
	private class Operator{
		/**
		 * 优先级
		 */
		private int priority;
				
		/**
		 * 运算符结合方向
		 */
		private int direction;		
		
		public Operator(int priority, int direction){
			this.priority = priority;
			this.direction = direction;
		}

		public int getPriority() {
			return priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public int getDirection() {
			return direction;
		}

		public void setDirection(int direction) {
			this.direction = direction;
		}
		
	}
}
