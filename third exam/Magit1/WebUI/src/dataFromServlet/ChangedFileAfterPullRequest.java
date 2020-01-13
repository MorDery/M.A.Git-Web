package dataFromServlet;

import logic.FileFullPathAndItemData;
import java.util.List;

public class ChangedFileAfterPullRequest {
    private List<FileFullPathAndItemData> m_AddedFiles;
    private List<FileFullPathAndItemData> m_DeletedFiles;
    private List<FileFullPathAndItemData> m_UpdatedFiles;

    public ChangedFileAfterPullRequest(List<FileFullPathAndItemData> m_AddedFiles, List<FileFullPathAndItemData> m_DeletedFiles, List<FileFullPathAndItemData> m_UpdatedFiles) {
        this.m_AddedFiles = m_AddedFiles;
        this.m_DeletedFiles = m_DeletedFiles;
        this.m_UpdatedFiles = m_UpdatedFiles;
    }

    public List<FileFullPathAndItemData> getAddedFiles() {
        return m_AddedFiles;
    }

    public List<FileFullPathAndItemData> getDeletedFiles() {
        return m_DeletedFiles;
    }

    public List<FileFullPathAndItemData> getUpdatedFiles() {
        return m_UpdatedFiles;
    }
}
