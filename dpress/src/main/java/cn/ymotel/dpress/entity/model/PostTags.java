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
 * @since 2020-06-03
 */
public class PostTags extends Model<PostTags> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private Integer postId;

    private Integer tagId;

    private Long siteid;

    public Integer getId() {
        return id;
    }

    public PostTags setId(Integer id) {
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
    public Integer getPostId() {
        return postId;
    }

    public PostTags setPostId(Integer postId) {
        this.postId = postId;
        return this;
    }
    public Integer getTagId() {
        return tagId;
    }

    public PostTags setTagId(Integer tagId) {
        this.tagId = tagId;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public PostTags setSiteid(Long siteid) {
        this.siteid = siteid;
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
            ", siteid=" + siteid +
        "}";
    }
}
