package MAGitHub.servlets;

import logic.MAGit;
import logic.users.UserManager;
import chat.utils.ServletUtils;
import com.google.gson.Gson;
import dataFromServlet.LoadFromXMLStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;




public class CheckValidationOfXMLServlet extends HttpServlet {
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
        String repositoryFromXMLContent = request.getParameter("file");
        String userOfTheRepository = request.getParameter("creator");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        MAGit currentMagit = userManager.getMagitUser(userOfTheRepository);
        List<String> errors = new ArrayList<>();

        try {
            if (currentMagit.isXMLValid(repositoryFromXMLContent)) {
                out.println(gson.toJson(new LoadFromXMLStatus(true, null, currentMagit)));
            }
            else{
                errors = currentMagit.getM_ReaderFromXML().getM_XmlTester().getErrorMessage();
                response.setStatus(400);
                StringBuilder sb = new StringBuilder("Invalid XML:" +  System.lineSeparator());
                for (String str : errors) {
                    sb.append(str + System.lineSeparator());
                }
                response.getWriter().write(sb.toString());            }
        }catch (Exception exception) {
            out.println(gson.toJson(exception.getMessage()));
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
