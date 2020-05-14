package cn.ymotel.dpress.admin.backups;

import cn.hutool.core.date.DateUtil;
import cn.ymotel.dactor.action.Actor;
import cn.ymotel.dactor.message.ServletMessage;
import cn.ymotel.dactor.spring.annotaion.ActorCfg;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import run.halo.app.model.entity.Category;
import run.halo.app.model.entity.Tag;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.utils.MarkdownUtils;
import run.halo.app.utils.SlugUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActorCfg(urlPatterns = "/api/admin/backups/markdown",methods = RequestMethod.POST)
public class MarkdownImportActor implements Actor<ServletMessage> {
    @Autowired
    private SqlSession sqlSession;
    @Override
    public Map Execute(ServletMessage message) throws Throwable {
       byte[] bs= message.getFileBytes("file");
        String markdown=new String(bs, StandardCharsets.UTF_8);
        Map<String, List<String>> frontMatter = MarkdownUtils.getFrontMatter(markdown);
        List<String> elementValue;
        Map postMap=new HashMap();
        postMap.put("type",0);
        if (frontMatter.size() > 0) {
            for (String key : frontMatter.keySet()) {
                elementValue = frontMatter.get(key);
                for (String ele : elementValue) {
                    switch (key) {
                        case "title":
                            postMap.put("title",ele);
                            break;
                        case "date":
                            postMap.put("create_time",DateUtil.parse(ele));

                            break;
                        case "permalink":
                            postMap.put("slug",ele);

                            break;
                        case "thumbnail":
                            postMap.put("thumbnail",ele);
                            break;
                        case "status":
                            postMap.put("status",PostStatus.valueOf(ele).getValue());
                            break;
                        case "comments":
                            postMap.put("disallow_comment",Boolean.parseBoolean(ele));
                            break;

                        default:
                            break;
                    }
                }
            }
            sqlSession.insert("posts.i",postMap);
        }
        return  new HashMap();
    }
}
