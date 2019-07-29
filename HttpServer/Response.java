package HttpServer.Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
    private BufferedWriter bw;
    private StringBuilder headInfo;
    private StringBuilder content;
    private int len;

    private final String Blank=" ";
    private final String CRLF="\r\n";

    public Response () {
        content = new StringBuilder();
        headInfo = new StringBuilder();
        len = 0;
    }
    public Response(Socket client){
        this();
        try{
            bw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        }catch (Exception e){
            e.printStackTrace();
            headInfo=null;
        }
    }
    public Response(OutputStream os){
        this();
        bw=new BufferedWriter(new OutputStreamWriter(os));
    }

    //构建头信息 根据状态码
    private void createHeadInfo(int code){
        //1、响应行: HTTP/1.1 200 OK
        headInfo.append("HTTP/1.1").append(Blank);
        headInfo.append(code).append(Blank);
        switch (code){
            case 200:
                headInfo.append("OK").append(CRLF);
                break;
            case 404:
                headInfo.append("NOT FOUND").append(CRLF);
                break;
            case 505:
                headInfo.append("Server ERROR").append(CRLF);
                break;
        }
        //2、响应头(最后一行存在空行):
			/*
			 Date:Mon,31Dec209904:25:57GMT
			Server:shsxt Server/0.0.1;charset=GBK
			Content-type:text/html
			Content-length:39725426
			 */
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Server:").append("shsxt Server/0.0.1;charset=GBK").append(CRLF);
        headInfo.append("Content-type:text/html").append(CRLF);
        headInfo.append("Content-length:").append(len).append(CRLF);
        headInfo.append(CRLF);
    }
    //动态添加响应内容
    public Response print(String info){
        content.append(info);
        len+=info.getBytes().length;
        return this;
    }
    public Response println(String info){
        content.append(info).append(CRLF);
        len+=(info+CRLF).getBytes().length;
        return this;
    }

    //推送回响应信息给浏览器
    public void pushToBrower(int code)throws IOException{
        if(null==headInfo){
            code=505;
        }
        createHeadInfo(code);
        bw.append(headInfo);
        bw.append(content);
        bw.flush();
    }

}
