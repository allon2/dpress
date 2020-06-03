package run.halo.app.core.freemarker.tag;

import cn.ymotel.dpress.service.CommentsService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import run.halo.app.model.entity.PostComment;
import run.halo.app.model.enums.CommentStatus;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.PostCommentService;

import java.io.IOException;
import java.util.Map;

/**
 * Freemarker custom tag of comment.
 *
 * @author ryanwang
 * @date 2019-03-22
 */
@Component
public class CommentTagDirective implements TemplateDirectiveModel {

    private final PostCommentService postCommentService;

    public CommentTagDirective(Configuration configuration, PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
        configuration.setSharedVariable("commentTag", this);
    }
    @Autowired
    private CommentsService commentsService;
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        long siteid=((SimpleNumber)env.getVariable("_siteid")).getAsNumber().longValue();
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "latest":
                    int top = Integer.parseInt(params.get("top").toString());
//                    Page<PostComment> postComments = postCommentService.pageLatest(top, CommentStatus.PUBLISHED);
//                    env.setVariable("comments", builder.build().wrap(postCommentService.convertToWithPostVo(postComments)));
                    env.setVariable("comments", builder.build().wrap(commentsService.Latest(siteid,top,0)));
                    break;
                case "count":
                    env.setVariable("count", builder.build().wrap(commentsService.count(siteid)));
//                    env.setVariable("count", builder.build().wrap(postCommentService.count()));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
}
