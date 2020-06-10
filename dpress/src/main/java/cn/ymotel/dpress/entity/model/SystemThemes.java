package cn.ymotel.dpress.entity.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dpress
 * @since 2020-06-09
 */
public class SystemThemes extends Model<SystemThemes> {

    private static final long serialVersionUID = 1L;

    /**
     * json格式，key为返回值 value为执行方法
     */
    private String variable;

    private String content;

    private byte[] bcontent;

    private String encoding;

    private String mediatype;

    private String path;

    private String theme;

    public String getVariable() {
        return variable;
    }

    public SystemThemes setVariable(String variable) {
        this.variable = variable;
        return this;
    }
    public String getContent() {
        return content;
    }

    public SystemThemes setContent(String content) {
        this.content = content;
        return this;
    }
    public byte[] getBcontent() {
        return bcontent;
    }

    public SystemThemes setBcontent(byte[] bcontent) {
        this.bcontent = bcontent;
        return this;
    }
    public String getEncoding() {
        return encoding;
    }

    public SystemThemes setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }
    public String getMediatype() {
        return mediatype;
    }

    public SystemThemes setMediatype(String mediatype) {
        this.mediatype = mediatype;
        return this;
    }
    public String getPath() {
        return path;
    }

    public SystemThemes setPath(String path) {
        this.path = path;
        return this;
    }
    public String getTheme() {
        return theme;
    }

    public SystemThemes setTheme(String theme) {
        this.theme = theme;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "SystemThemes{" +
            "variable=" + variable +
            ", content=" + content +
            ", bcontent=" + bcontent +
            ", encoding=" + encoding +
            ", mediatype=" + mediatype +
            ", path=" + path +
            ", theme=" + theme +
        "}";
    }
}
