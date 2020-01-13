package logic;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commit {
    private String m_RootFolderId;
    private List<String> m_PrecedingCommitId ;
    private String m_Message;
    private String m_Author;
    private String m_DateOfCreation;
    private transient SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss:sss");

    //from xml we get here
    public Commit(String i_RootFolderId,String i_Message, String i_Author,String i_Date){
        m_RootFolderId = i_RootFolderId;
        m_Message = i_Message;
        m_Author = i_Author;
        m_DateOfCreation = i_Date;
        m_PrecedingCommitId = new ArrayList<>();
    }
    public Commit(String i_RootFolderId,String i_Message, String i_Author){
        m_RootFolderId = i_RootFolderId;
        m_Message = i_Message;
        m_Author = i_Author;
        m_DateOfCreation = formatter.format(new Date());
        m_PrecedingCommitId = new ArrayList<>();
    }

    public Commit(String i_RootFolderId,String i_Message, String i_Author,String i_Date,List<String> i_PrecedingCommitId){
        m_RootFolderId = i_RootFolderId;
        m_Message = i_Message;
        m_Author = i_Author;
        m_PrecedingCommitId = i_PrecedingCommitId;
        m_DateOfCreation = i_Date;
    }
    public Commit(String i_RootFolderId,String i_Message, String i_Author,String i_Date,String i_PrecedingCommitId){
        m_RootFolderId = i_RootFolderId;
        m_Message = i_Message;
        m_Author = i_Author;
        m_PrecedingCommitId = new ArrayList<>();
        if(i_PrecedingCommitId != null) {
            m_PrecedingCommitId.add(i_PrecedingCommitId);
        }
        m_DateOfCreation = i_Date;
    }

    public String getFirstPrecedingCommitId(){
       String result = "";
        for(String precendingCommit : m_PrecedingCommitId){
            result = precendingCommit;
        }
        return result;
    }

    public String getM_RootFolderId() {
        return m_RootFolderId;
    }

    public List<String> getM_PrecedingCommitId() {
        return m_PrecedingCommitId;
    }

    public void setM_DateOfCreation(String m_DateOfCreation) {
        this.m_DateOfCreation = m_DateOfCreation;
    }

    public String getM_Message() {
        return m_Message;
    }

    public String getM_Author() {
        return m_Author;
    }

    public void setM_RootFolderId(String m_RootFolderId) {
        this.m_RootFolderId = m_RootFolderId;
    }

    public String getM_DateOfCreation() {
        return m_DateOfCreation;
    }

    public void setM_PrecedingCommitId(List<String> m_PrecedingCommitId) {
        this.m_PrecedingCommitId = m_PrecedingCommitId;
    }
    public void setM_PrecedingCommitId(String m_PrecedingCommitId) {

        this.m_PrecedingCommitId.clear();
        if (!(m_PrecedingCommitId.isEmpty()) && !(m_PrecedingCommitId == null))//if that's not the root commit - set the precending commit to new value
        {
            this.m_PrecedingCommitId.add(m_PrecedingCommitId);
        }
    }

    public String ToStringForSha1() {
        return String.format("%s;%s;%s",m_RootFolderId,m_PrecedingCommitId.toString(),m_Message);
    }

    public String[] commitDetailsFormatGetter(String i_RepositoryFullPath,String i_CommitId) {
        String commitContent = FileZipper.unZip(i_RepositoryFullPath + "/.magit/objects/" + i_CommitId);
        String[] splittedCommitContent = commitContent.split(";");
        return splittedCommitContent;
    }

    public String getSha1() {
        return DigestUtils.sha1Hex(this.ToStringForSha1());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String commitPrecending : m_PrecedingCommitId){
            sb.append(commitPrecending);
            sb.append(",");
        }
        return String.format("%s;%s;%s;%s;%s", m_RootFolderId ,sb.toString(), m_Message, m_Author, m_DateOfCreation);
    }
}
