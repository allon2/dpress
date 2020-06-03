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
public class Comments extends Model<Comments> {

    private static final long serialVersionUID = 1L;

    private Integer type;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Boolean allowNotification;

    private String author;

    private String authorUrl;

    private String content;

    private String email;

    private String gravatarMd5;

    private String ipAddress;

    private Boolean isAdmin;

    private Long parentId;

    private Integer postId;

    private Integer status;

    private Integer topPriority;

    private String userAgent;

    private Long siteid;
    @TableField(exist = false)
    public String fullPath;

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Integer getType() {
        return type;
    }

    public Comments setType(Integer type) {
        this.type = type;
        return this;
    }
    public Long getId() {
        return id;
    }

    public Comments setId(Long id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Comments setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Comments setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public Boolean getAllowNotification() {
        return allowNotification;
    }

    public Comments setAllowNotification(Boolean allowNotification) {
        this.allowNotification = allowNotification;
        return this;
    }
    public String getAuthor() {
        return author;
    }

    public Comments setAuthor(String author) {
        this.author = author;
        return this;
    }
    public String getAuthorUrl() {
        return authorUrl;
    }

    public Comments setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }
    public String getContent() {
        return content;
    }

    public Comments setContent(String content) {
        this.content = content;
        return this;
    }
    public String getEmail() {
        return email;
    }

    public Comments setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getGravatarMd5() {
        return gravatarMd5;
    }

    public Comments setGravatarMd5(String gravatarMd5) {
        this.gravatarMd5 = gravatarMd5;
        return this;
    }
    public String getIpAddress() {
        return ipAddress;
    }

    public Comments setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }
    public Boolean getAdmin() {
        return isAdmin;
    }

    public Comments setAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }
    public Long getParentId() {
        return parentId;
    }

    public Comments setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }
    public Integer getPostId() {
        return postId;
    }

    public Comments setPostId(Integer postId) {
        this.postId = postId;
        return this;
    }
    public Integer getStatus() {
        return status;
    }

    public Comments setStatus(Integer status) {
        this.status = status;
        return this;
    }
    public Integer getTopPriority() {
        return topPriority;
    }

    public Comments setTopPriority(Integer topPriority) {
        this.topPriority = topPriority;
        return this;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public Comments setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public Comments setSiteid(Long siteid) {
        this.siteid = siteid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Comments{" +
            "type=" + type +
            ", id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", allowNotification=" + allowNotification +
            ", author=" + author +
            ", authorUrl=" + authorUrl +
            ", content=" + content +
            ", email=" + email +
            ", gravatarMd5=" + gravatarMd5 +
            ", ipAddress=" + ipAddress +
            ", isAdmin=" + isAdmin +
            ", parentId=" + parentId +
            ", postId=" + postId +
            ", status=" + status +
            ", topPriority=" + topPriority +
            ", userAgent=" + userAgent +
            ", siteid=" + siteid +
        "}";
    }
}
