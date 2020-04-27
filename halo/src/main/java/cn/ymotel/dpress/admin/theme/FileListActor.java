package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import run.halo.app.model.support.ThemeFile;

import java.util.*;

@ActorCfg(urlPatterns = "/api/admin/themes/activation/files.json")
public class FileListActor implements Actor<ServletMessage> {
    private static String[] CAN_EDIT_SUFFIX = {".ftl", ".css", ".js", ".yaml", ".yml", ".properties"};
    @Autowired
    private SqlSession sqlSession;
    public String getActiveTheme(ServletMessage message){

        Map tMap = new HashMap();
        tMap.put("siteid", Utils.getSiteIdFromMessage(message));
        Map themeMap = sqlSession.selectOne("options.qactivetheme", tMap);
        String theme = (String) themeMap.get("option_value");
        return theme;

    }
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Map ttmap=new HashMap();
        ttmap.put("siteid", Utils.getSiteIdFromMessage(message));
        ttmap.put("theme",getActiveTheme(message));
        List ls=sqlSession.selectList("dpress.qalltemplate",ttmap);
        ThemeFile root=new ThemeFile();
        for(int i=0;i<ls.size();i++){
            Map tMap=(Map)ls.get(i);

            FillNodes((String)tMap.get("path"),"",root);

        }
        Map rtnMap=new HashMap();
        rtnMap.put("data",root.getNode());
        message.getResponse().setContentType("");
        System.out.println(root);
        return rtnMap;
    }



    public static String nextPath(String path,String startPath){
        if(startPath.equalsIgnoreCase("")){
            return path.split("/")[0];
        }
        if(path.equalsIgnoreCase(startPath)){
            return path;
        }
        int i=path.indexOf(startPath);

        if(i<0){
            System.out.println("ttt");
        }
        i=i+startPath.length()+1;
        return startPath+"/"+path.substring(i).split("/")[0];
    }
    public static boolean canDeep(String path,String startPath){
        if(path.equalsIgnoreCase(startPath)){
            return false;
        }
        if(path.startsWith(startPath)){
            return true;
        }
        return false;
    }
    public static FileNode findFileNode(String path,String startPath,FileNode node){
         startPath= nextPath(path,startPath);
         if(node.getPath().equalsIgnoreCase(startPath)){
             return node;
         }
        List<FileNode> nodes=node.getChilds();
        for(int i=0;i<nodes.size();i++){
            FileNode tnode=nodes.get(i);
            if(tnode.getPath().equalsIgnoreCase(path)){
                return tnode;
            }
            if(canDeep(tnode.getPath(),startPath)){
                return findFileNode(path,startPath,tnode);
            }
        }
        return null;
    }

    /**
     *
     * @param path
     * @param startPath
     * @param fileNode
     * @return 返回，是否继续，true表示已经找到，不继续，false表示继续
     */
    public static  boolean  FillNodes(String path,String startPath,ThemeFile fileNode){
        if(startPath.equalsIgnoreCase(path)){
            return true;
        }
        startPath= nextPath(path,startPath);





            boolean isfind=false;
            if(fileNode.getNode()!=null) {
                for (int i = 0; i < fileNode.getNode().size(); i++) {
                    ThemeFile node = fileNode.getNode().get(i);
                    if (startPath.startsWith(node.getPath())) {
                        boolean b = FillNodes(path, startPath, node);
                        if (b) {
                            isfind = true;
                            return true;
                        }
                    }
                }
            }

        if(!isfind){
            if(fileNode.getNode()==null){
                fileNode.setNode(new ArrayList<>());
            }

            ThemeFile f=new ThemeFile();
            f.setPath(startPath);
            f.setEditable(isEditable(startPath));
            f.setName(getFileName(startPath));
            f.setIsFile(true);
            fileNode.getNode().add(f);
            fileNode.setIsFile(false);
            fileNode.setEditable(false);



            FillNodes(path, startPath, f);

            return true;
        }
        return true;
    }


    public static String getFileName(String path){
        int index=path.lastIndexOf("/");
        if(index>=0){
            return   path.substring(index+1);
        }
        return path;
    }
    public static boolean isEditable(String path){
        // Check suffix
        for (String suffix : CAN_EDIT_SUFFIX) {
            if (path.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args){
//        Map tMap=new HashMap();
//        tMap.put("path","source/css/blog_basic.css");
//        List ls=new ArrayList();
//        ls.add(tMap);
//
//        Map rootMap=new HashMap();
//        findMap("a/css/blog_basic.css","",rootMap);
//
//        findMap("source/css/blog_basic.css","",rootMap);
//       findMap("source/css/blog_basicq.css","",rootMap);
//        Map tMap= findMap("source/css","",rootMap);
//        System.out.println(rootMap);
//        System.out.println(tMap);
        ThemeFile root=new ThemeFile();
        root.setPath("");
        FillNodes("source/css/blog_basic.css","",root);
        FillNodes("source/css/blog_basic1.css","",root);
        FillNodes("ae/b/blog_basic1.css","",root);
        FillNodes("index.html","",root);
        FillNodes("module/macro.ftl","",root);
        System.out.println(JSON.toJSON(root.getNode()));

    }
}
