package cn.ymotel.dpress.admin.login;

import cn.hutool.crypto.digest.BCrypt;
import cn.ymotel.dactor.action.Actor;
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
import run.halo.app.exception.BadRequestException;
import run.halo.app.model.entity.User;
import run.halo.app.security.authentication.Authentication;
import run.halo.app.security.context.SecurityContextHolder;
import run.halo.app.security.token.AuthToken;
import run.halo.app.security.util.SecurityUtils;
import run.halo.app.utils.HaloUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ActorCfg(urlPatterns = "/api/admin/refresh/{refreshToken}",methods = RequestMethod.POST)
public class RefreshActor implements Actor<ServletMessage> {

    @Autowired
    AutokenService autokenService;
    @Autowired
    private SqlSession sqlSession;

    @Override
    public Map Execute(ServletMessage message) throws Throwable {


        AuthToken token= autokenService.refreshToken((User)message.getUser(),message.getContextData("refreshToken"));
        Map rtnMap=new HashMap();
        rtnMap.put("access_token",token.getAccessToken());
        rtnMap.put("expired_in",token.getExpiredIn());
        rtnMap.put("refresh_token",token.getRefreshToken());
        return rtnMap;
    }

}
