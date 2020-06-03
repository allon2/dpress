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
public class Links extends Model<Links> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private String description;

    private String logo;

    private String name;

    private Integer priority;

    private String team;

    private String url;

    private Long siteid;

    public Integer getId() {
        return id;
    }

    public Links setId(Integer id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Links setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Links setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getDescription() {
        return description;
    }

    public Links setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getLogo() {
        return logo;
    }

    public Links setLogo(String logo) {
        this.logo = logo;
        return this;
    }
    public String getName() {
        return name;
    }

    public Links setName(String name) {
        this.name = name;
        return this;
    }
    public Integer getPriority() {
        return priority;
    }

    public Links setPriority(Integer priority) {
        this.priority = priority;
        return this;
    }
    public String getTeam() {
        return team;
    }

    public Links setTeam(String team) {
        this.team = team;
        return this;
    }
    public String getUrl() {
        return url;
    }

    public Links setUrl(String url) {
        this.url = url;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public Links setSiteid(Long siteid) {
        this.siteid = siteid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Links{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", description=" + description +
            ", logo=" + logo +
            ", name=" + name +
            ", priority=" + priority +
            ", team=" + team +
            ", url=" + url +
            ", siteid=" + siteid +
        "}";
    }
}
