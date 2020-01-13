package dataFromServlet;

import java.util.List;

public class ChangedInformationAfterPullRequest {
    private List<String> m_CommitsInfo;
    private List<String> m_CommitsSHA1List;

    public ChangedInformationAfterPullRequest(List<String> i_CommitsInfo, List<String> i_CommitsList){
        m_CommitsInfo = i_CommitsInfo;
        m_CommitsSHA1List = i_CommitsList;
    }

    public List<String> getCommitsInfo() {
        return m_CommitsInfo;
    }

    public List<String> getCommitsSHA1List() {
        return m_CommitsSHA1List;
    }
}