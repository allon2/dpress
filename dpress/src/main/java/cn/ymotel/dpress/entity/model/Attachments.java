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
public class Attachments extends Model<Attachments> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private Date createTime;

    private Date updateTime;

    private String fileKey;

    private String height;

    private String mediaType;

    private String name;

    private String path;

    private String size;

    private String suffix;

    private String thumbPath;

    private String type;

    private String width;

    public String getId() {
        return id;
    }

    public Attachments setId(String id) {
        this.id = id;
        return this;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public Attachments setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public Attachments setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
    public String getFileKey() {
        return fileKey;
    }

    public Attachments setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }
    public String getHeight() {
        return height;
    }

    public Attachments setHeight(String height) {
        this.height = height;
        return this;
    }
    public String getMediaType() {
        return mediaType;
    }

    public Attachments setMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }
    public String getName() {
        return name;
    }

    public Attachments setName(String name) {
        this.name = name;
        return this;
    }
    public String getPath() {
        return path;
    }

    public Attachments setPath(String path) {
        this.path = path;
        return this;
    }
    public String getSize() {
        return size;
    }

    public Attachments setSize(String size) {
        this.size = size;
        return this;
    }
    public String getSuffix() {
        return suffix;
    }

    public Attachments setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }
    public String getThumbPath() {
        return thumbPath;
    }

    public Attachments setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
        return this;
    }
    public String getType() {
        return type;
    }

    public Attachments setType(String type) {
        this.type = type;
        return this;
    }
    public String getWidth() {
        return width;
    }

    public Attachments setWidth(String width) {
        this.width = width;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Attachments{" +
            "id=" + id +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", fileKey=" + fileKey +
            ", height=" + height +
            ", mediaType=" + mediaType +
            ", name=" + name +
            ", path=" + path +
            ", size=" + size +
            ", suffix=" + suffix +
            ", thumbPath=" + thumbPath +
            ", type=" + type +
            ", width=" + width +
        "}";
    }
}
