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
public class Tags extends Model<Tags> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String name;

    private String slug;

    private String slugName;

    private String thumbnail;

    public String getId() {
        return id;
    }

    public Tags setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Tags setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Tags setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getName() {
        return name;
    }

    public Tags setName(String name) {
        this.name = name;
        return this;
    }
    public String getSlug() {
        return slug;
    }

    public Tags setSlug(String slug) {
        this.slug = slug;
        return this;
    }
    public String getSlugName() {
        return slugName;
    }

    public Tags setSlugName(String slugName) {
        this.slugName = slugName;
        return this;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public Tags setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Tags{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", name=" + name +
            ", slug=" + slug +
            ", slugName=" + slugName +
            ", thumbnail=" + thumbnail +
        "}";
    }
}
