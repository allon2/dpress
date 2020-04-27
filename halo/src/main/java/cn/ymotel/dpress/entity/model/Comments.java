package cn.ymotel.dpress.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-03-18
 */
public class Comments extends Model<Comments> {

    private static final long serialVersionUID = 1L;

    private String type;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String allowNotification;

    private String author;

    private String authorUrl;

    private String content;

    private String email;

    private String gravatarMd5;

    private String ipAddress;

    private String isAdmin;

    private String parentId;

    private String postId;

    private String status;

    private String topPriority;

    private String userAgent;

    public String getType() {
        return type;
    }

    public Comments setType(String type) {
        this.type = type;
        return this;
    }
    public String getId() {
        return id;
    }

    public Comments setId(String id) {
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
    public String getAllowNotification() {
        return allowNotification;
    }

    public Comments setAllowNotification(String allowNotification) {
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
    public String getIsAdmin() {
        return isAdmin;
    }

    public Comments setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }
    public String getParentId() {
        return parentId;
    }

    public Comments setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }
    public String getPostId() {
        return postId;
    }

    public Comments setPostId(String postId) {
        this.postId = postId;
        return this;
    }
    public String getStatus() {
        return status;
    }

    public Comments setStatus(String status) {
        this.status = status;
        return this;
    }
    public String getTopPriority() {
        return topPriority;
    }

    public Comments setTopPriority(String topPriority) {
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
        "}";
    }
}
