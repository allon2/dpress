package run.halo.app.core.freemarker.tag;

import cn.hutool.core.util.PageUtil;
import cn.ymotel.dpress.service.OptionsService;
import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import run.halo.app.model.support.HaloConst;
import run.halo.app.model.support.Pagination;
import run.halo.app.model.support.RainbowPage;
import run.halo.app.service.OptionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static run.halo.app.model.support.HaloConst.URL_SEPARATOR;

/**
 * @author ryanwang
 * @date 2020-03-07
 */
@Component
public class PaginationTagDirective implements TemplateDirectiveModel {

    private final OptionService optionService;

    public PaginationTagDirective(Configuration configuration,
                                  OptionService optionService) {
        this.optionService = optionService;
        configuration.setSharedVariable("paginationTag", this);
    }
    public Object findPageId(Environment env) throws TemplateModelException {
        TemplateHashModel datamodel = env.getDataModel();
        TemplateModel pageKey= datamodel.get("_pageKey");
        if(pageKey==null){
            return  null;
        }

        StringModel templateModel = (StringModel) datamodel.get(((SimpleScalar)pageKey).getAsString());
        if (templateModel == null) {
            return null;
        }
        PageImpl page = (PageImpl) templateModel.getWrappedObject();
       List list= page.getContent();
       if(list==null||list.size()==0){
           return null;
       }
       Map map=(Map)list.get(0);
       return map.get("id");
    }
    @Autowired
    private OptionsService optionsService;
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Object siteid = null;
        try {
            siteid = ((SimpleNumber) env.getVariable("_siteid")).getAsNumber();
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }
        Object elementid=findPageId(env);

        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        if (params.containsKey(HaloConst.METHOD_KEY)) {

            // Get params
            String method = params.get(HaloConst.METHOD_KEY).toString();
            int page = Integer.parseInt(params.get("page").toString());
            int total = Integer.parseInt(params.get("total").toString());
            int display = Integer.parseInt(params.get("display").toString());

            // Create pagination object.
            Pagination pagination = new Pagination();

            // Generate next page full path and pre page full path.
            StringBuilder nextPageFullPath = new StringBuilder();
            StringBuilder prevPageFullPath = new StringBuilder();

//            if (optionService.isEnabledAbsolutePath()) {
//                nextPageFullPath.append(optionService.getBlogBaseUrl());
//                prevPageFullPath.append(optionService.getBlogBaseUrl());
//            }

            int[] rainbow = PageUtil.rainbow(page + 1, total, display);

            List<RainbowPage> rainbowPages = new ArrayList<>();
            StringBuilder fullPath = new StringBuilder();

//            if (optionService.isEnabledAbsolutePath()) {
//                fullPath.append(optionService.getBlogBaseUrl());
//            }

//            String pathSuffix = optionService.getPathSuffix();
             String  pathSuffix=optionsService.getPathSuffix(siteid);
            switch (method) {
                case "index":

                    nextPageFullPath.append("/page/")
                        .append(page + 2).append("/").append(elementid).append("/").append(page+1)
                        .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(URL_SEPARATOR);
                    } else {
                        prevPageFullPath.append("/page/")
                            .append(page).append("/").append(elementid).append("/").append(page+1)
                            .append(pathSuffix);
                    }

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current +"/"+elementid+"/"+(page+1)+ pathSuffix);
                        rainbowPage.setIsCurrent(page + 1 == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "archives":

                    nextPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getArchivesPrefix());
                        .append(optionsService.getArchives(siteid));
                    prevPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getArchivesPrefix());
                          .append(optionsService.getArchives(siteid));
                    nextPageFullPath.append("/page/")
                        .append(page + 2).append("/").append(elementid).append("/").append(page+1)
                        .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.
                            append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                            .append(page).append("/").append(elementid).append("/").append(page+1)
                            .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
//                        .append(optionService.getArchivesPrefix());
                          .append(optionsService.getArchives(siteid));

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString()  + current +"/"+elementid+"/"+(page+1) + pathSuffix);
                        rainbowPage.setIsCurrent(page + 1 == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "search":
                    String keyword = params.get("keyword").toString();

                    nextPageFullPath.append(URL_SEPARATOR)
                        .append("search");
                    prevPageFullPath.append(URL_SEPARATOR)
                        .append("search");

                    nextPageFullPath.append("/page/")
                        .append(page + 2)
                        .append(pathSuffix)
                        .append("?keyword=")
                        .append(keyword);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix)
                            .append("?keyword=")
                            .append(keyword);
                    } else {
                        prevPageFullPath.append("/page/")
                            .append(page)
                            .append(pathSuffix)
                            .append("?keyword=")
                            .append(keyword);
                    }

                    fullPath.append(URL_SEPARATOR)
                        .append("search");

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix + "?keyword=" + keyword);
                        rainbowPage.setIsCurrent(page + 1 == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "tagPosts":
                    String tagSlug = params.get("slug").toString();

                    nextPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getTagsPrefix())
                            .append(optionsService.getTags(siteid))
                        .append(URL_SEPARATOR)
                        .append(tagSlug);
                    prevPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getTagsPrefix())
                            .append(optionsService.getTags(siteid))

                            .append(URL_SEPARATOR)
                        .append(tagSlug);

                    nextPageFullPath.append("/page/")
                        .append(page + 2)
                        .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                            .append(page)
                            .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
//                        .append(optionService.getTagsPrefix())
                            .append(optionsService.getTags(siteid))

                            .append(URL_SEPARATOR)
                        .append(tagSlug);

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page + 1 == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "categoryPosts":
                    String categorySlug = params.get("slug").toString();

                    nextPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getCategoriesPrefix())
                            .append(optionsService.getCategories(siteid))
                        .append(URL_SEPARATOR)
                        .append(categorySlug);
                    prevPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getCategoriesPrefix())
                            .append(optionsService.getCategories(siteid))
                            .append(URL_SEPARATOR)
                        .append(categorySlug);

                    nextPageFullPath.append("/page/")
                        .append(page + 2)
                        .append(pathSuffix);
                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                            .append(page)
                            .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append(optionsService.getCategories(siteid))
//                            .append(optionService.getCategoriesPrefix())
                        .append(URL_SEPARATOR)
                        .append(categorySlug);

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page + 1 == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "photos":

                    nextPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getPhotosPrefix());
                    .append(optionsService.getPhotosPrefix(siteid));
                    prevPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getPhotosPrefix());
                    .append(optionsService.getPhotosPrefix(siteid));

                    nextPageFullPath.append("/page/")
                        .append(page + 2)
                        .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                            .append(page)
                            .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
//                        .append(optionService.getPhotosPrefix());
                    .append(optionsService.getPhotosPrefix(siteid));

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page + 1 == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "journals":

                    nextPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getJournalsPrefix());
                            .append(optionsService.getJournalsPrefix(siteid));
                    prevPageFullPath.append(URL_SEPARATOR)
//                        .append(optionService.getJournalsPrefix());
                            .append(optionsService.getJournalsPrefix(siteid));

                    nextPageFullPath.append("/page/")
                        .append(page + 2)
                        .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                            .append(page)
                            .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
//                        .append(optionService.getJournalsPrefix());
                            .append(optionsService.getJournalsPrefix(siteid));

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page + 1 == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                default:
                    break;
            }
            pagination.setNextPageFullPath(nextPageFullPath.toString());
            pagination.setPrevPageFullPath(prevPageFullPath.toString());
            pagination.setRainbowPages(rainbowPages);
            pagination.setHasNext(total != page + 1);
            pagination.setHasPrev(page != 0);
            env.setVariable("pagination", builder.build().wrap(pagination));
        }
        body.render(env.getOut());
    }
}
