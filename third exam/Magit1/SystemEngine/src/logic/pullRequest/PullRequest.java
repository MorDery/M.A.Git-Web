package logic.pullRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PullRequest {

    private String m_UserNameOfRequester;
    private String m_TargetBranchName;
    private String m_BaseBranchName;
    private String m_Message;
    private String m_TimeStamp;
    private int m_SerialNumber;
    private PullRequestStatus m_Status;

    public enum PullRequestStatus {
        OPEN, CLOSE, REJECTED
    }

    public PullRequest(String i_UserNameOfRequester, String i_TargetBranchName, String i_BaseBranchName, String i_Message){
        m_UserNameOfRequester = i_UserNameOfRequester;
        m_TargetBranchName = i_TargetBranchName;
        m_BaseBranchName = i_BaseBranchName;
        m_Message = i_Message;
        m_TimeStamp = (new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss:SSS").format(new Date()));
        m_Status = PullRequestStatus.OPEN;
    }

    public String getUserNameOfRequester() {
        return m_UserNameOfRequester;
    }

    public String getTargetBranchName() {
        return m_TargetBranchName;
    }

    public String getBaseBranchName() {
        return m_BaseBranchName;
    }

    public String getMessage() {
        return m_Message;
    }

    public String getTimeStamp() {
        return m_TimeStamp;
    }

    public void setStatus(PullRequestStatus m_Status) {
        this.m_Status = m_Status;
    }

    public void setSerialNumber(int m_SerialNumber) {
        this.m_SerialNumber = m_SerialNumber;
    }
}
