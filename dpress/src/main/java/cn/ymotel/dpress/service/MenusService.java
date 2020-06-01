package cn.ymotel.dpress.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MenusService {
    @Autowired
    private SqlSession sqlSession;

    public List listTeams(Object siteid){
        Map map=new HashMap<>();
        map.put("siteid",siteid);
      List list=  sqlSession.selectList("menus.qallorderpriority",map);
        Map rtnMap=new HashMap();
        List rtnList=new ArrayList();
      for(int i=0;i<list.size();i++){
        Map tMap=(Map)list.get(i);
        Object team=tMap.get("team");
          List tList=null;
        if(rtnMap.containsKey(team)){
              tList=(List)rtnMap.get(team);
        }else{
             tList=new ArrayList();
             rtnMap.put(team,tList);
        }
        tList.add(tMap);
      }
      for(java.util.Iterator iter=rtnMap.entrySet().iterator();iter.hasNext();){
        Map.Entry entry=(Map.Entry)iter.next();
        Map kMap=new HashMap();
        kMap.put("team",entry.getKey());
          kMap.put("menus",entry.getValue());
        rtnList.add(kMap);
      }
    return  rtnList;

    }
    public List treebyteam(Object siteid,Object team){
        Map map=new HashMap();
        map.put("siteid",siteid);
        List ls=sqlSession.selectList("menus.qbyteam",map);
        List treeList= buildTree(ls,"id","parentId",0);
        return treeList;
    }
    public List listAsTree(Object siteid){
        Map map=new HashMap();
        map.put("siteid",siteid);
        List ls=sqlSession.selectList("menus.qall1",map);
        List treeList= buildTree(ls,"id","parentId",0);
        return treeList;
    }
    public List buildTree(List<Map> list, String idKey, String parentIdKey, Object parentId){
        List rtnList=new ArrayList();
        for(Map map:list){
            Object tparentId=map.get(parentIdKey);
            Object id=map.get(idKey);
            if(tparentId==null){
                continue;
            }
            if(tparentId.toString().equals(parentId.toString())){
                rtnList.add(map);
            }else{
                continue;
            }
            List list2=buildTree(list,idKey,parentIdKey,id);
            if(list2==null||list2.size()==0){
                map.put("children", null);

            }else {
                map.put("children", list2);
            }
        }
        return rtnList;
    }
}
