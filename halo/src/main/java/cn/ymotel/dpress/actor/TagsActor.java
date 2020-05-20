package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.core.DyanmicUrlPattern;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.support.BindingAwareModelMap;
import run.halo.app.controller.content.model.PostModel;
import run.halo.app.service.OptionService;
import run.halo.app.service.PostService;
import run.halo.app.utils.HaloUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//@ActorCfg(urlPatterns = "/tags/{slug}")

@ActorCfg(chain = "publicchain")
public class TagsActor extends  FreemarkerActor implements DyanmicUrlPattern<HttpServletRequest> {
    @Override
    public String[] getPatterns(HttpServletRequest request) {
        Object siteid=request.getSession().getAttribute(Utils.FRONT_SESSION_SITEID);
        String archives=optionsService.getTags(siteid);

//        String archives=optionsService.getOption(request.getSession().getAttribute(Utils.FRONT_SESSION_SITEID),"archives_prefix","archives");
        return new String[]{"/"+archives+"/{slug}"};
    }
//
//    @Autowired
//    private  PostService postService;
//    @Autowired
//    private  OptionService optionService;
    @Autowired
    private  PostModel postModel;

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    OptionsService optionsService;

    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        String  spage=message.getContextData("page");
        Integer p=new Integer(1);
        if(spage!=null){
            p=Integer.parseInt(spage);
        }

        ViewData viewData=new ViewData();

        BindingAwareModelMap model=new BindingAwareModelMap();
        viewData.setData(model);

        Pageable pageable = PageRequest
                .of(p >= 1 ? p - 1 : p, 10);
        {
            Map map = new HashMap();
            map.put("siteid", Utils.getSiteId());
            map.put("slug", message.getContextData("slug"));
            Map  tagsMap = sqlSession.selectOne("tags.qBySlug", map);
            tagsMap.put("fullPath", "/tags/" + tagsMap.get("slug"));
            Map   postParams=new HashMap();
            postParams.put("category_id",tagsMap.get("id"));
            postParams.put("siteid",Utils.getSiteId());
            postParams.put("start", pageable.getOffset());
            List posts=sqlSession.selectList("posttag.qpost",postParams);
            for(int i=0;i<posts.size();i++){
                Map tMap=(Map)posts.get(i);
                Map ttMap=new HashMap();
                ttMap.put("siteid",Utils.getSiteId());
                ttMap.put("postid",tMap.get("id"));
                tMap.put("summary",generateSummary((String)tMap.get("format_content")));
                tMap.put("fullPath","/archives/"+tMap.get("slug"));
            }
            //findAllPostIdsByCategoryId

            Map ctMap = sqlSession.selectOne("posttag.qpostcount", postParams);
            long total = Long.parseLong(ctMap.get("ct").toString());
            Page page = new PageImpl(posts, pageable, total);


            model.addAttribute("is_tag", true);
            model.addAttribute("posts", page);
            model.addAttribute("tag", tagsMap);

            model.addAttribute("meta_keywords", optionsService.getOption(Utils.getSiteId(), OptionsService.KEY_WORDS, ""));
            model.addAttribute("meta_description", optionsService.getOption(Utils.getSiteId(), OptionsService.DESCRIPTION, ""));
        }
            viewData.setViewName("tag");
        return viewData;
    }
    private final Pattern summaryPattern = Pattern.compile("\\s*|\t|\r|\n");

    @NonNull
    protected String generateSummary(@NonNull String htmlContent) {
        Assert.notNull(htmlContent, "html content must not be null");

        String text = HaloUtils.cleanHtmlTag(htmlContent);

        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");

        // Get summary length
        Integer summaryLength = 150;

        return StringUtils.substring(text, 0, summaryLength);
    }
    public String index(Model model,
                         Integer page) {
        return postModel.list(page, model);
    }
}
