package HttpServer.Server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;


/**
 * 多线程处理加入分发器
 */
public class Server01 {
    public static void main(String[] args) {
        Server01 server = new Server01();
        server.start();
    }

    private ServerSocket serverSocket;
    private boolean isRunning=true;
    public void start() {
        try {
            serverSocket = new ServerSocket(8888);
            receive();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    public void receive() {
        while(isRunning){
            try {
                //接受连接处理
                Socket client = serverSocket.accept();
                System.out.println("一个客户端建立了连接");

                new Thread(new Dispatcher(client)).start();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("客户端错误");
            }
        }


    }

    public void stop() {
        isRunning=false;
        try{
            this.serverSocket.close();
            System.out.println("服务器已停止");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
