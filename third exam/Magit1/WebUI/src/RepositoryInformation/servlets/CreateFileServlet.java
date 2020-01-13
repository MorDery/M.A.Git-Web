package RepositoryInformation.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import dataFromServlet.FileActionStatus;
import logic.Writer;
import logic.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateFileServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = SessionUtils.getUsername(request.getSession());
        UserManager gameManager = ServletUtils.getUserManager(getServletContext());
        String filePath = request.getParameter("filePath");
        String fileNameToCreate = request.getParameter("fileNameToCreate");
        String fileContent = request.getParameter("fileContent");

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        if(filePath == null){
            out.println(gson.toJson(new FileActionStatus(false, filePath, "Please select one of the folders to create a file in it!")));
        }
        else if(fileNameToCreate == null){
            out.println(gson.toJson(new FileActionStatus(false, fileNameToCreate, "Please enter name for the file you wish to create!")));
        }
        else if(fileContent.isEmpty()){
            out.println(gson.toJson(new FileActionStatus(false, fileContent, "Please the content of the file you wish to create!")));
        }
        else {
            filePath += "\\" + fileNameToCreate + ".txt";
            File file = new File(filePath);
            file.createNewFile();
            Writer.writeToFile(filePath, fileContent);
            out.println(gson.toJson(new FileActionStatus(true, fileContent, "")));

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
