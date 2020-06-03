package cn.ymotel.dpress.actor;

import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.OptionsService;
import cn.ymotel.dpress.service.PostCategorysService;
import cn.ymotel.dpress.service.PostsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.lang.NonNull;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.support.BindingAwareModelMap;
import run.halo.app.controller.content.model.PostModel;
import run.halo.app.model.enums.PostPermalinkType;
import run.halo.app.service.MenuService;
import run.halo.app.service.OptionService;
import run.halo.app.service.PostService;
import run.halo.app.utils.HaloUtils;
import run.halo.app.utils.MarkdownUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.domain.Sort.Direction.DESC;

@ActorCfg(urlPatterns = {"/","/page/{page}/{startid}/{prePage}"},chain = "publicchain",timeout = 60*1000)
public class IndexActor extends  FreemarkerActor {
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


    @Autowired
    PostCategorysService postCategorysService;
    /**
     * 下一页比上一页大
     * @param page
     * @param prePageNumber
     * @param size
     * @return 返回Offset
     */
    public int  getOffset(int page,int prePageNumber,int size){
           return  (page-prePageNumber)*size;

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
    @Autowired
    private  MenuService menuService;

    @Override
    public Object Execute(ServletMessage message) throws Throwable {

//        postCategorysService.findPostIdsBycategoryId(Utils.getSiteId(),0);

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
                .of(p >= 1 ? p - 1 : p, pagesize);
        {
//            int offset=getOffset(p,message.getContextData("prePage",1),pagesize);
//            Object startid;
//            if(offset<0){
//                startid= getSubtId(message.getContextData("startid",Integer.class),pagesize);
//                offset=pagesize;
//            }else{
//                startid=getStartId(message.getContextData("startid",Integer.class));
//            }
//            offset=Math.abs(offset);
            Map map = new HashMap();
            map.put("status", "0");
            map.put("siteid", Utils.getSiteId());
            map.put("start", pageable.getOffset());
            map.put("startid",objs[1]);
            map.put("size", objs[0]);

            List ls= sqlSession.selectList("posts.qpostslimit", map);
                ls=getSubList(ls,(Integer) objs[0],pagesize);
           for(int i=0;i<ls.size();i++){
               Map tMap=(Map)ls.get(i);
               Map ttMap=new HashMap();
               ttMap.put("siteid",Utils.getSiteId());
               ttMap.put("postid",tMap.get("id"));



               String formatContent="";
               if(tMap.get("editor_type").equals(0)){
                   formatContent= MarkdownUtils.renderHtml((String)tMap.get("original_content"));
               }else{
                   formatContent=(String)tMap.get("original_content");
               }



               tMap.put("tags",sqlSession.selectList("posttag.qtagbypostid",ttMap));
               tMap.put("summary",generateSummary(formatContent));
               tMap.put("fullPath","/"+optionsService.getArchives(Utils.getSiteId())+"/"+tMap.get("slug"));
           }

//            Map ctMap=sqlSession.selectOne("posts.qpostscount",map);
//            {
//                long end = System.currentTimeMillis();
//                System.out.println("qpostscount time--" + (end - begin) / 1000);
//            }
//            long total=Long.parseLong(ctMap.get("ct").toString());
            long total=postsService.count(Utils.getSiteId(),0);
            Page page=new PageImpl(ls,pageable,total);
            model.addAttribute("is_index", true);
            model.addAttribute("posts", page);
            model.addAttribute("meta_keywords", optionsService.getOption(Utils.getSiteId(),OptionsService.KEY_WORDS,""));
            model.addAttribute("meta_description",  optionsService.getOption(Utils.getSiteId(),OptionsService.DESCRIPTION,""));
            model.addAttribute("_pageKey","posts");
            model.addAttribute("_pageNumber",p);
        }
        viewData.setViewName("index");
        return viewData;
    }
    @Autowired
    PostsService postsService;
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
