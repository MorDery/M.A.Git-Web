package logic.notifications;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ForkNotification extends Notification{

    String m_NameOfForkedRepository;
    String m_NameOfUserWhichForked;

    public ForkNotification(String i_NameOfForkedRepository, String i_NameOfUserWhichForked){
        m_NameOfForkedRepository = i_NameOfForkedRepository;
        m_NameOfUserWhichForked = i_NameOfUserWhichForked;
        m_IsShownOnSecondPage = false;
        m_IsShownOnThirdPage = false;
        m_TimeStamp = (new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss:SSS").format(new Date()));
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Fork was done on Time: ");
        stringBuilder.append(m_TimeStamp);
        stringBuilder.append(", User that forked the repository: ");
        stringBuilder.append(m_NameOfUserWhichForked);
        stringBuilder.append(", Name of  forked repository: ");
        stringBuilder.append(m_NameOfForkedRepository);
        return stringBuilder.toString();
    }


}
