package cn.ymotel.dpress.actor;

public class ViewData {
    public static String TYPE_FTL="ftl";
    private Object data;
    private Object viewName;
    private String type;
    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getViewName() {
        return viewName;
    }

    public void setViewName(Object viewName) {
        this.viewName = viewName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
