package cn.ymotel.dpress.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import run.halo.app.cache.AbstractStringCacheStore;
import run.halo.app.exception.BadRequestException;
import run.halo.app.model.entity.User;
import run.halo.app.security.authentication.Authentication;
import run.halo.app.security.context.SecurityContextHolder;
import run.halo.app.security.token.AuthToken;
import run.halo.app.security.util.SecurityUtils;
import run.halo.app.utils.HaloUtils;

import java.util.concurrent.TimeUnit;

@Component
public class AutokenService {
    /**
     * Expired seconds.
     */
    int ACCESS_TOKEN_EXPIRED_SECONDS = 24 * 3600;

    int REFRESH_TOKEN_EXPIRED_DAYS = 30;
    @Autowired
    private AbstractStringCacheStore cacheStore;
    public void clearToken(User user) {
        // Check if the current is logging in
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (user == null) {
            throw new BadRequestException("您尚未登录，因此无法注销");
        }

        // Get current user
//        User user = authentication.getDetail().getUser();

        // Clear access token
        cacheStore.getAny(SecurityUtils.buildAccessTokenKey(user), String.class).ifPresent(accessToken -> {
            // Delete token
            cacheStore.delete(SecurityUtils.buildTokenAccessKey(accessToken));
            cacheStore.delete(SecurityUtils.buildAccessTokenKey(user));
        });

        // Clear refresh token
        cacheStore.getAny(SecurityUtils.buildRefreshTokenKey(user), String.class).ifPresent(refreshToken -> {
            cacheStore.delete(SecurityUtils.buildTokenRefreshKey(refreshToken));
            cacheStore.delete(SecurityUtils.buildRefreshTokenKey(user));
        });

    }
    @NonNull
    public AuthToken buildAuthToken(@NonNull User user) {
        Assert.notNull(user, "User must not be null");

        // Generate new token
        AuthToken token = new AuthToken();

        token.setAccessToken(HaloUtils.randomUUIDWithoutDash());
        token.setExpiredIn(ACCESS_TOKEN_EXPIRED_SECONDS);
        token.setRefreshToken(HaloUtils.randomUUIDWithoutDash());

        // Cache those tokens, just for clearing
        cacheStore.putAny(SecurityUtils.buildAccessTokenKey(user), token.getAccessToken(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(SecurityUtils.buildRefreshTokenKey(user), token.getRefreshToken(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        // Cache those tokens with user id
        cacheStore.putAny(SecurityUtils.buildTokenAccessKey(token.getAccessToken()), user.getId(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(SecurityUtils.buildTokenRefreshKey(token.getRefreshToken()), user.getId(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        return token;
    }
    public AuthToken refreshToken(User user,String refreshToken) {
        Integer userId = cacheStore.getAny(SecurityUtils.buildTokenRefreshKey(refreshToken), Integer.class)
                .orElseThrow(() -> new BadRequestException("登录状态已失效，请重新登录").setErrorData(refreshToken));
        // Remove all token
        cacheStore.getAny(SecurityUtils.buildAccessTokenKey(user), String.class)
                .ifPresent(accessToken -> cacheStore.delete(SecurityUtils.buildTokenAccessKey(accessToken)));
        cacheStore.delete(SecurityUtils.buildTokenRefreshKey(refreshToken));
        cacheStore.delete(SecurityUtils.buildAccessTokenKey(user));
        cacheStore.delete(SecurityUtils.buildRefreshTokenKey(user));

        return buildAuthToken(user);
    }

}
