package MAGitHub.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import dataFromServlet.NotificationListInformation;
import logic.MAGit;
import logic.users.UserManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class NotificationsServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userName = SessionUtils.getUsername(request.getSession());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        MAGit currentMagit = userManager.getMagitUser(userName);
        String pageActivater = request.getParameter("pageCalling");

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        if(pageActivater.equals("second")) {
            out.println(gson.toJson(new NotificationListInformation(currentMagit.getNotifications(),true)));
        }
        else{
            out.println(gson.toJson(new NotificationListInformation(currentMagit.getNotifications(),false)));
        }
    }

    protected void clearNotificationShownFields(HttpServletRequest request, HttpServletResponse response) {
        String userName = SessionUtils.getUsername(request.getSession());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        MAGit currentMagit = userManager.getMagitUser(userName);
        currentMagit.clearNotifictionsStatus();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        clearNotificationShownFields(request, response);
    }



    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
