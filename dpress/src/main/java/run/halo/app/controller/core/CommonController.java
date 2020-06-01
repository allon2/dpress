package run.halo.app.controller.core;

import cn.hutool.extra.servlet.ServletUtil;
import cn.ymotel.dpress.Utils;
import cn.ymotel.dpress.template.MultiDomainFreeMarkerView;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.UrlPathHelper;
import run.halo.app.exception.AbstractHaloException;
import run.halo.app.exception.NotFoundException;
import run.halo.app.service.OptionService;
import run.halo.app.service.ThemeService;
import run.halo.app.utils.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Error page Controller
 *
 * @author ryanwang
 * @date 2017-12-26
 */
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CommonController extends AbstractErrorController {

    private static final String NOT_FOUND_TEMPLATE = "404.ftl";

    private static final String INTERNAL_ERROR_TEMPLATE = "500.ftl";

    private static final String ERROR_TEMPLATE = "error.ftl";

    private static final String DEFAULT_ERROR_PATH = "common/error/error";

    private static final String COULD_NOT_RESOLVE_VIEW_WITH_NAME_PREFIX = "Could not resolve view with name '";

    private final ThemeService themeService;

    private final ErrorProperties errorProperties;

    private final ErrorAttributes errorAttributes;

    private final OptionService optionService;

    public CommonController(ThemeService themeService,
                            ErrorAttributes errorAttributes,
                            ServerProperties serverProperties,
                            OptionService optionService) {
        super(errorAttributes);
        this.themeService = themeService;
        this.errorAttributes = errorAttributes;
        this.errorProperties = serverProperties.getError();
        this.optionService = optionService;
    }
    @Autowired
    private SqlSession sqlSession;
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    /**
     * Handle error
     *
     * @param request request
     * @return String
     */
    @GetMapping(produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String handleError(HttpServletRequest request, HttpServletResponse response, Model model) {
        log.error("Request URL: [{}], URI: [{}], Request Method: [{}], IP: [{}]",
            request.getRequestURL(),
            request.getRequestURI(),
            request.getMethod(),
            ServletUtil.getClientIP(request));


        handleCustomException(request);

        Map<String, Object> errorDetail = Collections.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request)));
        {
            String path=(String)errorDetail.get("path");
            path=path.toLowerCase();
            if(path.endsWith(".jpg")||path.endsWith(".png")||path.endsWith(".js")||path.endsWith(".css")){
                return "";
            }
        }
        model.addAttribute("error", errorDetail);
        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
        model.addAttribute("meta_description", optionService.getSeoDescription());

        log.debug("Error detail: [{}]", errorDetail);

        HttpStatus status = getStatus(request);

        if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            try {
                String domain=request.getServerName();
                /**
                 * 得到域名,
                 */
                Map tMap=new HashMap();
                tMap.put("domain",domain);
                Map rtnMap=sqlSession.selectOne("dpress.qsiteid",tMap);
                Template template= multiDomainFreeMarkerView.getTemplate(request,rtnMap.get("id"),"404");
                if(template!=null){
                    String content= multiDomainFreeMarkerView.getProcessedString(request,rtnMap.get("id"),"404",new HashMap());

                    return content;
                }

            } catch (java.lang.Throwable e) {
                e.printStackTrace();
            }

        } else if (status.equals(HttpStatus.NOT_FOUND)) {
            try {
                String domain=request.getServerName();
                /**
                 * 得到域名,
                 */
                Map tMap=new HashMap();
                tMap.put("domain",domain);
                Map rtnMap=sqlSession.selectOne("dpress.qsiteid",tMap);
                Template template= multiDomainFreeMarkerView.getTemplate(request,rtnMap.get("id"),"404");
                if(template!=null){
                   String content= multiDomainFreeMarkerView.getProcessedString(request,rtnMap.get("id"),"404",new HashMap());

                    return content;
                }

            } catch (java.lang.Throwable e) {
                e.printStackTrace();
            }
        }
        String content= defaultErrorContent(request,model.asMap());
        return content;

    }
    @Autowired
    private MultiDomainFreeMarkerView multiDomainFreeMarkerView;
    /**
     * Render 404 error page
     *
     * @return String
     */
    @GetMapping(value = "/404")
    public String contentNotFound() {
        if (themeService.templateExists(ERROR_TEMPLATE)) {
            return getActualTemplatePath(ERROR_TEMPLATE);
        }

        if (themeService.templateExists(NOT_FOUND_TEMPLATE)) {
            return getActualTemplatePath(NOT_FOUND_TEMPLATE);
        }

        return defaultErrorHandler();
    }

    /**
     * Render 500 error page
     *
     * @return template path:
     */
    @GetMapping(value = "/500")
    public String contentInternalError() {
        if (themeService.templateExists(ERROR_TEMPLATE)) {
            return getActualTemplatePath(ERROR_TEMPLATE);
        }

        if (themeService.templateExists(INTERNAL_ERROR_TEMPLATE)) {
            return getActualTemplatePath(INTERNAL_ERROR_TEMPLATE);
        }

        return defaultErrorHandler();
    }
    private String defaultErrorContent(HttpServletRequest request,Map params){
        try {
            String domain=request.getServerName();
            /**
             * 得到域名,
             */
            Map tMap=new HashMap();
            tMap.put("domain",domain);
            Map rtnMap=sqlSession.selectOne("dpress.qsiteid",tMap);
            Template template= multiDomainFreeMarkerView.getTemplate(request,rtnMap.get("id"),DEFAULT_ERROR_PATH);
            if(template!=null){
                String content= multiDomainFreeMarkerView.getProcessedString(request,rtnMap.get("id"),DEFAULT_ERROR_PATH,params);

                return content;
            }

        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    private String defaultErrorHandler() {


        return DEFAULT_ERROR_PATH;
    }

    private String getActualTemplatePath(@NonNull String template) {
        Assert.hasText(template, "FTL template must not be blank");

        StringBuilder path = new StringBuilder();
        path.append("themes/")
            .append(themeService.getActivatedTheme().getFolderName())
            .append('/')
            .append(FilenameUtils.getBasename(template));

        return path.toString();
    }

    /**
     * Handles custom exception, like HaloException.
     *
     * @param request http servlet request must not be null
     */
    private void handleCustomException(@NonNull HttpServletRequest request) {
        Assert.notNull(request, "Http servlet request must not be null");

        Object throwableObject = request.getAttribute("javax.servlet.error.exception");
        if (throwableObject == null) {
            return;
        }

        Throwable throwable = (Throwable) throwableObject;
        log.error("Captured an exception", throwable);

        if (throwable instanceof NestedServletException) {
            Throwable rootCause = ((NestedServletException) throwable).getRootCause();
            if (rootCause instanceof AbstractHaloException) {
                AbstractHaloException haloException = (AbstractHaloException) rootCause;
                request.setAttribute("javax.servlet.error.status_code", haloException.getStatus().value());
                request.setAttribute("javax.servlet.error.exception", rootCause);
                request.setAttribute("javax.servlet.error.message", haloException.getMessage());
            }
        } else if (StringUtils.startsWithIgnoreCase(throwable.getMessage(), COULD_NOT_RESOLVE_VIEW_WITH_NAME_PREFIX)) {
            request.setAttribute("javax.servlet.error.status_code", HttpStatus.NOT_FOUND.value());

            NotFoundException viewNotFound = new NotFoundException("该路径没有对应的模板");
            request.setAttribute("javax.servlet.error.exception", viewNotFound);
            request.setAttribute("javax.servlet.error.message", viewNotFound.getMessage());
        }

    }

    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request the source request
     * @return if the stacktrace attribute should be included
     */
    private boolean isIncludeStackTrace(HttpServletRequest request) {
        ErrorProperties.IncludeStacktrace include = errorProperties.getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }
}
