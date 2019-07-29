package HttpServer.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.*;

/**
 * 封装请求协议: 获取 method uri以及请求参数
 *  封装请求协议: 封装请求参数为Map
 * @author 王少成
 */

public class Request {
    private String requestInfo;
    private String method;
    private String url;
    private String queryStr;
    private final String CRLF="\r\n";
    private Map<String, List<String>> parameterMap;
    public Request(Socket client)throws IOException{
        this(client.getInputStream());
    }
    public Request(InputStream is){
        parameterMap=new HashMap<>();
        byte[] datas=new byte[1024*1024];
        int len;
        try{
            len=is.read(datas);
            this.requestInfo=new String(datas,0,len);
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        parseRequestInfo();

    }

    private void parseRequestInfo(){
        System.out.println("------分解-------");
        System.out.println("---1、获取请求方式: 开头到第一个/------");
       // POST /aaaa?name=1 HTTP/1.1
        this.method=this.requestInfo.substring(0,this.requestInfo.indexOf("/")).toLowerCase();
        this.method=this.method.trim();//去掉空格
        System.out.println("---2、获取请求url: 第一个/ 到 HTTP/------");
        System.out.println("---可能包含请求参数? 前面的为url------");
        //1)、获取/的位置
        int startIndex=this.requestInfo.indexOf("/")+1;
        //2)、获取 HTTP/的位置
        int endIndex=this.requestInfo.indexOf("HTTP/");
        //3)、分割字符串 左闭右开
        this.url=this.requestInfo.substring(startIndex,endIndex).trim();
        //4)、获取？的位置
        int queryIndex=this.requestInfo.indexOf("?");
        if(queryIndex>=0){
            String[] urlArray=this.url.split("\\?");
            this.url=urlArray[0];
            queryStr=urlArray[1];
        }
        System.out.println(this.url);
        //Pragma: no-cache
        //Cache-Control: no-cache
        //
        //name=wangshaocheng
        System.out.println("---3、获取请求参数:如果Get已经获取,如果是post可能在请求体中------");
        if(method.equals("post")){
            String str=this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            System.out.println(str);
            if(null==queryStr){
                queryStr=str;
            }else{
                queryStr+="&"+str;
            }
        }
        queryStr=queryStr==null?"":queryStr;
        System.out.println(method+"-->"+url+"--->"+queryStr);

        convertMap();
    }

    //将请求参数转换为map
    private void convertMap(){
        String[] keyValues=this.queryStr.split("&");
        for(String  queryStr:keyValues){
            String[] kv=queryStr.split("=");
            //防止出现 =号没有值的情况
            kv= Arrays.copyOf(kv,2);

            String key=kv[0];
            String value=kv[1];
            if(!parameterMap.containsKey(key)){
                parameterMap.put(key,new ArrayList<>());
            }
            parameterMap.get(key).add(value);

        }
    }
    /**
     * 通过name获取对应的多个值
     * @param key
     * @return
     */
    public String[] getParameters(String key){
        List<String> values=parameterMap.get(key);
        if(null==values || values.size()==0){
            return null;
        }
        return values.toArray(new String[0]);
    }
    /**
     * 通过name获取对应的一个值
     * @param key
     * @return
     */
    public String getParameter(String key){
        String []  values =getParameters(key);
        return values ==null?null:values[0];
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryStr() {
        return queryStr;
    }
    /**
     * 处理中文
     * @return
     */
    private String decode(String value,String enc) {
        try {
            return java.net.URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
