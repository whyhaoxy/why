#coding=utf-8
import socket
import StringIO
import sys
import datetime
import os
import codecs

class wsgiTest(object):
    address_family = socket.AF_INET
    socket_type = socket.SOCK_STREAM
    request_queue_size = 1

    def __init__(self, server_address):
        # 创建socket
        self.listen_socket = listen_socket = socket.socket(self.address_family,self.socket_type)
        listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        listen_socket.bind(server_address)
        listen_socket.listen(self.request_queue_size)
        host, port = self.listen_socket.getsockname()[0:2]
        self.server_name = socket.getfqdn(host)
        self.server_port = port
        self.headers_set = []  # 返回信息头部

    def set_app(self, application):
        self.application = application

    #持续运行server
    def serve_forever(self):
        listen_socket = self.listen_socket
        while True:
            self.client_connection, client_address = listen_socket.accept()
            # 处理一个请求和关闭客户端连接。然后循环结束等待另一个客户端连接
            self.handle_one_request()

    #处理请求
    def handle_one_request(self):
        self.request_data = request_data = self.client_connection.recv(1024)
        # 打印
        # print(''.join( '< {line} \n'.format(line=line)
        #               for line in request_data.splitlines()))
        self.parse_request(request_data)
        env = self.get_environ() # 构建环境使用要求的数据字典

        # 应用程序调用和返回一个结果，将成为HTTP响应体
        #print ('self.application: %s' %self.application)
        result = self.application(env, self.start_response)
        self.finish_response(result) # 发送给客户端

    def parse_request(self, text):   #解析请求
        request_line = text.splitlines()[0]
        request_line = request_line.rstrip('\r\n')
        #分解请求
        (self.request_method,  # GET
         self.path,  # /hello
         self.request_version  # HTTP/1.1
         ) = request_line.split()

    def get_environ(self):
        env = {}
        env['wsgi.version'] = (1, 0)
        env['wsgi.url_scheme'] = 'http'
        env['wsgi.input'] = StringIO.StringIO(self.request_data)
        env['wsgi.errors'] = sys.stderr
        env['wsgi.multithread'] = False
        env['wsgi.multiprocess'] = False
        env['wsgi.run_once'] = False
        # CGI变量
        env['REQUEST_METHOD'] = self.request_method  # GET
        env['PATH_INFO'] = self.path  # /hello
        env['SERVER_NAME'] = self.server_name  # localhost
        env['SERVER_PORT'] = str(self.server_port)  # 8888
        return env

    def start_response(self, status, response_headers, exc_onfo=None):
        server_headers = [
            ('Date', datetime.datetime.now().strftime('%a, %d %b %Y %H:%M:%S GMT')),
            ('Server', 'WSGIServer 02'),
        ]
        self.headers_set = [status, response_headers + server_headers]

    def finish_response(self, result):
        try:
            status, response_headers = self.headers_set
            response = 'HTTP/1.1 {status}\r\n'.format(status=status)
            for header in response_headers:
                response += '{0}: {1}\r\n'.format(*header)
            response += '\r\n'
            for data in result:
                response += data
             #打印
            #print(''.join( '> {line}\n'.format(line=line)
            #    for line in response.splitlines()
            #))
            self.client_connection.sendall(response)
        finally:
            self.client_connection.close()


SERVER_ADDRESS = (HOST, PORT) = '', 8888

def make_server(server_address, application):
    server = wsgiTest(server_address)
    server.set_app(application)
    return server


if __name__ == '__main__':

    from wsgiTest import application

    # 创建一个服务器，IP地址为空，端口是8000，处理函数是application:
    #httpd = make_server('', 8000, application)
    httpd = make_server(SERVER_ADDRESS, application)
    print ("Serving HTTP on port 8888...")
    # 开始监听HTTP请求:
    httpd.serve_forever()

def application(environ, start_response):
    """A barebones WSGI application.

    This is a starting point for your own Web framework :)
    """
    file_path = environ['PATH_INFO'].split('/')[1]  # 得到文件名

    if file_path.endswith('.html'):

        if os.path.exists(file_path):
            status = '200 OK'
            response_headers = [('Content-Type', 'text/html')]
            f = codecs.open(file_path, "r", "UTF-8")
            content = f.read()
            f.close()
        else:
            status = '200 OK'
            response_headers = [('Content-Type', 'text/plain')]
            content = '404 Not Found '
    else:
        status = '200 OK'
        content = '<h1>Hello!' + file_path + '</h1>'
        response_headers = [('Content-Type', 'text/html')]

    start_response(status, response_headers)
    return content