package cn.ymotel.dpress.admin.menu;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/menus/tree_view",methods = RequestMethod.GET)
public class MenuListActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public List Execute(ServletMessage message) throws Throwable {
        Map map=message.getContext();
        map.put("siteid", Utils.getSiteId());

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
