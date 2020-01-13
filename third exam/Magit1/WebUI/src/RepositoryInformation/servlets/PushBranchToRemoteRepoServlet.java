package RepositoryInformation.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import dataFromServlet.BranchesActionStatus;
import logic.Branch;
import logic.MAGit;
import logic.Repository;
import logic.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class PushBranchToRemoteRepoServlet extends HttpServlet {

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

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        String userName = SessionUtils.getUsername(request.getSession());
        UserManager gameManager = ServletUtils.getUserManager(getServletContext());
        MAGit currentMagit = gameManager.getMagitUser(userName);
        String branchNameToPush = request.getParameter("branchNameToPush");
        Repository localRepository = currentMagit.getActiveRepository();
        Branch branchToPush = null ;

        if(localRepository.getM_RemoteRepositoryLocation() == null) {
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToPush, "Repository has no remote repository!")));
        }
        String remoteRepositoryUserName = extractUserNameFromRemoteRepoLocation(localRepository.getM_RemoteRepositoryLocation());
        MAGit remoteRepoMagit = gameManager.getMagitUser(remoteRepositoryUserName);
        Repository remoteRepository = remoteRepoMagit.getRepositories().get(localRepository.getM_RemoteRepositoryLocation());

        if(branchNameToPush == null){
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToPush, "You can't enter empty branch name!")));
        }
        else if (branchNameToPush.isEmpty()) {
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToPush, "You can't enter empty branch name!")));
        } else if (localRepository.getM_RemoteRepositoryLocation().isEmpty()) {
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToPush, "Current repository has no remote repository!")));
        } else if (!branchNameToPush.equals(localRepository.getHeadBranch().getM_Name())) {
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToPush, "This Branch is not the Head Branch! Can't Push it!")));
        } else if (!localRepository.getM_Branches().containsKey(branchNameToPush)) {
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToPush, "wrong Branch was entered name!Can't find it on this repository!")));
        } else if (localRepository.getM_Branches().get(branchNameToPush).getM_Tracking()) {
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToPush, "This Branch is a Remote Tracking Branch!Cant push it!")));
        } else {
            branchToPush = localRepository.getM_Branches().get(branchNameToPush);
            currentMagit.PushToRemoteRepository(branchToPush,localRepository,remoteRepository);
            remoteRepoMagit.getRepositories().put(remoteRepository.getM_Location(),remoteRepository);
            out.println(gson.toJson(new BranchesActionStatus(true, null, null)));
        }
        response.setStatus(200);
    }
    private String extractUserNameFromRemoteRepoLocation(String i_RemoteRepoLocation) {
        String separator = "\\";
        String[] splittedLocationContent = i_RemoteRepoLocation.split(Pattern.quote(separator));

        return splittedLocationContent[2];
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
