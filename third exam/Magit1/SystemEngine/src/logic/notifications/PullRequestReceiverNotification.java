package logic.notifications;

import logic.pullRequest.PullRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PullRequestReceiverNotification extends Notification {
    private String m_RepositoryName;
    private String m_SenderUserName;
    private String m_BaseBranchName;
    private String m_TargetBranchName;
    private String m_CancelMessage;

    public PullRequestReceiverNotification(String i_RepositoryName, PullRequest i_SenderPullRequest) {
        m_RepositoryName = i_RepositoryName;
        m_SenderUserName = i_SenderPullRequest.getUserNameOfRequester();
        m_BaseBranchName = i_SenderPullRequest.getBaseBranchName();
        m_TargetBranchName = i_SenderPullRequest.getTargetBranchName();
        m_IsShownOnSecondPage = false;
        m_IsShownOnThirdPage = false;
        m_TimeStamp = (new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss:SSS").format(new Date()));
    }

    public void setCancelMessage(String m_CancelMessage) {
        this.m_CancelMessage = m_CancelMessage;
    }

    @Override
    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();
        if(m_CancelMessage !=null) {
            stringBuilder.append("Pull request was Canceled on time: ");
        }
        else{
            stringBuilder.append("Pull request was Accepted on time: ");
        }
        stringBuilder.append(m_TimeStamp);
        stringBuilder.append(", Repository name: ");
        stringBuilder.append(m_RepositoryName);
        stringBuilder.append(", Name of sending user: ");
        stringBuilder.append(m_SenderUserName);

        if(m_CancelMessage != null) {
            stringBuilder.append(", Reason for cancel: ");
            stringBuilder.append(m_CancelMessage);
        }

        stringBuilder.append(", Name of target branch: ");
        stringBuilder.append(m_TargetBranchName);
        stringBuilder.append(", Name of base branch: ");
        stringBuilder.append(m_BaseBranchName);
        return stringBuilder.toString();
    }

}
