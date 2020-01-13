package dataFromServlet;

import logic.Repository;

public class RepositoryInformationForTable {

    private String m_RepositoryName;
    private String m_ActiveBranchName;
    private int m_NumberOfBranches;
    private String m_LastCommitDate;
    private String m_LastCommitMessage;
    private String  m_RepositoryLocation;


    public RepositoryInformationForTable(Repository i_RepositoryToConvert){
        m_RepositoryName = i_RepositoryToConvert.getM_Name();

        m_ActiveBranchName = i_RepositoryToConvert.getHeadBranch().getM_Name();
        m_NumberOfBranches = i_RepositoryToConvert.getM_Branches().size();
        m_LastCommitDate =  i_RepositoryToConvert.getLastCommit().getM_DateOfCreation();
        m_LastCommitMessage = i_RepositoryToConvert.getLastCommit().getM_Message();
        m_RepositoryLocation = i_RepositoryToConvert.getM_Location();
    }
}
