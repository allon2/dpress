package cn.ymotel.dpress.admin.login;

import cn.hutool.crypto.digest.BCrypt;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.AutokenService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.cache.AbstractStringCacheStore;
import run.halo.app.event.logger.LogEvent;
import run.halo.app.exception.BadRequestException;
import run.halo.app.model.entity.User;
import run.halo.app.model.enums.LogType;
import run.halo.app.security.authentication.Authentication;
import run.halo.app.security.context.SecurityContextHolder;
import run.halo.app.security.token.AuthToken;
import run.halo.app.security.util.SecurityUtils;
import run.halo.app.utils.HaloUtils;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ActorCfg(urlPatterns = "/api/admin/login",methods = RequestMethod.POST)
public class LoginActor implements Actor<ServletMessage> {
    @Autowired
    AutokenService autokenService;
    @Autowired
    private SqlSession sqlSession;

    @Override
    public Map Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap();
        map.put("username",message.getContextData("username"));
       Map userMap= sqlSession.selectOne("users.q",map);
       if(userMap==null||userMap.isEmpty()){
           throw new BadRequestException("用户名或者密码不正确");
       }
        String usingpassword=(String)userMap.get("password");
       String password=message.getContextData("password");
        if(!passwordMatch(usingpassword,password)){
            throw new BadRequestException("用户名或者密码不正确");
        }
//        clearToken();
        User user=new User();
        user.setId((Integer)userMap.get("id"));
        user.setAvatar((String)userMap.get("avatar"));
        user.setDescription((String)userMap.get("description"));
        user.setEmail((String)userMap.get("email"));
        user.setExpireTime((java.util.Date)userMap.get("expire_time"));
        user.setNickname((String)userMap.get("nickname"));
        user.setUsername(message.getContextData("username"));
        user.setCreateTime((java.util.Date)userMap.get("create_time"));
        user.setUpdateTime((java.util.Date)userMap.get("update_time"));
        user.setPassword((String)userMap.get("password"));
        message.getRequest().getSession().invalidate();
        message.setUser(user);
        message.getRequest().getSession().setAttribute(Utils.ADMIN_SITEID,getSiteId());

        AuthToken token= autokenService.buildAuthToken(user);
        Map rtnMap=new HashMap();
        rtnMap.put("access_token",token.getAccessToken());
        rtnMap.put("expired_in",token.getExpiredIn());
        rtnMap.put("refresh_token",token.getRefreshToken());
        return rtnMap;
    }

    public Object getSiteId(){
        List ls=sqlSession.selectList("dpress.qallsite");

        for(int i=0;i<ls.size();i++){
            Map map=(Map)ls.get(i);
            map.put("parentId",0);
            map.put("children",null);
        }
        return ((Map)ls.get(0)).get("id");
    }
    public boolean passwordMatch(String password, String plainPassword) {

        return !StringUtils.isBlank(plainPassword) && BCrypt.checkpw(plainPassword, password);
    }

}
