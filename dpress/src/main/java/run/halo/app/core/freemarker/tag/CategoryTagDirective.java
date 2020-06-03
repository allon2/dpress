package run.halo.app.core.freemarker.tag;

import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.service.CategorysService;
import cn.ymotel.dpress.service.OptionsService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import run.halo.app.model.entity.Category;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.CategoryService;
import run.halo.app.service.PostCategoryService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Freemarker custom tag of category.
 *
 * @author ryanwang
 * @date 2019-03-22
 */
@Component
public class CategoryTagDirective implements TemplateDirectiveModel {
    @Autowired
    private SqlSession sqlSession;
    private final CategoryService categoryService;

    @Autowired
    private CategorysService categorysService;
    private final PostCategoryService postCategoryService;

    public CategoryTagDirective(Configuration configuration,
                                CategoryService categoryService,
                                PostCategoryService postCategoryService) {
        this.categoryService = categoryService;
        this.postCategoryService = postCategoryService;
        configuration.setSharedVariable("categoryTag", this);
    }
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {

        Object siteid=null;
        try {
            siteid= ((SimpleNumber)env.getVariable("_siteid")).getAsNumber();
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }
        String baseurl=null;
        try{
            baseurl=((SimpleScalar)env.getVariable("_baseurl")).getAsString();
        }catch (Exception e){}

        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
         if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "list":
                    env.setVariable("categories", builder.build().wrap(getList(baseurl,siteid)));
//                    env.setVariable("categories", builder.build().wrap(postCategoryService.listCategoryWithPostCountDto(Sort.by(DESC, "createTime"))));
                    break;
                case "tree":
                    env.setVariable("categories", builder.build().wrap(categorysService.listAsTree(siteid,"create_time")));
//                    env.setVariable("categories", builder.build().wrap(categoryService.listAsTree(Sort.by(DESC, "createTime"))));
                    break;
                case "listByPostId":
                    Integer postId = Integer.parseInt(params.get("postId").toString());
//                    List<Category> categories = postCategoryService.listCategoriesBy(postId);
//                    env.setVariable("categories", builder.build().wrap(categoryService.convertTo(categories)));

                     env.setVariable("categories", builder.build().wrap(categorysService.listCategoriesbyPostId(siteid,postId)));
                    break;
                case "count":

                    env.setVariable("count", builder.build().wrap(categorysService.count(siteid)));
//                    env.setVariable("count", builder.build().wrap(categoryService.count()));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
    @Autowired
    OptionsService optionsService;
    public List getList(String baseurl,Object siteid){
        Map map=new HashMap();
        map.put("siteid", Utils.getSiteId());
        List ls=  sqlSession.selectList("categories.qall",map);
        for(int i=0;i<ls.size();i++){
            Map tMap=(Map)ls.get(i);
            tMap.put("fullPath",baseurl+"/"+optionsService.getCategories(siteid)+"/"+tMap.get("slug"));
        }
        return ls;
    }
}
