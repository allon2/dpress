package cn.ymotel.dpress.entity.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dpress
 * @since 2020-06-03
 */
public class DpressTemplate extends Model<DpressTemplate> {

    private static final long serialVersionUID = 1L;

    /**
     * json格式，key为返回值 value为执行方法
     */
    private String variable;

    private String content;

    private Blob bcontent;

    @TableField("lastModified")
    private Date lastModified;

    private Long siteid;

    private String encoding;

    private String mediatype;

    private String path;

    private Long cifseq;

    private String theme;

    public String getVariable() {
        return variable;
    }

    public DpressTemplate setVariable(String variable) {
        this.variable = variable;
        return this;
    }
    public String getContent() {
        return content;
    }

    public DpressTemplate setContent(String content) {
        this.content = content;
        return this;
    }
    public Blob getBcontent() {
        return bcontent;
    }

    public DpressTemplate setBcontent(Blob bcontent) {
        this.bcontent = bcontent;
        return this;
    }
    public Date getLastModified() {
        return lastModified;
    }

    public DpressTemplate setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public DpressTemplate setSiteid(Long siteid) {
        this.siteid = siteid;
        return this;
    }
    public String getEncoding() {
        return encoding;
    }

    public DpressTemplate setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }
    public String getMediatype() {
        return mediatype;
    }

    public DpressTemplate setMediatype(String mediatype) {
        this.mediatype = mediatype;
        return this;
    }
    public String getPath() {
        return path;
    }

    public DpressTemplate setPath(String path) {
        this.path = path;
        return this;
    }
    public Long getCifseq() {
        return cifseq;
    }

    public DpressTemplate setCifseq(Long cifseq) {
        this.cifseq = cifseq;
        return this;
    }
    public String getTheme() {
        return theme;
    }

    public DpressTemplate setTheme(String theme) {
        this.theme = theme;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DpressTemplate{" +
            "variable=" + variable +
            ", content=" + content +
            ", bcontent=" + bcontent +
            ", lastModified=" + lastModified +
            ", siteid=" + siteid +
            ", encoding=" + encoding +
            ", mediatype=" + mediatype +
            ", path=" + path +
            ", cifseq=" + cifseq +
            ", theme=" + theme +
        "}";
    }
}
