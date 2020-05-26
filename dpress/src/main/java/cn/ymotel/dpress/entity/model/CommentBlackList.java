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
public class CommentBlackList extends Model<CommentBlackList> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private Date banTime;

    private String ipAddress;

    public String getId() {
        return id;
    }

    public CommentBlackList setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public CommentBlackList setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public CommentBlackList setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public Date getBanTime() {
        return banTime;
    }

    public CommentBlackList setBanTime(Date banTime) {
        this.banTime = banTime;
        return this;
    }
    public String getIpAddress() {
        return ipAddress;
    }

    public CommentBlackList setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CommentBlackList{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", banTime=" + banTime +
            ", ipAddress=" + ipAddress +
        "}";
    }
}
