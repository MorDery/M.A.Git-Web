package RepositoryInformation.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import dataFromServlet.ChangedInformationAfterPullRequest;
import logic.Branch;
import logic.Commit;
import logic.MAGit;
import logic.Repository;
import logic.pullRequest.PullRequest;
import logic.users.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ChangedInformationServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        String userName = SessionUtils.getUsername(request.getSession());
        UserManager gameManager = ServletUtils.getUserManager(getServletContext());
        MAGit currentMagit = gameManager.getMagitUser(userName);
        Repository localRepository = currentMagit.getActiveRepository();

        int targetPullRequestSerialNumber = Integer.parseInt(request.getParameter("pullRequestSerialNumber"));
        PullRequest pullRequest = localRepository.getM_PullRequests().get(targetPullRequestSerialNumber);

        String baseBranchName = pullRequest.getBaseBranchName();
        String targetBranchName = pullRequest.getTargetBranchName();
        Branch baseBranch = localRepository.getM_Branches().get(baseBranchName) ;
        Branch targetBranch = localRepository.getM_Branches().get(targetBranchName);
        List<String> commitsNames = new ArrayList<>();
        List<String> commitsList = new ArrayList<>();
        localRepository.getChangedCommitsAfterPullRequest(commitsList, commitsNames, baseBranch,targetBranch);

        out.println(gson.toJson(new ChangedInformationAfterPullRequest(commitsNames, commitsList)));
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

