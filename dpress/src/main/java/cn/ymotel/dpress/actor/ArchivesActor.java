package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.core.DyanmicUrlPattern;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import cn.ymotel.dpress.service.PostsService;
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
@ActorCfg(chain = "publicchain",timeout = 60*1000)
public class ArchivesActor extends  FreemarkerActor implements DyanmicUrlPattern<HttpServletRequest> {
    @Override
    public boolean ignore() {
        if(Utils.isInstall()){
            return false;
        }
        return true;
    }
    public Object getMaxStartId(){
        Map data=new HashMap();
        data.put("status",0);
        data.put("type",0);
        data.put("siteid",Utils.getSiteId());
        return sqlSession.selectOne("posts.qmaxid",data);
    }
    public Object getSubtId(int id,int offset){
        Map data=new HashMap();
        data.put("status",0);
        data.put("type",0);
        data.put("siteid",Utils.getSiteId());
        data.put("id",id);
        data.put("offset",offset);
        List list= sqlSession.selectList("posts.qprevPost1",data);
        if(list==null||list.size()==0){
            return  null;
        }
        return list.get(list.size()-1);
    }
    public Object getStartId(Object startid){
        if(startid==null){
            return  getMaxStartId();
        }
        return  startid;
    }
    public List getSubList(List list,int size,int pagesize){
        List rtnList=new ArrayList();
        int tt=size-pagesize;
        for(int i=0;i<list.size();i++){
            if(i<tt){
                continue;
            }
            rtnList.add(list.get(i));
        }
        return  rtnList;
    }

    /**
     * 一次最多请求10页
     * @param requestPage
     * @param prePage
     * @param startId
     * @param pagesize
     * @return
     */
    public Object[] getOffsetAndStartId(Integer requestPage,Integer prePage,Integer startId,int pagesize){
        if(requestPage==null||requestPage<1){
            requestPage=1;
        }
        if(prePage==null||prePage<1){
            prePage=1;
        }
        if(Math.abs(requestPage-prePage)>10){
            if(requestPage>prePage){
                requestPage=prePage+10;
            }else{
                requestPage=prePage-10;
            }
        }

        Object startid;
        int offset;
        if(requestPage>=prePage){
            //下几页，
            startid=getStartId(startId);
            offset=(requestPage-prePage)*pagesize+pagesize;
        }else{
            //上几页
            offset=(prePage-requestPage)*pagesize;
            startid= getSubtId(startId,offset);
            offset=pagesize;
        }

        Object[] obs=new Object[3];
        obs[0]=offset;
        obs[1]=startid;
        obs[2]=requestPage;
        return  obs;
    }
    @Override
    public String[] getPatterns(HttpServletRequest request) {
        //"/page/{page}/{startid}/{prePage}
        Object siteid=Utils.getFrontSiteId(request);
        String archives=optionsService.getArchives(siteid);
        return new String[]{"/"+archives,"/"+archives+"/","/"+archives+"/page/{page}/{startid}/{prePage}"};
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
        Integer p=message.getContextData("page",1);
        int pagesize=10;
        Object[] objs=getOffsetAndStartId(p,message.getContextData("prePage",1),message.getContextData("startid",Integer.class),pagesize);
        p=(Integer) objs[2];
//        String token=message.getContextData("token");
//        PostPermalinkType permalinkType = optionService.getPostPermalinkType();

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
            map.put("startid",objs[1]);
            map.put("size", objs[0]);

           List ls= sqlSession.selectList("posts.qpostslimit", map);
            ls=getSubList(ls,(Integer) objs[0],pagesize);
            Map<Integer,List> yearMap=new HashMap();
            List archives=new ArrayList();
            String archive=optionsService.getArchives(Utils.getSiteId());
           for(int i=0;i<ls.size();i++){
               Map tMap=(Map)ls.get(i);

               Map ttMap=new HashMap();
               ttMap.put("siteid",Utils.getSiteId());
               ttMap.put("postid",tMap.get("id"));
               tMap.put("tags",sqlSession.selectList("posttag.qtagbypostid",ttMap));
               tMap.put("summary",generateSummary((String)tMap.get("format_content")));
               tMap.put("fullPath","/"+archive+"/"+tMap.get("slug"));
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

//            Map ctMap=sqlSession.selectOne("posts.qpostscount",map);
//            long total=Long.parseLong(ctMap.get("ct").toString());
            long total=postsService.count(Utils.getSiteId(),0);

            Page page=new PageImpl(ls,pageable,total);
            model.addAttribute("is_archives", true);
            model.addAttribute("posts", page);
            model.addAttribute("archives", archives);
            model.addAttribute("meta_keywords", optionsService.getOption(Utils.getSiteId(),OptionsService.KEY_WORDS,""));
            model.addAttribute("meta_description",  optionsService.getOption(Utils.getSiteId(),OptionsService.DESCRIPTION,""));
            model.addAttribute("_pageKey","posts");
            model.addAttribute("_pageNumber",p);
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
    @Autowired
    PostsService postsService;
    public String index(Model model,
                         Integer page) {
        return postModel.list(page, model);
    }
}
