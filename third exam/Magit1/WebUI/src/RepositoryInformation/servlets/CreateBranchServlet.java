package RepositoryInformation.servlets;

import logic.MAGit;
import logic.users.UserManager;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import dataFromServlet.BranchesActionStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateBranchServlet extends HttpServlet {
    private final static int SHA1_LENGTH = 40;

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
        String branchNameToCreate = request.getParameter("branchNameToCreate");
        String commitToPointAt = request.getParameter("commitSha1ToPoint");

        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        if (branchNameToCreate.isEmpty() || branchNameToCreate == null) {
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToCreate, "You can't enter empty branch name!")));
        } else if (commitToPointAt.isEmpty() || commitToPointAt == null) {
            out.println(gson.toJson(new BranchesActionStatus(false, commitToPointAt, "You can't enter empty commit SHA-1!")));
        } else if (currentMagit.getActiveRepository().getM_Commits().size() == 0) {
            out.println(gson.toJson(new BranchesActionStatus(false, null, "Cannot create branch since there are no commits!")));
        } else if (currentMagit.isBrunchNameExist(branchNameToCreate)) {
            out.println(gson.toJson(new BranchesActionStatus(false, branchNameToCreate, "This branch already exists!")));
        } else if (commitToPointAt.length() != SHA1_LENGTH) {
            out.println(gson.toJson(new BranchesActionStatus(false, commitToPointAt, "Invalid input! SHA-1 must include 40 Hexa characters")));
        } else if (!currentMagit.isSha1ExistsInFileSystem(commitToPointAt)) {
            out.println(gson.toJson(new BranchesActionStatus(false, commitToPointAt, "This SHA-1 does not exist!")));
        } else if (!currentMagit.isSha1OfReachableCommit(commitToPointAt)) {
            out.println(gson.toJson(new BranchesActionStatus(false, commitToPointAt, "This is not a SHA-1 of reachable commit!")));
        } else if(currentMagit.getActiveRepository().isSha1OfCommitThatRemoteBranchPointingOn(commitToPointAt) != null) {
            BranchesActionStatus branchesActionStatus = new BranchesActionStatus(false, commitToPointAt, "");
            branchesActionStatus.setIsNeedToCreateRTBOnRemoteBranch(true);
            branchesActionStatus.setNewBranchNameToCreate(branchNameToCreate);
            out.println(gson.toJson(branchesActionStatus));
        } else {
            currentMagit.createBranch(branchNameToCreate,commitToPointAt);
            out.println(gson.toJson(new BranchesActionStatus(true, null, null)));
        }
        response.setStatus(200);
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
