package MAGitHub.servlets;

import logic.MAGit;
import logic.users.UserManager;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateRepositoryFromXMLServlet extends HttpServlet {

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

        String userName = SessionUtils.getUsername(request.getSession());
        UserManager gameManager = ServletUtils.getUserManager(getServletContext());
        MAGit currentMagit = gameManager.getMagitUser(userName);
        String userChoice = request.getParameter("isRepositoryNeedToBeDeleted");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

         try {
             if (userChoice.equals("true")) {
                 currentMagit.OverrideRepositoryInLocationAndLoadXML();
             } else if (userChoice.equals("false")) {
                 currentMagit.LoadRepositoryToOurObjects(currentMagit.getActiveRepository().getM_Location());
             } else {
                 currentMagit.CreateRepositoryUsingXML();
             }
         }
         catch (Exception exception) {
             response.setStatus(400);
             response.getWriter().write(exception.getMessage());
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
