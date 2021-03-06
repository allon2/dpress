package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.core.DyanmicUrlPattern;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.support.BindingAwareModelMap;
import run.halo.app.utils.HaloUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//@ActorCfg(urlPatterns = "/s/{slug}")

@ActorCfg(chain = "publicchain")
public class SheetActor extends  FreemarkerActor implements DyanmicUrlPattern<HttpServletRequest> {
    @Override
    public boolean ignore() {
        if(Utils.isInstall()){
            return false;
        }
        return true;
    }

    @Autowired
    OptionsService optionsService;
    @Override
    public String[] getPatterns(HttpServletRequest request) {

        Object siteid=Utils.getFrontSiteId(request);
        String archives=optionsService.getSheet(siteid);

//        String archives=optionsService.getOption(request.getSession().getAttribute(Utils.FRONT_SESSION_SITEID),"archives_prefix","archives");
        return new String[]{"/"+archives+"/{slug}"};
    }
    @Autowired
    private SqlSession sqlSession;
    private final Pattern summaryPattern = Pattern.compile("\\s*|\t|\r|\n");

    public String generateDescription(String content) {
        Assert.notNull(content, "html content must not be null");

        String text = HaloUtils.cleanHtmlTag(content);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        // Get summary length
        Integer summaryLength =150;

        return StringUtils.substring(text, 0, summaryLength);
    }

    @Override
    public Object Execute(ServletMessage message) throws Throwable {

        Map tMap=new HashMap();
        tMap.put("slug",message.getContext().get("slug"));
        tMap.put("siteid", Utils.getSiteId());
       Map postData= sqlSession.selectOne("posts.qsheet",tMap);


        ViewData viewData=new ViewData();

        BindingAwareModelMap model=new BindingAwareModelMap();
        viewData.setData(model);
        model.put("meta_keywords",postData.get("meta_keywords"));
        model.put("meta_description",postData.get("meta_description"));
        model.put("post",postData);


        model.addAttribute("sheet", postData);
        model.addAttribute("post", postData);
        model.addAttribute("is_sheet", true);


        Map categoriesMap=new HashMap();
        categoriesMap.put("postid",postData.get("id"));
        categoriesMap.put("siteid",Utils.getSiteId());
        sqlSession.update("posts.uvisit",categoriesMap);
        List categoriesList=sqlSession.selectList("post_categories.qbypostid",categoriesMap);
        model.put("categories",categoriesList);
        model.put("tags", Collections.EMPTY_LIST);
        model.put("metas", Collections.EMPTY_LIST);
//        Map prevMap=this.sqlSession.selectOne("posts.qprevPost",categoriesMap);
//        Map nextMap=this.sqlSession.selectOne("posts.qnextPost",categoriesMap);
//        if(prevMap!=null){
//            prevMap.put("fullPath","/archives/"+prevMap.get("slug"));
//        }
//        if(nextMap!=null){
//            nextMap.put("fullPath","/archives/"+nextMap.get("slug"));
//        }
//        model.put("nextPost",nextMap);
//        model.put("prevPost",prevMap);


//
//        if (PostPermalinkType.ID.equals(permalinkType) && !Objects.isNull(p)) {
//            Post post = postService.getById(p);
//
//            viewData.setViewName(postModel.content(post, token, model));
//            return viewData;
//        }
        viewData.setViewName("sheet");
        return viewData;
    }

}
