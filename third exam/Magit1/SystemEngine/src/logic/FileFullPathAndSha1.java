package logic;

public class FileFullPathAndSha1 {
    private String m_FullPath;
    private String m_Sha1;
    private Folder.Item m_ItemData;

    public FileFullPathAndSha1(String i_FullPath, String i_Sha1 ,Folder.Item i_ItemData) {
        m_ItemData = i_ItemData;
        m_FullPath = i_FullPath;
        m_Sha1 = i_Sha1;
    }

    public String getFullPath() {
        return m_FullPath;
    }

    public String getSha1() {
        return m_Sha1;
    }

    public Folder.Item getItemData() {
        return m_ItemData;
    }
}
