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
import run.halo.app.model.enums.PostPermalinkType;
import run.halo.app.service.OptionService;
import run.halo.app.service.PostService;
import run.halo.app.utils.HaloUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//@ActorCfg(urlPatterns = "/categories/{slug}")

@ActorCfg(chain = "publicchain")
public class CategoryActor extends  FreemarkerActor implements DyanmicUrlPattern<HttpServletRequest> {
    @Override
    public boolean ignore() {
        if(Utils.isInstall()){
            return false;
        }
        return true;
    }

    @Override
    public String[] getPatterns(HttpServletRequest request) {

        Object siteid=Utils.getFrontSiteId(request);
        String archives=optionsService.getCategories(siteid);

//        String archives=optionsService.getOption(request.getSession().getAttribute(Utils.FRONT_SESSION_SITEID),"archives_prefix","archives");
        return new String[]{"/"+archives+"/{slug}"};
    }
    @Autowired
    private  PostService postService;
    @Autowired
    private  OptionService optionService;
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
        String token=message.getContextData("token");
        PostPermalinkType permalinkType = optionService.getPostPermalinkType();

        ViewData viewData=new ViewData();

        BindingAwareModelMap model=new BindingAwareModelMap();
        viewData.setData(model);

//        if (PostPermalinkType.ID.equals(permalinkType) && !Objects.isNull(p)) {
//            Post post = postService.getById(p);
//            viewData.setViewName(postModel.content(post, token, model));
//            return viewData;
//        }
//        this.index(model, p);

        Pageable pageable = PageRequest
                .of(p >= 1 ? p - 1 : p, 10);
        {
            Map map = new HashMap();
            map.put("siteid", Utils.getSiteId());
            map.put("slug", message.getContextData("slug"));
            Map  categoriesMap = sqlSession.selectOne("categories.qBySlug", map);
            categoriesMap.put("fullPath", "/categories/" + categoriesMap.get("slug"));
            Map   postParams=new HashMap();
            postParams.put("category_id",categoriesMap.get("id"));
            postParams.put("siteid",Utils.getSiteId());
            postParams.put("start", pageable.getOffset());
            List posts=sqlSession.selectList("post_categories.qpost",postParams);
            for(int i=0;i<posts.size();i++){
                Map tMap=(Map)posts.get(i);
                Map ttMap=new HashMap();
                ttMap.put("siteid",Utils.getSiteId());
                ttMap.put("postid",tMap.get("id"));
                tMap.put("tags",sqlSession.selectList("posttag.qtagbypostid",ttMap));
                tMap.put("summary",generateSummary((String)tMap.get("format_content")));
                tMap.put("fullPath","/archives/"+tMap.get("slug"));
            }
            //findAllPostIdsByCategoryId

            Map ctMap = sqlSession.selectOne("post_categories.qpostcount", postParams);
            long total = Long.parseLong(ctMap.get("ct").toString());
            Page page = new PageImpl(posts, pageable, total);


            model.addAttribute("is_category", true);
            model.addAttribute("posts", posts);
            model.addAttribute("category", categoriesMap);
            model.addAttribute("posts", page);
            model.addAttribute("meta_keywords", optionsService.getOption(Utils.getSiteId(), OptionsService.KEY_WORDS, ""));
            model.addAttribute("meta_description", optionsService.getOption(Utils.getSiteId(), OptionsService.DESCRIPTION, ""));
        }
            viewData.setViewName("category");
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
