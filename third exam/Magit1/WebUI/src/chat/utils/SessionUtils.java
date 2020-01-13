package chat.utils;

import chat.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getUsername(HttpSession session) {
        return (String)session.getAttribute(Constants.USERNAME);
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static boolean hasSession(HttpServletRequest request) {
        return request.getSession(false) != null;
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(Constants.USERNAME) != null;
    }
}