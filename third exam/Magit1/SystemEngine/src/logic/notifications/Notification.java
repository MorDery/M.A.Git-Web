package logic.notifications;

import java.text.DateFormat;

public abstract class  Notification {
    String m_TimeStamp;
    boolean m_IsShownOnSecondPage;
    boolean m_IsShownOnThirdPage;

    public void setIsShownOnSecondPage(boolean m_IsShown) {
        this.m_IsShownOnSecondPage = m_IsShown;
    }

    public boolean getIsShownOnSecondPage() {
        return m_IsShownOnSecondPage;
    }

    public boolean getIsShownOnThirdPage() {
        return m_IsShownOnThirdPage;
    }

    public void setIsShownOnThirdPage(boolean m_IsShownOnThirdPage) {
        this.m_IsShownOnThirdPage = m_IsShownOnThirdPage;
    }

    @Override
    public abstract String toString();
}
