package dataFromServlet;

public class BranchesActionStatus {
    private boolean m_IsValid;
    private String m_ObjectReference;
    private String m_ErrorMessage;
    private String m_NewBranchNameToCreate;
    private boolean m_IsNeedToCreateRTBAndCheckout = false;
    private boolean m_IsNeedToCreateRTBOnRemoteBranch = false;

    public BranchesActionStatus(boolean isValid, String i_WantedObjectToAct, String i_ErrorMessage) {
        m_IsValid = isValid;
        m_ObjectReference = i_WantedObjectToAct;
        m_ErrorMessage = i_ErrorMessage;
    }

    public void setIsNeedToCreateRTBAndCheckout(boolean m_IsNeedToCreateRTBAndCheckout) {
        this.m_IsNeedToCreateRTBAndCheckout = m_IsNeedToCreateRTBAndCheckout;
    }

    public void setIsNeedToCreateRTBOnRemoteBranch(boolean m_IsNeedToCreateRTBOnRemoteBranch) {
        this.m_IsNeedToCreateRTBOnRemoteBranch = m_IsNeedToCreateRTBOnRemoteBranch;
    }

    public void setNewBranchNameToCreate(String m_NewBranchNameToCreate) {
        this.m_NewBranchNameToCreate = m_NewBranchNameToCreate;
    }
}
