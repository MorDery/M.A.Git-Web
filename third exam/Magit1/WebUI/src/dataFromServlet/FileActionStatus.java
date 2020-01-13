package dataFromServlet;

public class FileActionStatus {
    private boolean m_IsValid;
    private String m_ObjectReference;
    private String m_ErrorMessage;


    public FileActionStatus(boolean isValid, String i_WantedObjectToAct, String i_ErrorMessage) {
        m_IsValid = isValid;
        m_ObjectReference = i_WantedObjectToAct;
        m_ErrorMessage = i_ErrorMessage;
    }

}
