package com.shengxian.common;


import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * 消息对象 单例模式
 * @author SHZ
 *
 */
public class Message extends HashMap<String, Object>{



	String keyCode = "code",keyMessage = "info",keyData = "data";


	public Message getResponseBody(){
		return  this;
	}

	/**
	 * 定义无参私有构造方法
	 */
	public Message(){};

	/**
	 * 定义有参私有构造方法
	 * @param code
	 */
	public Message(int code) {
		this(code,"");
	}

	/**
	 * 静态内部类
	 */
	private static class messsageInstance{
		private static final Message INSTANCE = new Message(codeNull);

	}

	/**
	 * 创建空状态消息
	 * @return
	 */
	public static Message non(){
		return new Message(codeNull);
	}

	/**
	 * 创建成功消息
	 * @return
	 */
	public static Message successed(){
		return new Message(codeSuccessed);
	}

	/**
	 * 创建消息
	 * @return
	 */
	public static Message failured(){
		return new Message(codeFailured);
	}

	/**
	 * @param code 状态码
	 * @param info 消息内容
	 */
	public Message(int code, String info) {
		put(keyCode, code);
		put(keyMessage, info);
	}


	public int getCode() {
		return (Integer)get(keyCode);
	}
	public Message code(int code) {
		put(keyCode, code);
		return this;
	}


	public String getMessage() {
		return (String)get(keyMessage);
	}

	public Message message(String info) {
		put(keyMessage, info);
		return this;
	}


	public Message data(Object data){
		put(keyData, data);
		return this;
	}

	public Object getData() {
		return this.get(keyData);
	}




	/**
	 *
	 */
	public  static final long serialVersionUID = 1L;
	/**
	 * 成功码
	 */
	public static final int codeSuccessed = 1;
	/**
	 * 退出登录
	 */
	public static final int codeOutOfLogin = 2;
	/**
	 * 失败码
	 */
	public static final int codeFailured = -1;

	/**
	 * 未知状态
	 */
	private static final int codeNull = 0;

	public static void main(String[] args) {

		String i= "2";
		if (!i.equals("2") && !i.equals("3")){
			System.out.println("dd");
		}


	}
	
}
