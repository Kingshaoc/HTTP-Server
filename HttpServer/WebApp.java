package HttpServer.Server;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WebApp {
    private static WebContext webContext;
    static {
        try{
            //sax解析
            //1.获取解析工厂
            SAXParserFactory factory=SAXParserFactory.newInstance();
            //2.从解析工厂中获取解析器
            SAXParser parse=factory.newSAXParser();
            //3.编写处理器
            //4.加载文档Document注册处理器
            WebHandler handler=new WebHandler();
            parse.parse(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream("HttpServer/Server/web.xml")
                    ,handler);
            webContext = new WebContext(handler.getEntities(),handler.getMappings());


        }catch (Exception e){
            System.out.println("解析配置文件错误");
        }
    }
    /**
    * 通过url获取配置文件对应的servlet
    * @param url
     * @return
    */
    public static Servlet getServletFromUrl(String url){
        String className=webContext.getClz("/"+url);
        Class clz;
        try{
            System.out.println(url+"-->"+className+"-->");
            clz=Class.forName(className);
            Servlet servlet=(Servlet)clz.getConstructor().newInstance();
            return servlet;
        }catch (Exception e){

        }
        return null;

    }
}
