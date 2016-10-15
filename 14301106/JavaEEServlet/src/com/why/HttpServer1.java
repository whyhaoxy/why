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

	// �رշ�������
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

	public static void main(String[] args) {
		HttpServer1 server = new HttpServer1();
		// �ȴ���������
		server.await();
	}

	public void await() {
		ServerSocket serverSocket = null;
		int port = 3333;
		try {
			// �������׽��ֶ���
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// ѭ���ȴ�����
		while (true) {
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try {
				// �ȴ����ӣ����ӳɹ��󣬷���һ��Socket����
				socket = serverSocket.accept();
				System.out.println("��������ͻ���һ���µ�TCP���ӣ��ÿͻ��ĵ�ַΪ��" + socket.getInetAddress() + ": " + socket.getPort());
				
				input = socket.getInputStream();
				output = socket.getOutputStream();

				// ����Request���󲢽���
				Request request = new Request(input);
				//request.parse();
				// ����Ƿ��ǹرշ�������
				if (request.getUri().equals(SHUTDOWN_COMMAND)) {
					break;
				}

				// ���� Response ����
				Response response = new Response(output);
				response.setRequest(request);

				ServletProcessor1 processor = new ServletProcessor1();
				processor.process(request, response);

				// �ر� socket
				socket.close();

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
