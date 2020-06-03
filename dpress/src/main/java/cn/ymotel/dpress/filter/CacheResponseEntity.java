package cn.ymotel.dpress.filter;

import org.springframework.util.FastByteArrayOutputStream;

import java.io.Serializable;

public class CacheResponseEntity  implements Serializable {
    private byte[] content;
    private String contentType;
   private String encoding;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
