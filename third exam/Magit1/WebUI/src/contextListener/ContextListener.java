package contextListener;

import logic.Deleter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;

public class ContextListener implements ServletContextListener {

    private final static String m_WorkDirectoryPath = "c:\\magit-ex3";

    @Override
    public void contextInitialized(ServletContextEvent i_ContextEvent) {
        new File(m_WorkDirectoryPath).mkdir();
    }

    @Override
    public void contextDestroyed(ServletContextEvent i_ContextEvent) {
        try {
            Deleter.deleteDir(m_WorkDirectoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
