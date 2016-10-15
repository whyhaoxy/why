package com.why;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

//import ex02.pyrmont.Request;
//import ex02.pyrmont.Response;
//import ex02.pyrmont.StaticResourceProcessor;

public class HttpServer1 {

	// 关闭服务命令
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

	public static void main(String[] args) {
		HttpServer1 server = new HttpServer1();
		// 等待连接请求
		server.await();
	}

	public void await() {
		ServerSocket serverSocket = null;
		int port = 3333;
		try {
			// 服务器套接字对象
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// 循环等待请求
		while (true) {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				// 等待连接，连接成功后，返回一个Socket对象
				socket = serverSocket.accept();
				System.out.println("建立了与客户的一个新的TCP连接，该客户的地址为：" + socket.getInetAddress() + ": " + socket.getPort());
				
				input = socket.getInputStream();
				output = socket.getOutputStream();

				// 创建Request对象并解析
				Request request = new Request(input);
				//request.parse();
				// 检查是否是关闭服务命令
				if (request.getUri().equals(SHUTDOWN_COMMAND)) {
					break;
				}

				// 创建 Response 对象
				Response response = new Response(output);
				response.setRequest(request);

				ServletProcessor1 processor = new ServletProcessor1();
				processor.process(request, response);

				// 关闭 socket
				socket.close();

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
