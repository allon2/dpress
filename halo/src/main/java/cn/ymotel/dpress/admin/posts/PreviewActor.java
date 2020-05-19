package cn.ymotel.dpress.admin.posts;

import cn.hutool.core.lang.UUID;
import cn.ymotel.dactor.Constants;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.response.ResponseViewType;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/posts/preview/{id}",methods = RequestMethod.GET)
public class PreviewActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public String Execute(ServletMessage message) throws Throwable {
        Map params=message.getContext();
        params.put("siteid",Utils.getSiteId());
        Map rtnMap=sqlSession.selectOne("posts.q",params);
        String fullpath=(String)rtnMap.get("slug");
        String token=UUID.randomUUID().toString(true);
        fullpath="/archives/"+fullpath+"?token="+token;
        String baseurl=Utils.getBaseUrl(message);
        Map tokenMap=new HashMap();
        tokenMap.put(token, message.getRequest().getSession().getAttribute(Utils.ADMIN_SITEID));
        message.getRequest().getSession().setAttribute("_Preiview_Token",tokenMap);
        return baseurl+fullpath;
    }
}
