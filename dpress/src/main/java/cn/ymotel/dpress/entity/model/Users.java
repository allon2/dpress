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
public class Users extends Model<Users> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private String avatar;

    private String description;

    private String email;

    private Date expireTime;

    private String nickname;

    private String password;

    private String username;

    private Long siteid;

    public Integer getId() {
        return id;
    }

    public Users setId(Integer id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Users setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Users setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getAvatar() {
        return avatar;
    }

    public Users setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
    public String getDescription() {
        return description;
    }

    public Users setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getEmail() {
        return email;
    }

    public Users setEmail(String email) {
        this.email = email;
        return this;
    }
    public Date getExpireTime() {
        return expireTime;
    }

    public Users setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
        return this;
    }
    public String getNickname() {
        return nickname;
    }

    public Users setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
    public String getPassword() {
        return password;
    }

    public Users setPassword(String password) {
        this.password = password;
        return this;
    }
    public String getUsername() {
        return username;
    }

    public Users setUsername(String username) {
        this.username = username;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public Users setSiteid(Long siteid) {
        this.siteid = siteid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Users{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", avatar=" + avatar +
            ", description=" + description +
            ", email=" + email +
            ", expireTime=" + expireTime +
            ", nickname=" + nickname +
            ", password=" + password +
            ", username=" + username +
            ", siteid=" + siteid +
        "}";
    }
}
