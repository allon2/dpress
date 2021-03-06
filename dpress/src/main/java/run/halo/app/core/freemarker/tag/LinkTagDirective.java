package run.halo.app.core.freemarker.tag;

import cn.ymotel.dpress.service.LinksServices;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.LinkService;

import java.io.IOException;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Freemarker custom tag of link.
 *
 * @author ryanwang
 * @date 2019-03-22
 */
@Component
public class LinkTagDirective implements TemplateDirectiveModel {

    private final LinkService linkService;
    @Autowired
    private LinksServices linksServices;
    public LinkTagDirective(Configuration configuration, LinkService linkService) {
        this.linkService = linkService;
        configuration.setSharedVariable("linkTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        long siteid=((SimpleNumber)env.getVariable("_siteid")).getAsNumber().longValue();
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "list":
                    env.setVariable("links", builder.build().wrap(linksServices.listAll(siteid)));
//                    env.setVariable("links", builder.build().wrap(linkService.listAll()));
                    break;
                case "listTeams":
                    env.setVariable("teams", builder.build().wrap(linksServices.listTeams(siteid,"create_time")));
//                    env.setVariable("teams", builder.build().wrap(linkService.listTeamVos(Sort.by(DESC, "createTime"))));
                    break;
                case "count":
                    env.setVariable("count", builder.build().wrap(linksServices.count(siteid)));

//                    env.setVariable("count", builder.build().wrap(linkService.count()));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
}
