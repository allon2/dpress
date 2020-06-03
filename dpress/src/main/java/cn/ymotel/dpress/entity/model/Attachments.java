package cn.ymotel.dpress.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.sql.Blob;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author dpress
 * @since 2020-06-03
 */
public class Attachments extends Model<Attachments> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private String fileKey;

    private Integer height;

    private String mediaType;

    private String name;

    private String path;

    private Long size;

    private String suffix;

    private String thumbPath;

    private Integer type;

    private Integer width;

    private Long siteid;

    private Blob content;

    private Blob thumbnailcontent;

    public Integer getId() {
        return id;
    }

    public Attachments setId(Integer id) {
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
    public Integer getHeight() {
        return height;
    }

    public Attachments setHeight(Integer height) {
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
    public Long getSize() {
        return size;
    }

    public Attachments setSize(Long size) {
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
    public Integer getType() {
        return type;
    }

    public Attachments setType(Integer type) {
        this.type = type;
        return this;
    }
    public Integer getWidth() {
        return width;
    }

    public Attachments setWidth(Integer width) {
        this.width = width;
        return this;
    }
    public Long getSiteid() {
        return siteid;
    }

    public Attachments setSiteid(Long siteid) {
        this.siteid = siteid;
        return this;
    }
    public Blob getContent() {
        return content;
    }

    public Attachments setContent(Blob content) {
        this.content = content;
        return this;
    }
    public Blob getThumbnailcontent() {
        return thumbnailcontent;
    }

    public Attachments setThumbnailcontent(Blob thumbnailcontent) {
        this.thumbnailcontent = thumbnailcontent;
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
            ", siteid=" + siteid +
            ", content=" + content +
            ", thumbnailcontent=" + thumbnailcontent +
        "}";
    }
}
