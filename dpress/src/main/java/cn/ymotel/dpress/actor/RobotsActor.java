package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.support.BindingAwareModelMap;
import run.halo.app.utils.HaloUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ActorCfg(urlPatterns = "/robots.txt",chain = "publicchain")
public class RobotsActor extends  FreemarkerActor {

    @Override
    public Object Execute(ServletMessage message) throws Throwable {
       ViewData viewData=new ViewData();

        BindingAwareModelMap model=new BindingAwareModelMap();
        viewData.setData(model);


        viewData.setViewName("common/web/robots");
        return viewData;
    }

}
