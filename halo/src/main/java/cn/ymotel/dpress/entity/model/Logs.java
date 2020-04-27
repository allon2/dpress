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
public class Logs extends Model<Logs> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String content;

    private String ipAddress;

    private String logKey;

    private String type;

    public String getId() {
        return id;
    }

    public Logs setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Logs setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Logs setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getContent() {
        return content;
    }

    public Logs setContent(String content) {
        this.content = content;
        return this;
    }
    public String getIpAddress() {
        return ipAddress;
    }

    public Logs setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }
    public String getLogKey() {
        return logKey;
    }

    public Logs setLogKey(String logKey) {
        this.logKey = logKey;
        return this;
    }
    public String getType() {
        return type;
    }

    public Logs setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Logs{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", content=" + content +
            ", ipAddress=" + ipAddress +
            ", logKey=" + logKey +
            ", type=" + type +
        "}";
    }
}
