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
public class Categories extends Model<Categories> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String description;

    private String name;

    private String parentId;

    private String slug;

    private String slugName;

    private String thumbnail;

    public String getId() {
        return id;
    }

    public Categories setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Categories setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Categories setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getDescription() {
        return description;
    }

    public Categories setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getName() {
        return name;
    }

    public Categories setName(String name) {
        this.name = name;
        return this;
    }
    public String getParentId() {
        return parentId;
    }

    public Categories setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }
    public String getSlug() {
        return slug;
    }

    public Categories setSlug(String slug) {
        this.slug = slug;
        return this;
    }
    public String getSlugName() {
        return slugName;
    }

    public Categories setSlugName(String slugName) {
        this.slugName = slugName;
        return this;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public Categories setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Categories{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", description=" + description +
            ", name=" + name +
            ", parentId=" + parentId +
            ", slug=" + slug +
            ", slugName=" + slugName +
            ", thumbnail=" + thumbnail +
        "}";
    }
}
