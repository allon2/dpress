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
public class CommentBlackList extends Model<CommentBlackList> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Date banTime;

    private String ipAddress;

    private Long siteid;

    public Long getId() {
        return id;
    }

    public CommentBlackList setId(Long id) {
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
    public Long getSiteid() {
        return siteid;
    }

    public CommentBlackList setSiteid(Long siteid) {
        this.siteid = siteid;
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
            ", siteid=" + siteid +
        "}";
    }
}
