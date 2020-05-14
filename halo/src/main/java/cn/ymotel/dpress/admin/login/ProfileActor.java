package cn.ymotel.dpress.admin.login;

import cn.hutool.crypto.digest.BCrypt;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.Message;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
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

@ActorCfg(urlPatterns = "/api/admin/users/profiles",methods = RequestMethod.GET)
public class ProfileActor implements Actor<Message> {


    @Override
    public User Execute(Message message) throws Throwable {
       User user=message.getUser();
       return user;
    }

}
