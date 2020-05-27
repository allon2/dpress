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
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@ActorCfg(urlPatterns = {"/archives","/archives/","/archives/page/{page}"})
@ActorCfg(chain = "publicchain")
public class ArchivesActor extends  FreemarkerActor implements DyanmicUrlPattern<HttpServletRequest> {
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
        String archives=optionsService.getArchives(siteid);
        return new String[]{"/"+archives,"/"+archives+"/","/"+archives+"/page/{page}"};
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
            map.put("status", "0");
            map.put("siteid", Utils.getSiteId());
            map.put("start", pageable.getOffset());
           List ls= sqlSession.selectList("posts.qpostslimit", map);
            Map<Integer,List> yearMap=new HashMap();
            List archives=new ArrayList();

           for(int i=0;i<ls.size();i++){
               Map tMap=(Map)ls.get(i);

               Map ttMap=new HashMap();
               ttMap.put("siteid",Utils.getSiteId());
               ttMap.put("postid",tMap.get("id"));
               tMap.put("tags",sqlSession.selectList("posttag.qtagbypostid",ttMap));
               tMap.put("summary",generateSummary((String)tMap.get("format_content")));
               tMap.put("fullPath","/archives/"+tMap.get("slug"));
               java.util.Date date=(java.util.Date)tMap.get("createTime");
               Calendar calendar=Calendar.getInstance();
               calendar.setTime(date);

               List tls=yearMap.computeIfAbsent(calendar.get(Calendar.YEAR), k->new ArrayList());
                tls.add(tMap);
               ;

           }
            yearMap.forEach(new BiConsumer<Integer, List>() {
                @Override
                public void accept(Integer integer, List list) {
                    Map map=new HashMap();
                    map.put("year",integer);
                    map.put("posts",list);
                    archives.add(map);
                }
            });



            Map ctMap=sqlSession.selectOne("posts.qpostscount",map);
            long total=Long.parseLong(ctMap.get("ct").toString());
            Page page=new PageImpl(ls,pageable,total);
            model.addAttribute("is_archives", true);
            model.addAttribute("posts", page);
            model.addAttribute("archives", archives);
            model.addAttribute("meta_keywords", optionsService.getOption(Utils.getSiteId(),OptionsService.KEY_WORDS,""));
            model.addAttribute("meta_description",  optionsService.getOption(Utils.getSiteId(),OptionsService.DESCRIPTION,""));
        }
        viewData.setViewName("archives");
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
