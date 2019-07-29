package HttpServer.Server;

import HttpServer.servlet.Entity;
import HttpServer.servlet.Mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebContext {
    private List<Entity> entities=null;
    private List<Mapping> mappings=null;

    //key-->servlet-name  value -->servlet-class
    private Map<String,String> entityMap=new HashMap<>();
    //key-->url-pattern  value -->servlet-class
    private Map<String,String> mappingMap=new HashMap<>();

    public WebContext(List<Entity> entities, List<Mapping> mappings){
        this.entities=entities;
        this.mappings=mappings;

        //将entity 的List转成了对应map
        for(Entity entity:entities){
            entityMap.put(entity.getName(),entity.getClz());
        }
        //将map 的List转成了对应map
        for(Mapping mapping:mappings){
            for(String pattern:mapping.getPatterns()){
                mappingMap.put(pattern,mapping.getName());
            }
        }

    }

    /**
     * 通过URL的路径找到了对应class
     * @param pattern
     * @return
     */

    public String getClz(String pattern){
        String name=mappingMap.get(pattern);
        return entityMap.get(name);
    }


}
