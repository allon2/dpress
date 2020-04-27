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
public class Journals extends Model<Journals> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String content;

    private String likes;

    private String sourceContent;

    private String type;

    public String getId() {
        return id;
    }

    public Journals setId(String id) {
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
    public String getLikes() {
        return likes;
    }

    public Journals setLikes(String likes) {
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
    public String getType() {
        return type;
    }

    public Journals setType(String type) {
        this.type = type;
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
        "}";
    }
}
