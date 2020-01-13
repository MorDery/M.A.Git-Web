package RepositoryInformation.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import dataFromServlet.BranchesActionStatus;
import logic.Branch;
import logic.MAGit;
import logic.Repository;
import logic.notifications.PullRequestSenderNotification;
import logic.pullRequest.PullRequest;
import logic.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class PullRequestSenderServlet extends HttpServlet {
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
        String targetBranchNameToPush = request.getParameter("targetBranchNameToPush");
        String baseBranchName = request.getParameter("baseBranchName");
        String messageFromUser = request.getParameter("messageFromUser");

        Repository localRepository = currentMagit.getActiveRepository();

        if (localRepository.getM_RemoteRepositoryLocation() == null) {
            out.println(gson.toJson(new BranchesActionStatus(false, null, "There is no remote repository!")));
        }

        String remoteRepositoryUserName = extractUserNameFromRemoteRepoLocation(localRepository.getM_RemoteRepositoryLocation());
        MAGit remoteRepoMagit = gameManager.getMagitUser(remoteRepositoryUserName);
        Repository remoteRepository = remoteRepoMagit.getRepositories().get(localRepository.getM_RemoteRepositoryLocation());

        if (targetBranchNameToPush == null || targetBranchNameToPush.isEmpty() || baseBranchName == null || baseBranchName.isEmpty()) {
            out.println(gson.toJson(new BranchesActionStatus(false, targetBranchNameToPush, "You cannot enter empty branch name!")));
        } else if (messageFromUser == null || messageFromUser.isEmpty()) {
            out.println(gson.toJson(new BranchesActionStatus(false, messageFromUser, "You cannot enter empty message!")));
        } else if (!localRepository.getM_Branches().containsKey(targetBranchNameToPush)) {
            out.println(gson.toJson(new BranchesActionStatus(false, targetBranchNameToPush, "There is no such a branch to push!")));
        } else if (!remoteRepository.getM_Branches().containsKey(baseBranchName)) {
            out.println(gson.toJson(new BranchesActionStatus(false, baseBranchName, "There is no such a branch to merge!")));
        } else if(localRepository.getM_Branches().get(targetBranchNameToPush).getM_IsRemote()){
            out.println(gson.toJson(new BranchesActionStatus(false, targetBranchNameToPush, "Cannot push remote branch!")));
        } else{
            PullRequest pullRequest = new PullRequest(userName, targetBranchNameToPush, baseBranchName, messageFromUser);
            pullRequest.setSerialNumber(remoteRepository.getM_PullRequests().size());
            remoteRepository.getM_PullRequests().add(pullRequest);
            remoteRepoMagit.getNotifications().add(new PullRequestSenderNotification(localRepository.getM_RemoteRepositoryName(), pullRequest));
            out.println(gson.toJson(new BranchesActionStatus(true, targetBranchNameToPush, "Pull request was sent!")));
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


