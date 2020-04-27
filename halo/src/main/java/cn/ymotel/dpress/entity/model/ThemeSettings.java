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
public class ThemeSettings extends Model<ThemeSettings> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String settingKey;

    private String themeId;

    private String settingValue;

    public String getId() {
        return id;
    }

    public ThemeSettings setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public ThemeSettings setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public ThemeSettings setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getSettingKey() {
        return settingKey;
    }

    public ThemeSettings setSettingKey(String settingKey) {
        this.settingKey = settingKey;
        return this;
    }
    public String getThemeId() {
        return themeId;
    }

    public ThemeSettings setThemeId(String themeId) {
        this.themeId = themeId;
        return this;
    }
    public String getSettingValue() {
        return settingValue;
    }

    public ThemeSettings setSettingValue(String settingValue) {
        this.settingValue = settingValue;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ThemeSettings{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", settingKey=" + settingKey +
            ", themeId=" + themeId +
            ", settingValue=" + settingValue +
        "}";
    }
}
