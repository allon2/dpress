package cn.ymotel.dpress.admin.theme;

import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import run.halo.app.exception.BadRequestException;
import run.halo.app.exception.NotFoundException;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/themes/{themeId}",methods = RequestMethod.DELETE)
public class DeleteThemeActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Map map=new HashMap();
        map.put("key","installthemes");
        map.put("siteid",Utils.getSiteIdFromMessage(message));

        java.util.Map installthemesMap=sqlSession.selectOne("options.qoption",map);
        String themes=(String)installthemesMap.get("option_value");
        String themeId=message.getContextData("themeId");
        if(themes.indexOf(themeId)>=0){

        }else{
           throw new NotFoundException("没有找到 id 为 " + themeId + " 的主题").setErrorData(themeId);
        }

        {
            map.put("siteid",Utils.getSiteIdFromMessage(message));
            map.put("key","theme");
            java.util.Map themeMap=sqlSession.selectOne("options.qoption",map);
            String activeTheme=(String)themeMap.get("option_value");
            if(activeTheme.equals(themeId)){
                throw new BadRequestException("不能删除正在使用的主题").setErrorData(themeId);
            }

        }

        map.put("theme",themeId);
        this.sqlSession.delete("dpress.dtemplatebytheme",map);
//        this.sqlSession.delete("system_themes.dbyTheme",map);
        String[] removed = ArrayUtils.removeElement( themes.split(","), themeId);
        themes=  StringUtils.join(removed, ",");
        installthemesMap.put("update_time",new java.sql.Timestamp(System.currentTimeMillis()));
        installthemesMap.put("option_value",themes);
        installthemesMap.put("siteid",Utils.getSiteIdFromMessage(message));
        installthemesMap.put("option_key","installthemes");
        sqlSession.update("options.uoption",installthemesMap);
        return new HashMap<>();

    }


}
