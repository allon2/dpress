package run.halo.app.core.freemarker.tag;

import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.TagTagsService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import run.halo.app.model.entity.Tag;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.PostTagService;
import run.halo.app.service.TagService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Freemarker custom tag of tag.
 *
 * @author ryanwang
 * @date 2019-03-22
 */
@Component
public class TagTagDirective implements TemplateDirectiveModel {
    @Autowired
    private SqlSession sqlSession;
    private final TagService tagService;


    private final PostTagService postTagService;

    public TagTagDirective(Configuration configuration,
                           TagService tagService,
                           PostTagService postTagService) {
        this.tagService = tagService;
        this.postTagService = postTagService;
        configuration.setSharedVariable("tagTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Object siteid=null;
        try {
            siteid= ((SimpleNumber)env.getVariable("_siteid")).getAsNumber();
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        String baseurl=null;
        try{
            baseurl=((SimpleScalar)env.getVariable("_baseurl")).getAsString();
        }catch (Exception e){}
        if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "list":
                    env.setVariable("tags", builder.build().wrap(getList(baseurl)));
//                    env.setVariable("tags", builder.build().wrap(postTagService.listTagWithCountDtos(Sort.by(DESC, "createTime"))));
                    break;
                case "listByPostId":
//                    Integer postId = Integer.parseInt(params.get("postId").toString());
//                    List<Tag> tags = postTagService.listTagsBy(postId);
//                    env.setVariable("tags", builder.build().wrap(tagService.convertTo(tags)));
                    env.setVariable("tags", builder.build().wrap(tagTagsService.getTagsByPostId(siteid,params.get("postId"))));
                    break;
                case "count":
                    env.setVariable("count", builder.build().wrap(tagTagsService.count(siteid)));
//                    env.setVariable("count", builder.build().wrap(tagService.count()));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
    @Autowired
    private TagTagsService tagTagsService;
    public List getList(String baseurl){
        Map map=new HashMap();
        map.put("siteid", Utils.getSiteId());
       List ls=  sqlSession.selectList("tags.qall",map);
       for(int i=0;i<ls.size();i++){
           Map tMap=(Map)ls.get(i);
           tMap.put("fullPath",baseurl+"/tags/"+tMap.get("slug"));
       }
       return ls;
    }
}
