package MAGitHub.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import logic.MAGit;
import logic.notifications.ForkNotification;
import logic.users.UserManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ForkRepositoryServlet extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String currentUserName = SessionUtils.getUsername(request.getSession());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        MAGit currentUserMagit = userManager.getMagitUser(currentUserName);

        String localNewRepositoryName = request.getParameter("localRepositoryName");
        String remoteRepositoryLocation = request.getParameter("remoteRepositoryLocation");
        String userNameOfRemoteRepository = request.getParameter("userNameOfRemoteRepository");
        String localRepositoryLocation = "c:\\magit-ex3\\" + currentUserName ;

        MAGit otherUserMagit = userManager.getMagitUser(userNameOfRemoteRepository);
        String remoteRepositoryName = otherUserMagit.getRepositories().get(remoteRepositoryLocation).getM_Name();
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        if(localNewRepositoryName.isEmpty()){
            response.setStatus(400);
            response.getWriter().write("Can't enter empty repository name!");
        }
        else if (!currentUserMagit.isRepositoryExists(localRepositoryLocation)) {
            try {
                currentUserMagit.CloneRepository(remoteRepositoryLocation,localRepositoryLocation, localNewRepositoryName);
                otherUserMagit.getNotifications().add(new ForkNotification(remoteRepositoryName, currentUserName));
            } catch (Exception exception) {
                response.setStatus(400);
                response.getWriter().write(exception.toString());
            }
        }
        else{
            response.setStatus(400);
            response.getWriter().write("There is already a repository with the same name!");
        }

        if(response.getStatus() != 400) {
            out.println(gson.toJson((null)));
        }
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
        processRequest(request, response);
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
