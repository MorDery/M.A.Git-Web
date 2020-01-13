package logic.notifications;

import logic.Branch;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeleteBranchNotification extends Notification {

    String m_NameOfDeletedBranch;
    String m_NameOfUserWhichForked;

    public DeleteBranchNotification(Branch i_DeletedBranch, String i_NameOfUserWhichForked){
        m_NameOfDeletedBranch = i_DeletedBranch.getM_Name();
        m_NameOfUserWhichForked = i_NameOfUserWhichForked;
        m_IsShownOnSecondPage = false;
        m_IsShownOnThirdPage = false;
        m_TimeStamp = (new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss:SSS").format(new Date()));
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Delete branch was done on Time: ");
        stringBuilder.append(m_TimeStamp);
        stringBuilder.append(", User that Deleted the branch: ");
        stringBuilder.append(m_NameOfUserWhichForked);
        stringBuilder.append(", Name of  Delete  Branch: ");
        stringBuilder.append(m_NameOfDeletedBranch);
        return stringBuilder.toString();
    }
}
