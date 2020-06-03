package run.halo.app.core.freemarker.tag;

import cn.ymotel.dpress.service.PostsService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import run.halo.app.model.entity.Post;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.PostCategoryService;
import run.halo.app.service.PostService;
import run.halo.app.service.PostTagService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Freemarker custom tag of post.
 *
 * @author ryanwang
 * @date 2018-04-26
 */
@Component
public class PostTagDirective implements TemplateDirectiveModel {

    private final PostService postService;

    private final PostTagService postTagService;

    private final PostCategoryService postCategoryService;

    public PostTagDirective(Configuration configuration,
                            PostService postService,
                            PostTagService postTagService,
                            PostCategoryService postCategoryService) {
        this.postService = postService;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
        configuration.setSharedVariable("postTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        long siteid=((SimpleNumber)env.getVariable("_siteid")).getAsNumber().longValue();
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "latest":
                    int top = Integer.parseInt(params.get("top").toString());
                    env.setVariable("posts", builder.build().wrap(postsService.listLatest(siteid,top)));
//                    env.setVariable("posts", builder.build().wrap(postService.convertToListVo(postService.listLatest(top))));
                    break;
                case "count":

                env.setVariable("count", builder.build().wrap(postsService.count(siteid,PostStatus.PUBLISHED.getValue())));
//                    env.setVariable("count", builder.build().wrap(postService.countByStatus(PostStatus.PUBLISHED)));
                    break;
                case "archiveYear":
                    env.setVariable("archives", builder.build().wrap(postsService.listYearArchives(siteid)));
//                    env.setVariable("archives", builder.build().wrap(postService.listYearArchives()));
                    break;
                case "archiveMonth":
                    env.setVariable("archives", builder.build().wrap(postsService.listMonthArchives(siteid)));
//                    env.setVariable("archives", builder.build().wrap(postService.listMonthArchives()));
                    break;
                case "archive":
                    String type = params.get("type").toString();
                    env.setVariable("archives", builder.build().wrap("year".equals(type) ? postsService.listYearArchives(siteid) : postsService.listMonthArchives(siteid)));
//                    env.setVariable("archives", builder.build().wrap("year".equals(type) ? postService.listYearArchives() : postService.listMonthArchives()));
                    break;
                case "listByCategoryId":
                    Integer categoryId = Integer.parseInt(params.get("categoryId").toString());
//                    env.setVariable("posts", builder.build().wrap(postService.convertToListVo(postCategoryService.listPostBy(categoryId, PostStatus.PUBLISHED))));
                    env.setVariable("posts", builder.build().wrap(postsService.getPostsByPostCategoriesId(siteid,categoryId,0)));
                    break;
                case "listByCategorySlug":
                    String categorySlug = params.get("categorySlug").toString();
//                    List<Post> posts = postCategoryService.listPostBy(categorySlug, PostStatus.PUBLISHED);
//                    env.setVariable("posts", builder.build().wrap(postService.convertToListVo(posts)));
                    env.setVariable("posts", builder.build().wrap(postsService.getPostsByCategorieSlug(siteid,categorySlug,0)));
                    break;
                case "listByTagId":
                    Integer tagId = Integer.parseInt(params.get("tagId").toString());
//                    env.setVariable("posts", builder.build().wrap(postService.convertToListVo(postTagService.listPostsBy(tagId, PostStatus.PUBLISHED))));
                    env.setVariable("posts", builder.build().wrap(postsService.getPostsByPostTagId(siteid,tagId,0)));
                    break;
                case "listByTagSlug":
                    String tagSlug = params.get("tagSlug").toString();
                    //getPostsByTagSlug
//                    env.setVariable("posts", builder.build().wrap(postService.convertToListVo(postTagService.listPostsBy(tagSlug, PostStatus.PUBLISHED))));

                    env.setVariable("posts", builder.build().wrap(postsService.getPostsByTagSlug(siteid,tagSlug,0)));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
    @Autowired
    private PostsService postsService;
}
