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
public class Metas extends Model<Metas> {

    private static final long serialVersionUID = 1L;

    private Integer type;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private Date updateTime;

    private String metaKey;

    private Integer postId;

    private String metaValue;

    private Long siteid;

    public Integer getType() {
        return type;
    }

    public Metas setType(Integer type) {
        this.type = type;
        return this;
    }
    public Long getId() {
        return id;
    }

    public Metas setId(Long id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Metas setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Metas setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getMetaKey() {
        return metaKey;
    }

    public Metas setMetaKey(String metaKey) {
        this.metaKey = metaKey;
        return this;
    }
    public Integer getPostId() {
        return postId;
    }

    public Metas setPostId(Integer postId) {
        this.postId = postId;
        return this;
    }
    public String getMetaValue() {
        return metaValue;
    }

    public Metas setMetaValue(String metaValue) {
        this.metaValue = metaValue;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public Metas setSiteid(Long siteid) {
        this.siteid = siteid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Metas{" +
            "type=" + type +
            ", id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", metaKey=" + metaKey +
            ", postId=" + postId +
            ", metaValue=" + metaValue +
            ", siteid=" + siteid +
        "}";
    }
}
