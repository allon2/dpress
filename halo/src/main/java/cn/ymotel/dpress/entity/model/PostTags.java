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
public class PostTags extends Model<PostTags> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String postId;

    private String tagId;

    public String getId() {
        return id;
    }

    public PostTags setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public PostTags setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public PostTags setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getPostId() {
        return postId;
    }

    public PostTags setPostId(String postId) {
        this.postId = postId;
        return this;
    }
    public String getTagId() {
        return tagId;
    }

    public PostTags setTagId(String tagId) {
        this.tagId = tagId;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PostTags{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", postId=" + postId +
            ", tagId=" + tagId +
        "}";
    }
}
