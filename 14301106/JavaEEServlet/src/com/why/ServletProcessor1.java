package com.why;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.why.Constants;
import com.why.Request;
import com.why.Response;

public class ServletProcessor1 {

	public void process(Request request, Response response) throws Exception {
		//得到url   /login
		String uri = request.getUri();
		//String servletName = uri.substring(uri.lastIndexOf("/") + 1);
		System.out.println("url="+uri);
		// 解析 web.xml , 根据uri得到servlet路径
		String servletName = getServerName(uri);

		System.out.println("Processing servlet: " + servletName);
//		
//		// 加载servlet类
//		Servlet servlet = loadServlet(servletName);
//		// 将request和response交给Servlet处理
//		callService(servlet, req, res);
//		
//		
		
		String servletURL = "../" + servletName.replace('.', '/');
		
		File file = new File(servletURL);
		//URL url = new URL("file://Servlet/LoginServlet");
		URL url = file.toURL();
		URLClassLoader loader = new URLClassLoader(
				new URL[] { url }, Thread.currentThread().getContextClassLoader());
		Servlet servlet = null;
		
		try {
			@SuppressWarnings("unchecked")
			Class<Servlet> servletClass = (Class<Servlet>) loader
					.loadClass(servletName);
			servlet = (Servlet) servletClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	

		try {
			
			// 执行ervlet的service方法
			servlet.service((ServletRequest) request,
					(ServletResponse) response);
		} catch (Exception e) {
			System.out.println(e.toString());
		} catch (Throwable e) {
			System.out.println(e.toString());
		}

	}

	private static String getServerName(String uri)
			throws Exception{
			String servletName = null;
			
			//parse web.xml
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("web.xml"));
			//get root element ---<web-app></web-app>
			Element node = doc.getRootElement();
			System.out.println(node.getName());
			//List<Attribute> list = node.attributes();
			String servlet = null;
			Iterator<Element> it = node.elementIterator("servlet-mapping");
			
			while(it.hasNext()){
				Element e = it.next();
				Iterator<Element> ite = e.elementIterator("url-pattern");
				Iterator<Element> ite1 = e.elementIterator("servlet-name");
				if(ite.hasNext()){
					Element el = ite.next();
					if(el.getText().equals(uri)){
						servlet = ite1.next().getText();
						break;
					}
				}
			}
			
			Iterator<Element> it1 = node.elementIterator("servlet");
			
			while(it1.hasNext()){
				Element e = it1.next();
				Iterator<Element> iter = e.elementIterator("servlet-name");
				Iterator<Element> iter1 = e.elementIterator("servlet-class");
				
				if(iter.hasNext()&&iter.next().getText().equals(servlet)){
					servletName = iter1.next().getText();
					System.out.println(servletName);
					break;
				}
				
			}
			return servletName;
		}
		
}
