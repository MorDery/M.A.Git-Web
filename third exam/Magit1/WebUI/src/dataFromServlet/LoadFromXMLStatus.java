package dataFromServlet;

import logic.MAGit;
import java.util.List;

public class LoadFromXMLStatus {
    private boolean m_IsValid;
    boolean m_IsRepositoryAlreadyExists;

    public LoadFromXMLStatus(boolean m_IsValid, List<String> m_Errors, MAGit i_Magit) {
        this.m_IsValid = m_IsValid;
        m_IsRepositoryAlreadyExists = i_Magit.isRepositoryFileAlreadyExists();
    }
}
