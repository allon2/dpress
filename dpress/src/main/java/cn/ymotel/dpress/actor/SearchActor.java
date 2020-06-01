package cn.ymotel.dpress.actor;

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
import org.springframework.util.Assert;
import org.springframework.validation.support.BindingAwareModelMap;
import run.halo.app.utils.HaloUtils;
import run.halo.app.utils.MarkdownUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ActorCfg(urlPatterns = {"/search","/search/page/{page}"} , chain = "publicchain",timeout = 2*60*1000)
public class SearchActor  extends  FreemarkerActor {
//    public Object getMaxStartId(String keyword){
//        Map data=new HashMap();
//        data.put("status",0);
//        data.put("type",0);
//        data.put("siteid", Utils.getSiteId());
//        data.put("keyword","%"+keyword+"%");
//        return sqlSession.selectOne("posts.qsearchmaxid",data);
//    }
//    public Object getSubtId(int id,int offset,String keyword){
//        Map data=new HashMap();
//        data.put("status",0);
//        data.put("type",0);
//        data.put("siteid",Utils.getSiteId());
//        data.put("id",id);
//        data.put("offset",offset);
//        data.put("keyword","%"+keyword+"%");
//
//        List list= sqlSession.selectList("posts.qsearchprevPost1",data);
//        if(list==null||list.size()==0){
//            return  null;
//        }
//        return list.get(list.size()-1);
//    }
//    public Object getStartId(Object startid,String keyword){
//        if(startid==null){
//            return  getMaxStartId(keyword);
//        }
//        return  startid;
//    }
//    public List getSubList(List list,int size,int pagesize){
//        List rtnList=new ArrayList();
//        int tt=size-pagesize;
//        for(int i=0;i<list.size();i++){
//            if(i<tt){
//                continue;
//            }
//            rtnList.add(list.get(i));
//        }
//        return  rtnList;
//    }
//
//    /**
//     * 一次最多请求10页
//     * @param requestPage
//     * @param prePage
//     * @param startId
//     * @param pagesize
//     * @return
//     */
//    public Object[] getOffsetAndStartId(Integer requestPage,Integer prePage,Integer startId,int pagesize,String keyword){
//        if(requestPage==null||requestPage<1){
//            requestPage=1;
//        }
//        if(prePage==null||prePage<1){
//            prePage=1;
//        }
//        if(Math.abs(requestPage-prePage)>10){
//            if(requestPage>prePage){
//                requestPage=prePage+10;
//            }else{
//                requestPage=prePage-10;
//            }
//        }
//
//        Object startid;
//        int offset;
//        if(requestPage>=prePage){
//            //下几页，
//            startid=getStartId(startId,keyword);
//            offset=(requestPage-prePage)*pagesize+pagesize;
//        }else{
//            //上几页
//            offset=(prePage-requestPage)*pagesize;
//            startid= getSubtId(startId,offset,keyword);
//            offset=pagesize;
//        }
//
//        Object[] obs=new Object[3];
//        obs[0]=offset;
//        obs[1]=startid;
//        obs[2]=requestPage;
//        return  obs;
//    }

    @Override
    public Object Execute(ServletMessage message) throws Throwable {
        Integer p=message.getContextData("page",1);
        String keyword=message.getContextData("keyword");
        int pagesize=10;
//        Object[] objs=getOffsetAndStartId(p,message.getContextData("prePage",1),message.getContextData("startid",Integer.class),pagesize,keyword);
//        p=(Integer) objs[2];

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
            map.put("size",pagesize);
//            map.put("startid",objs[1]);
//            map.put("size", objs[0]);
            map.put("keyword","%"+keyword+"%");
            List ls= sqlSession.selectList("posts.qsearchpostslimit", map);
//            ls=getSubList(ls,(Integer) objs[0],pagesize);
            for(int i=0;i<ls.size();i++){
                Map tMap=(Map)ls.get(i);

                String formatContent="";
                if(tMap.get("editor_type").equals("0")){
                    formatContent= MarkdownUtils.renderHtml((String)tMap.get("original_content"));
                }else{
                    formatContent=(String)tMap.get("original_content");
                }


                Map ttMap=new HashMap();
                ttMap.put("siteid",Utils.getSiteId());
                ttMap.put("postid",tMap.get("id"));
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
            long total=postsService.count(Utils.getSiteId(),0,keyword);
            Page page=new PageImpl(ls,pageable,total);
            model.addAttribute("is_search", true);
            model.addAttribute("keyword", keyword);
            model.addAttribute("posts", page);
            model.addAttribute("meta_keywords", optionsService.getOption(Utils.getSiteId(), OptionsService.KEY_WORDS,""));
            model.addAttribute("meta_description",  optionsService.getOption(Utils.getSiteId(),OptionsService.DESCRIPTION,""));
            model.addAttribute("_pageKey","posts");
            model.addAttribute("_pageNumber",p);
        }
        viewData.setViewName("search");
        return viewData;
    }
    @Autowired
    private SqlSession sqlSession;

    @Autowired
    OptionsService optionsService;
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
}
