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
public class Journals extends Model<Journals> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private String content;

    private Long likes;

    private String sourceContent;

    private Integer type;

    private Long siteid;

    public Integer getId() {
        return id;
    }

    public Journals setId(Integer id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Journals setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Journals setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getContent() {
        return content;
    }

    public Journals setContent(String content) {
        this.content = content;
        return this;
    }
    public Long getLikes() {
        return likes;
    }

    public Journals setLikes(Long likes) {
        this.likes = likes;
        return this;
    }
    public String getSourceContent() {
        return sourceContent;
    }

    public Journals setSourceContent(String sourceContent) {
        this.sourceContent = sourceContent;
        return this;
    }
    public Integer getType() {
        return type;
    }

    public Journals setType(Integer type) {
        this.type = type;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public Journals setSiteid(Long siteid) {
        this.siteid = siteid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Journals{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", content=" + content +
            ", likes=" + likes +
            ", sourceContent=" + sourceContent +
            ", type=" + type +
            ", siteid=" + siteid +
        "}";
    }
}
