package cn.ymotel.dpress.entity.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dpress
 * @since 2020-06-03
 */
public class FlywaySchemaHistory extends Model<FlywaySchemaHistory> {

    private static final long serialVersionUID = 1L;

    private Integer installedRank;

    private String version;

    private String description;

    private String type;

    private String script;

    private Integer checksum;

    private String installedBy;

    private Date installedOn;

    private Integer executionTime;

    private Boolean success;

    public Integer getInstalledRank() {
        return installedRank;
    }

    public FlywaySchemaHistory setInstalledRank(Integer installedRank) {
        this.installedRank = installedRank;
        return this;
    }
    public String getVersion() {
        return version;
    }

    public FlywaySchemaHistory setVersion(String version) {
        this.version = version;
        return this;
    }
    public String getDescription() {
        return description;
    }

    public FlywaySchemaHistory setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getType() {
        return type;
    }

    public FlywaySchemaHistory setType(String type) {
        this.type = type;
        return this;
    }
    public String getScript() {
        return script;
    }

    public FlywaySchemaHistory setScript(String script) {
        this.script = script;
        return this;
    }
    public Integer getChecksum() {
        return checksum;
    }

    public FlywaySchemaHistory setChecksum(Integer checksum) {
        this.checksum = checksum;
        return this;
    }
    public String getInstalledBy() {
        return installedBy;
    }

    public FlywaySchemaHistory setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
        return this;
    }
    public Date getInstalledOn() {
        return installedOn;
    }

    public FlywaySchemaHistory setInstalledOn(Date installedOn) {
        this.installedOn = installedOn;
        return this;
    }
    public Integer getExecutionTime() {
        return executionTime;
    }

    public FlywaySchemaHistory setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
        return this;
    }
    public Boolean getSuccess() {
        return success;
    }

    public FlywaySchemaHistory setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.installedRank;
    }

    @Override
    public String toString() {
        return "FlywaySchemaHistory{" +
            "installedRank=" + installedRank +
            ", version=" + version +
            ", description=" + description +
            ", type=" + type +
            ", script=" + script +
            ", checksum=" + checksum +
            ", installedBy=" + installedBy +
            ", installedOn=" + installedOn +
            ", executionTime=" + executionTime +
            ", success=" + success +
        "}";
    }
}
