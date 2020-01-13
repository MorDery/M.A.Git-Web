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

public class CheckoutBranchServlet extends HttpServlet {
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
        String branchNameToCheckOut = request.getParameter("branchNameToCheckOut");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        //להוסיף את הבדיקות של הRTB וכל זה
        if (branchNameToCheckOut.isEmpty()) {
            out.println(gson.toJson(new BranchesActionStatus(false,branchNameToCheckOut,"Invalid input! You can't enter empty name")));
        }
        else if (currentMagit.getActiveRepository().getHeadBranch().getM_Name().equals(branchNameToCheckOut)) {
            out.println(gson.toJson(new BranchesActionStatus(false,branchNameToCheckOut,"You cannot checkout to the current head branch!")));
        }
        else if (currentMagit.checkIfThereAreOpenChanges()) {
            out.println(gson.toJson(new BranchesActionStatus(false,branchNameToCheckOut,"You cannot checkout with open changes!")));
        } else if (currentMagit.getActiveRepository().getM_Branches().get(branchNameToCheckOut) == null) {
            out.println(gson.toJson(new BranchesActionStatus(false,branchNameToCheckOut,"Can't find that branch on this repository!")));
        }else if (currentMagit.getActiveRepository().getM_Branches().get(branchNameToCheckOut).getM_IsRemote()) {
            BranchesActionStatus branchesActionStatus = new BranchesActionStatus(false, branchNameToCheckOut, "You cannot checkout to remote branch!");
            branchesActionStatus.setIsNeedToCreateRTBAndCheckout(true);
            out.println(gson.toJson(branchesActionStatus));
        } else {
            currentMagit.CheckOut(branchNameToCheckOut);
            out.println(gson.toJson(new BranchesActionStatus(true,branchNameToCheckOut,null)));
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

