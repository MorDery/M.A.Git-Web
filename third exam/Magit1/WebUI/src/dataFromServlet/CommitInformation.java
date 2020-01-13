package dataFromServlet;

import logic.Commit;
import logic.Repository;
import java.util.List;

public class CommitInformation {
    private String m_CommitSha1;
    private String m_Message;
    private String m_Author;
    private String m_DateOfCreation;
    private List<String> m_PointedBranches;
    private String m_CommitText;


    public CommitInformation(Repository i_Repository, Commit i_Commit){
        m_CommitSha1 = i_Repository.getCommitSha1(i_Commit);
        m_Message = i_Commit.getM_Message();
        m_Author = i_Commit.getM_Author();
        m_DateOfCreation = i_Commit.getM_DateOfCreation();
        m_PointedBranches = i_Repository.getListOfPointedBranchesOnGivenCommitSha1(m_CommitSha1);
        m_CommitText = "SHA-1: " + m_CommitSha1 + ", Message: " + m_Message + ", Author: " + m_Author
                + ", Date of creation: " + m_DateOfCreation + ", Pointed branches: " + getPointedBranchesAsString(m_PointedBranches);
    }

    private String getPointedBranchesAsString(List<String> m_PointedBranches){
        StringBuilder result = new StringBuilder();

        for(String branchName : m_PointedBranches){
            result.append(branchName);
            result.append(", ");
        }

        if(m_PointedBranches.size() > 0) {
            result.delete(result.length() - 2, result.length());
        }
        else{
            result.append("None");
        }

        return result.toString();
    }
}
