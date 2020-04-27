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
public class Options extends Model<Options> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String optionKey;

    private String type;

    private String optionValue;

    public String getId() {
        return id;
    }

    public Options setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Options setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Options setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getOptionKey() {
        return optionKey;
    }

    public Options setOptionKey(String optionKey) {
        this.optionKey = optionKey;
        return this;
    }
    public String getType() {
        return type;
    }

    public Options setType(String type) {
        this.type = type;
        return this;
    }
    public String getOptionValue() {
        return optionValue;
    }

    public Options setOptionValue(String optionValue) {
        this.optionValue = optionValue;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Options{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", optionKey=" + optionKey +
            ", type=" + type +
            ", optionValue=" + optionValue +
        "}";
    }
}
