package HttpServer.Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Dispatcher implements Runnable {
    private Socket client;
    private Response response;
    private Request request;
    public Dispatcher(Socket client){
        this.client=client;
        try{
            request=new Request(client);
            response=new Response(client);
        }catch (IOException e){
            e.printStackTrace();
            release();
        }

    }
    @Override
    public void run() {
        try{
            if(null==request.getUrl()||request.getUrl().equals("")){
                InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("HttpServer/Server/index.html");
                response.print(new String(is.readAllBytes()));
                response.pushToBrower(200);
                is.close();
                return;
            }
            Servlet servlet =WebApp.getServletFromUrl(request.getUrl());
            if(null!=servlet) {
                servlet.service(request,response);
                //关注了状态码
                response.pushToBrower(200);
            }else {
                //错误....
                //错误....
                InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("HttpServer/Server/error.html");
                response.print((new String(is.readAllBytes())));
                response.pushToBrower(404);
                is.close();
            }
        }catch (IOException e){
            try {
                response.println("你好我不好，我会马上好");

                response.pushToBrower(500);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        release();

    }
    //释放资源
    private void release() {
        try {
            client.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
