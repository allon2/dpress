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
public class Photos extends Model<Photos> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String description;

    private String location;

    private String name;

    private Date takeTime;

    private String team;

    private String thumbnail;

    private String url;

    public String getId() {
        return id;
    }

    public Photos setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Photos setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Photos setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getDescription() {
        return description;
    }

    public Photos setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getLocation() {
        return location;
    }

    public Photos setLocation(String location) {
        this.location = location;
        return this;
    }
    public String getName() {
        return name;
    }

    public Photos setName(String name) {
        this.name = name;
        return this;
    }
    public Date getTakeTime() {
        return takeTime;
    }

    public Photos setTakeTime(Date takeTime) {
        this.takeTime = takeTime;
        return this;
    }
    public String getTeam() {
        return team;
    }

    public Photos setTeam(String team) {
        this.team = team;
        return this;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public Photos setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }
    public String getUrl() {
        return url;
    }

    public Photos setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Photos{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", description=" + description +
            ", location=" + location +
            ", name=" + name +
            ", takeTime=" + takeTime +
            ", team=" + team +
            ", thumbnail=" + thumbnail +
            ", url=" + url +
        "}";
    }
}
