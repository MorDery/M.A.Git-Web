package logic;

import java.util.ArrayList;
import java.util.List;

public class ViewMagitFileNode {

    private String m_Content;
    private  String m_Name;
    private List<ViewMagitFileNode> m_Childrens = new ArrayList<>();
    private Boolean m_IsFolder;
    private String m_Path;

    public ViewMagitFileNode(String content, String name, Boolean i_IsFolder, String i_Path ){
        m_Content=content;
        m_Name =name;
        m_IsFolder = i_IsFolder;
        m_Path = i_Path;
    }

    public String getPath() {
        return m_Path;
    }

    public List<ViewMagitFileNode> getChildrens() {
        return m_Childrens;
    }

    public Boolean getIsFolder() {
        return m_IsFolder;
    }

    public String getContent() {
        return m_Content;
    }

    public String getName() {
        return m_Name;
    }

    @Override
    public String toString() {
        return m_Name;
    }
}
