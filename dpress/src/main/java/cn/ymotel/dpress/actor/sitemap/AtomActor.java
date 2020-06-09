package cn.ymotel.dpress.actor.sitemap;

import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.actor.FreemarkerActor;
import cn.ymotel.dpress.actor.ViewData;
import cn.ymotel.dpress.entity.mapper.PostsMapper;
import cn.ymotel.dpress.entity.model.Posts;
import cn.ymotel.dpress.service.OptionsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.validation.support.BindingAwareModelMap;
import run.halo.app.utils.HaloUtils;
import run.halo.app.utils.MarkdownUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ActorCfg(urlPatterns = {"/atom", "/atom.xml"},chain = "publicchain" )
public class AtomActor extends FreemarkerActor {
        @Autowired
    private SqlSession sqlSession;
    private final static String XML_INVAID_CHAR = "[\\x00-\\x1F\\x7F]";

    @Autowired
    private OptionsService optionsService;

    @Override
    public ViewData Execute(ServletMessage message) throws Throwable {
        String baseUrl=Utils.getBaseUrl(message);

        BindingAwareModelMap model=new BindingAwareModelMap();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.orderByDesc("create_time");
        Map params=new HashMap<>();
        params.put("siteid", Utils.getSiteId());
        params.put("status",0);
        params.put("type",0);
        wrapper.allEq(params);
       List<Posts> posts= sqlSession.getMapper(PostsMapper.class).selectPage(new Page(0,20),wrapper).getRecords();
       for(int i=0;i<posts.size();i++){
           Posts post=  posts.get(i);
         if(post.getFormatContent()==null||post.getFormatContent().equals("")){
             if(post.getEditorType().equals(0)){ //MarkDown
                 post.setFormatContent(MarkdownUtils.renderHtml(post.getOriginalContent()));
             }else{
                 post.setFormatContent(post.getOriginalContent());
             }
         }
           post.setCreateTime(new java.sql.Timestamp(post.getCreateTime().getTime()));
         post.setEditTime(new java.sql.Timestamp(post.getEditTime().getTime()));
         post.setFormatContent(RegExUtils.replaceAll(post.getFormatContent(), XML_INVAID_CHAR, ""));
         if(post.getSummary()==null||post.getSummary().equals("")){
             post.setSummary(generateSummary(post.getFormatContent()));
         }
           post.setFullPath(baseUrl+"/"+optionsService.getArchives(Utils.getSiteId())+"/"+post.getSlug());
       }
        model.addAttribute("posts",posts);

        ViewData viewData=new ViewData();
        viewData.setData(model);
        viewData.setViewName("common/web/atom");
        viewData.setContentType("application/xml;charset=UTF-8");
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
}
