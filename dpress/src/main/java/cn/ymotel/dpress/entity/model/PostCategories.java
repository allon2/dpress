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
public class PostCategories extends Model<PostCategories> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String categoryId;

    private String postId;

    public String getId() {
        return id;
    }

    public PostCategories setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public PostCategories setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public PostCategories setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getCategoryId() {
        return categoryId;
    }

    public PostCategories setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }
    public String getPostId() {
        return postId;
    }

    public PostCategories setPostId(String postId) {
        this.postId = postId;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PostCategories{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", categoryId=" + categoryId +
            ", postId=" + postId +
        "}";
    }
}
