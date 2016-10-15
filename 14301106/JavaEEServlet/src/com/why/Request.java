package com.why;

import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Request implements ServletRequest {

	private InputStream input;
	private String uri;
	private Map<String, String> paramters = new HashMap<String, String>();
	private StringBuffer requestContentBuffer = new StringBuffer(2048);
	public Request(InputStream input) {
		this.input = input;
		prepareBuffer();
		parseString();
	}

	public String getUri() {
		return uri;
	}

	/**
	 * 
	 * requestString形式如下： GET /index.html HTTP/1.1 Host: localhost:8080
	 * Connection: keep-alive Cache-Control: max-age=0 ...
	 * 该函数目的就是为了获取/index.html字符串
	 */
	public void prepareBuffer(){
		byte[] buffer = new byte[2048];
		int i = -1;
		try {
			i = this.input.read(buffer);
			
			for(int k = 0; k < i ; ++k){
				requestContentBuffer.append((char)buffer[k]);
			}
			
			System.out.println(requestContentBuffer.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseString(){
		String[] lines = requestContentBuffer.toString().split("\r\n");
		//获取第一行内容
		String[] firstLineEle = lines[0].split(" ");
		
		String[] urlInfo = firstLineEle[1].split("\\?");
		if(urlInfo!=null){
			this.uri = urlInfo[0];
			String remainer = urlInfo[1];
			String[] pairs = remainer.split("&");
			for(int i = 0; i < pairs.length; ++i){
				String[] keys = pairs[i].split("=");
				paramters.put(keys[0], keys[1]);
			}
		}else{
			return;
		}
	}
	
	

	/* implementation of the ServletRequest */
	public Object getAttribute(String attribute) {
		return null;
	}

	public Enumeration<String> getAttributeNames() {
		return null;
	}

	public String getRealPath(String path) {
		return null;
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return null;
	}

	public boolean isSecure() {
		return false;
	}

	public String getCharacterEncoding() {
		return null;
	}

	public int getContentLength() {
		return 0;
	}

	public String getContentType() {
		return null;
	}

	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	public Locale getLocale() {
		return null;
	}

	public Enumeration<Locale> getLocales() {
		return null;
	}

	public String getParameter(String name) {
		if(this.paramters.get(name) != null){
			return this.paramters.get(name);
		}else
			return null;
	}

	public Map<String, String[]> getParameterMap() {
		return null;
	}

	public Enumeration<String> getParameterNames() {
		return null;
	}

	public String[] getParameterValues(String parameter) {
		return null;
	}

	public String getProtocol() {
		return null;
	}

	public BufferedReader getReader() throws IOException {
		return null;
	}

	public String getRemoteAddr() {
		return null;
	}

	public String getRemoteHost() {
		return null;
	}

	public String getScheme() {
		return null;
	}

	public String getServerName() {
		return null;
	}

	public int getServerPort() {
		return 0;
	}

	public void removeAttribute(String attribute) {
	}

	public void setAttribute(String key, Object value) {
	}

	public void setCharacterEncoding(String encoding)
			throws UnsupportedEncodingException {
	}

	@Override
	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AsyncContext startAsync() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}
