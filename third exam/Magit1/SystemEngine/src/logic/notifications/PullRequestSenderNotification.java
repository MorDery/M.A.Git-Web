package logic.notifications;

import logic.pullRequest.PullRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PullRequestSenderNotification extends Notification {
    private String m_RepositoryName;
    private String m_SenderUserName;
    private String m_BaseBranchName;
    private String m_TargetBranchName;
    private String m_Message;

    public PullRequestSenderNotification(String i_RepositoryName, PullRequest i_SenderPullRequest) {
        m_RepositoryName = i_RepositoryName;
        m_SenderUserName = i_SenderPullRequest.getUserNameOfRequester();
        m_BaseBranchName = i_SenderPullRequest.getBaseBranchName();
        m_TargetBranchName = i_SenderPullRequest.getTargetBranchName();
        m_Message = i_SenderPullRequest.getMessage();
        m_IsShownOnSecondPage = false;
        m_IsShownOnThirdPage = false;
        m_TimeStamp = (new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss:SSS").format(new Date()));
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pull request was received on time: ");
        stringBuilder.append(m_TimeStamp);
        stringBuilder.append(", Repository name: ");
        stringBuilder.append(m_RepositoryName);
        stringBuilder.append(", Name of sending user: ");
        stringBuilder.append(m_SenderUserName);
        stringBuilder.append(", Message: ");
        stringBuilder.append(m_Message);
        stringBuilder.append(", Name of target branch: ");
        stringBuilder.append(m_TargetBranchName);
        stringBuilder.append(", Name of base branch: ");
        stringBuilder.append(m_BaseBranchName);
        return stringBuilder.toString();
    }
}
