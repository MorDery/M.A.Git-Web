package dataFromServlet;

import logic.Branch;
import logic.Commit;
import logic.Repository;
import logic.pullRequest.PullRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectedRepositoryData {
    private Repository m_SelectedRepository;
    private List<Branch> m_BranchesList = new ArrayList<>();
    private List<CommitInformation> m_CommitsInformationList = new ArrayList<>();
    private List<PullRequest> m_PullRequests = new ArrayList<>();

    public SelectedRepositoryData(Repository i_SelectedRepository) {
        this.m_SelectedRepository = i_SelectedRepository;
        List <Commit> CommitsOfHeadBranch = i_SelectedRepository.getCommitsOfHeadBranch();

        for(Map.Entry<String,Branch> entryBranch : i_SelectedRepository.getM_Branches().entrySet()){
            m_BranchesList.add(entryBranch.getValue());
        }

        for(PullRequest pullRequest : m_SelectedRepository.getM_PullRequests()){
            m_PullRequests.add(pullRequest);
        }
        for(Commit commit : CommitsOfHeadBranch){
            m_CommitsInformationList.add(new CommitInformation(i_SelectedRepository, commit));
        }
    }
}
