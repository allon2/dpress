package cn.ymotel.dpress.admin.chain;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.pattern.MatchPair;
import cn.ymotel.dactor.pattern.PatternMatcher;
import cn.ymotel.dactor.spring.annotaion.BeforeChain;
import cn.ymotel.dpress.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import run.halo.app.cache.AbstractStringCacheStore;
import run.halo.app.exception.AuthenticationException;
import run.halo.app.security.util.SecurityUtils;

import java.util.Optional;

@BeforeChain(order = 2)
public class isLoginBeforeChain implements Actor<ServletMessage> {
    @Autowired
    protected  AbstractStringCacheStore cacheStore;
    private AntPathMatcher antPathMatcher=new AntPathMatcher();
    PatternMatcher patternMatcher;
    public isLoginBeforeChain() {
        patternMatcher=new PatternMatcher(new String[]{"/api/admin/**","/api/content/comments"},new String[]{
                "/api/admin/login",
                "/api/admin/refresh/*",
                "/api/admin/installations",
                "/api/admin/migrations/halo",
                "/api/admin/is_installed",
                "/api/admin/password/code",
                "/api/admin/password/reset",
                "/api/admin/login/precheck",
                "/api/admin/logout"
        },null,null);
        patternMatcher.setPathMatcher(antPathMatcher);
    }

    @Override
    public <E> E Execute(ServletMessage message) throws Throwable {

        MatchPair pair= patternMatcher.matchePatterns(message.getRequest().getServletPath(),null);
        if(pair==null){
            return null;
        }

        // Get token from request
        String token = Utils.getTokenFromRequest(message.getRequest());

        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("未登录，请登录后访问");
        }
        Optional<Integer> optionalUserId = cacheStore.getAny(SecurityUtils.buildTokenAccessKey(token), Integer.class);

        if (!optionalUserId.isPresent()) {
            throw new AuthenticationException("Token 已过期或不存在").setErrorData(token);
        }

        return null;
    }
}
