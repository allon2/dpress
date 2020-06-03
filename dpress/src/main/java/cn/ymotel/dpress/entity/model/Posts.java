package cn.ymotel.dpress.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dpress
 * @since 2020-06-03
 */
public class Posts extends Model<Posts> {

    private static final long serialVersionUID = 1L;

    private Integer type;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private Boolean disallowComment;

    private Date editTime;

    private Integer editorType;

    private String formatContent;

    private Long likes;

    private String metaDescription;

    private String metaKeywords;

    private String originalContent;

    private String password;

    private String slug;

    private Integer status;

    private String summary;

    private String template;

    private String thumbnail;

    private String title;

    private Integer topPriority;

    private String url;

    private Long visits;

    private Long siteid;

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    @TableField(exist = false)
    private  String fullPath;
    public Integer getType() {
        return type;
    }

    public Posts setType(Integer type) {
        this.type = type;
        return this;
    }
    public Integer getId() {
        return id;
    }

    public Posts setId(Integer id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Posts setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Posts setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public Boolean getDisallowComment() {
        return disallowComment;
    }

    public Posts setDisallowComment(Boolean disallowComment) {
        this.disallowComment = disallowComment;
        return this;
    }
    public Date getEditTime() {
        return editTime;
    }

    public Posts setEditTime(Date editTime) {
        this.editTime = editTime;
        return this;
    }
    public Integer getEditorType() {
        return editorType;
    }

    public Posts setEditorType(Integer editorType) {
        this.editorType = editorType;
        return this;
    }
    public String getFormatContent() {
        return formatContent;
    }

    public Posts setFormatContent(String formatContent) {
        this.formatContent = formatContent;
        return this;
    }
    public Long getLikes() {
        return likes;
    }

    public Posts setLikes(Long likes) {
        this.likes = likes;
        return this;
    }
    public String getMetaDescription() {
        return metaDescription;
    }

    public Posts setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
        return this;
    }
    public String getMetaKeywords() {
        return metaKeywords;
    }

    public Posts setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
        return this;
    }
    public String getOriginalContent() {
        return originalContent;
    }

    public Posts setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
        return this;
    }
    public String getPassword() {
        return password;
    }

    public Posts setPassword(String password) {
        this.password = password;
        return this;
    }
    public String getSlug() {
        return slug;
    }

    public Posts setSlug(String slug) {
        this.slug = slug;
        return this;
    }
    public Integer getStatus() {
        return status;
    }

    public Posts setStatus(Integer status) {
        this.status = status;
        return this;
    }
    public String getSummary() {
        return summary;
    }

    public Posts setSummary(String summary) {
        this.summary = summary;
        return this;
    }
    public String getTemplate() {
        return template;
    }

    public Posts setTemplate(String template) {
        this.template = template;
        return this;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public Posts setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }
    public String getTitle() {
        return title;
    }

    public Posts setTitle(String title) {
        this.title = title;
        return this;
    }
    public Integer getTopPriority() {
        return topPriority;
    }

    public Posts setTopPriority(Integer topPriority) {
        this.topPriority = topPriority;
        return this;
    }
    public String getUrl() {
        return url;
    }

    public Posts setUrl(String url) {
        this.url = url;
        return this;
    }
    public Long getVisits() {
        return visits;
    }

    public Posts setVisits(Long visits) {
        this.visits = visits;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public Posts setSiteid(Long siteid) {
        this.siteid = siteid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Posts{" +
            "type=" + type +
            ", id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", disallowComment=" + disallowComment +
            ", editTime=" + editTime +
            ", editorType=" + editorType +
            ", formatContent=" + formatContent +
            ", likes=" + likes +
            ", metaDescription=" + metaDescription +
            ", metaKeywords=" + metaKeywords +
            ", originalContent=" + originalContent +
            ", password=" + password +
            ", slug=" + slug +
            ", status=" + status +
            ", summary=" + summary +
            ", template=" + template +
            ", thumbnail=" + thumbnail +
            ", title=" + title +
            ", topPriority=" + topPriority +
            ", url=" + url +
            ", visits=" + visits +
            ", siteid=" + siteid +
        "}";
    }
}
