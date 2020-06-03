package cn.ymotel.dpress.entity.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dpress
 * @since 2020-06-03
 */
public class DpressSiteinfo extends Model<DpressSiteinfo> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String domain;

    private String sitename;

    /**
     * 0:default 1非默认值
     */
    @TableField("isDefault")
    private String isDefault;

    public Long getId() {
        return id;
    }

    public DpressSiteinfo setId(Long id) {
        this.id = id;
        return this;
    }
    public String getDomain() {
        return domain;
    }

    public DpressSiteinfo setDomain(String domain) {
        this.domain = domain;
        return this;
    }
    public String getSitename() {
        return sitename;
    }

    public DpressSiteinfo setSitename(String sitename) {
        this.sitename = sitename;
        return this;
    }
    public String getIsDefault() {
        return isDefault;
    }

    public DpressSiteinfo setIsDefault(String isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "DpressSiteinfo{" +
            "id=" + id +
            ", domain=" + domain +
            ", sitename=" + sitename +
            ", isDefault=" + isDefault +
        "}";
    }
}
