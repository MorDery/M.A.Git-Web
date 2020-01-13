package logic;

public class FileFullPathAndItemData {
    private String m_FullPath;
    private Folder.Item m_ItemData;
    private String m_Content;

    public FileFullPathAndItemData(String i_FullPath, Folder.Item i_ItemData) {
        m_ItemData = i_ItemData;
        m_FullPath = i_FullPath;
    }

    public String getContent() {
        return m_Content;
    }

    public String getFullPath() {
        return m_FullPath;
    }

    public Folder.Item getItemData() {
        return m_ItemData;
    }

    public void setContent(String m_Content) {
        this.m_Content = m_Content;
    }
}
