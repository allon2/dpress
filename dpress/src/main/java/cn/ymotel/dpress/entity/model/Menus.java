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
public class Menus extends Model<Menus> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String icon;

    private String name;

    private String parentId;

    private String priority;

    private String target;

    private String team;

    private String url;

    public String getId() {
        return id;
    }

    public Menus setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Menus setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Menus setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getIcon() {
        return icon;
    }

    public Menus setIcon(String icon) {
        this.icon = icon;
        return this;
    }
    public String getName() {
        return name;
    }

    public Menus setName(String name) {
        this.name = name;
        return this;
    }
    public String getParentId() {
        return parentId;
    }

    public Menus setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }
    public String getPriority() {
        return priority;
    }

    public Menus setPriority(String priority) {
        this.priority = priority;
        return this;
    }
    public String getTarget() {
        return target;
    }

    public Menus setTarget(String target) {
        this.target = target;
        return this;
    }
    public String getTeam() {
        return team;
    }

    public Menus setTeam(String team) {
        this.team = team;
        return this;
    }
    public String getUrl() {
        return url;
    }

    public Menus setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Menus{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", icon=" + icon +
            ", name=" + name +
            ", parentId=" + parentId +
            ", priority=" + priority +
            ", target=" + target +
            ", team=" + team +
            ", url=" + url +
        "}";
    }
}
