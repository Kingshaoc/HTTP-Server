package HttpServer.Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

    public static void main(String[] args) {
        Server server=new Server();
        server.start();
    }
    private ServerSocket serverSocket;
    public void start(){
        try{
            serverSocket=new ServerSocket(8888);
            receive();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    public void receive(){
        try
        {
            //接受连接处理
            Socket client=serverSocket.accept();
            System.out.println("一个客户端剪了了连接");
            InputStream is=client.getInputStream();
            byte[] data=new byte[1024*1024];
            int len=is.read(data);
            String requestInfo=new String(data,0,len);
            System.out.println(requestInfo);

            //返回
            StringBuilder content=new StringBuilder();
            content.append("<html>");
            content.append("<head>");
            content.append("<title>");
            content.append("服务器响应成功");
            content.append("</title>");
            content.append("</head>");
            content.append("<body>");
            content.append("server终于回来了。。。。");
            content.append("</body>");
            content.append("</html");
            int size=content.toString().getBytes().length;//必须获取字节数组的长度
            StringBuilder responseInfo=new StringBuilder();
            String blank =" ";
            String CRLF="\r\n";
            //1、响应行: HTTP/1.1 200 OK
            responseInfo.append("HTTP/1.1").append(blank).append(200).append(blank).append("OK").append(CRLF);
            //2、响应头(最后一行存在空行):
			/*
			 Date:Mon,31Dec209904:25:57GMT
			Server:shsxt Server/0.0.1;charset=GBK
			Content-type:text/html
			Content-length:39725426
			 */
			responseInfo.append("Date:").append(new Date()).append(CRLF);
			responseInfo.append("Server:").append("shsxt Server/0.0.1;charset=GBK").append(CRLF);
			responseInfo.append("Content-type:text/html").append(CRLF);
			responseInfo.append("Content-length:").append(size).append(CRLF);
			responseInfo.append(CRLF);
			//3正文
            responseInfo.append(content.toString());
            //4.写出到客户端
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            bw.write(responseInfo.toString());
            bw.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("客户端错误");
        }

    }

    public void stop(){

    }

}
