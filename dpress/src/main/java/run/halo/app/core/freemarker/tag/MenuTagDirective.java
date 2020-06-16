package run.halo.app.core.freemarker.tag;

import cn.ymotel.dpress.service.MenusService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.MenuService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Freemarker custom tag of menu.
 *
 * @author ryanwang
 * @date 2019-03-22
 */
@Component
public class MenuTagDirective implements TemplateDirectiveModel {

    private static final String METHOD_KEY = "method";

    private final MenuService menuService;
    @Autowired
    private SqlSession sqlSession;

    public MenuTagDirective(Configuration configuration, MenuService menuService) {
        this.menuService = menuService;
        configuration.setSharedVariable("menuTag", this);
    }
    @Autowired
    private MenusService menusService;
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Object siteid=null;
        try {
            siteid= ((SimpleNumber)env.getVariable("_siteid")).getAsNumber();
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "list": {
//                    env.setVariable("menus", builder.build().wrap(menuService.findBySiteid()));
                    Map map = new HashMap();
                    map.put("siteid", siteid);
                    List ls = sqlSession.selectList("menus.qall", map);
                    env.setVariable("menus", builder.build().wrap(ls));
                }
                    break;
                case "tree":
//                    env.setVariable("menus", builder.build().wrap(menuService.listAsTree(Sort.by(DESC, "priority"))));
                    env.setVariable("menus", builder.build().wrap(menusService.listAsTree(siteid)));
                    break;
                case "listTeams": {

                    ;
                    env.setVariable("teams", builder.build().wrap(menusService.listTeams(siteid)));
//                    env.setVariable("teams", builder.build().wrap(menuService.listTeamVos(Sort.by(DESC, "priority"))));
                }
                break;
                case "listByTeam": {
                    String team = params.get("team").toString();
                    Map map=new HashMap();
                    map.put("team",team);
                    map.put("siteid",siteid);
                    List list=sqlSession.selectList("menus.qbyteam",map);
                    env.setVariable("menus", builder.build().wrap(list));
//                    env.setVariable("menus", builder.build().wrap(menuService.listByTeam(team, Sort.by(DESC, "priority"))));
                }
                break;
                case "treeByTeam":
                    String treeTeam = params.get("team").toString();
//                    env.setVariable("menus", builder.build().wrap(menuService.listByTeamAsTree(treeTeam, Sort.by(DESC, "priority"))));
                    env.setVariable("menus", builder.build().wrap(menusService.treebyteam(siteid,treeTeam)));
                    break;
                case "count": {
                    Map map = new HashMap();
                    map.put("siteid", siteid);
                    Long count=sqlSession.selectOne("menus.count",map);
                    env.setVariable("count", builder.build().wrap(count));
                }
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
}
