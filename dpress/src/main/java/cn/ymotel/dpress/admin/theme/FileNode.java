package cn.ymotel.dpress.admin.theme;

import java.util.ArrayList;
import java.util.List;

public class FileNode {
    private String path;
   private  List<FileNode> childs=new ArrayList();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FileNode> getChilds() {
        return childs;
    }

    @Override
    public String toString() {
        return "FileNode{" +
                "path='" + path + '\'' +
                ", childs=" + childs +
                '}';
    }

    public void setChilds(List<FileNode> childs) {
        this.childs = childs;
    }
}
