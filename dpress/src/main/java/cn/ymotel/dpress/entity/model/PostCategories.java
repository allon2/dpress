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
public class PostCategories extends Model<PostCategories> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private Integer categoryId;

    private Integer postId;

    private Long siteid;

    public Integer getId() {
        return id;
    }

    public PostCategories setId(Integer id) {
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
    public Integer getCategoryId() {
        return categoryId;
    }

    public PostCategories setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }
    public Integer getPostId() {
        return postId;
    }

    public PostCategories setPostId(Integer postId) {
        this.postId = postId;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public PostCategories setSiteid(Long siteid) {
        this.siteid = siteid;
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
            ", siteid=" + siteid +
        "}";
    }
}
