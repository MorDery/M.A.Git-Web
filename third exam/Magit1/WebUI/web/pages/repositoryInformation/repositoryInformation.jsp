<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>RepositoryPage</title>
    <script src="../../common/jquery-2.0.3.min.js"></script>
    <script src="../../common/context-path-helper.js"></script>
    <link rel="stylesheet" href="../../common/bootstrap.min.css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src ="repositoryActions.js"></script>


    <style>
        * {
            box-sizing: border-box;
        }

        .Titels
        {
            font-size: 20px;
            color:whitesmoke;
        }

        .NamesFiled
        {
            font-size: 15px;
            color:whitesmoke;
        }

        /* Style the body */
        body {
            font-family: Lemon;
            background-color:  #116478;
            margin: 0;
        }

        .PullRequestsTable {
            border: 1px black solid;
            border-radius: 6px;
            width: 100%;
        }
        /* Header/logo Title */
        .header {
            padding: 50px;
            text-align: center;
            background: #1A9CBC;
            color: white;
        }

        /* Increase the font size of the heading */
        .header h1 {
            font-size: 40px;
        }

        /* Sticky navbar - toggles between relative and fixed, depending on the scroll position. It is positioned relative until a given offset position is met in the viewport - then it "sticks" in place (like position:fixed). The sticky value is not supported in IE or Edge 15 and earlier versions. However, for these versions the navbar will inherit default position */
        .navbar {
            overflow: hidden;
            background-color: #116478;
            position: sticky;
            position: -webkit-sticky;
            top: 0;
        }

        /* Style the navigation bar links */
        .navbar a {
            float: left;
            display: block;
            color: white;
            text-align: center;
            padding: 14px 20px;
            text-decoration: none;
        }

        /* Change color on hover */
        .navbar a:hover {
            background-color: #CFEAF1;
            color: black;
        }

        .branchesInfo {
            padding: 20px;
            background: #CFEAF1;
            margin: 3px;
        }

        .Commits {
            padding: 20px;
            background: #CFEAF1;
            margin: 3px;
        }


        .WC {
            padding: 20px;
            background: #CFEAF1;
            margin: 3px;
        }

        .fileContent{
            display: flex;
        }

        .saveBlobContentButton{
            margin: 5px;
            height:60px;
            width:120px;
        }

        .createFolderButton{
            margin: 5px;
            height:60px;
            width:120px;
        }
        .deleteFolderButton{
            margin: 5px;
            height:60px;
            width:120px;
        }
        .deleteFileButton{
            margin: 5px;
            height:60px;
            width:120px;
        }

        .createFileButton{
            margin: 5px;
            height:60px;
            width:120px;
        }

        .CommitButton{
            margin-left: 20px;
            height:60px;
            width:120px;
            margin-top: 5px;

        }

        .inputContent{
            margin-left: 20px;
        }

        .FileNameToCreate{
            margin: 5px;
        }

        .CommitMessage{
            margin-left: 20px;
        }

        .PRArea {
            padding: 20px;
            background: #CFEAF1;
            margin: 3px;
        }

        .CollaborationMenu{
            padding: 20px;
            background: #CFEAF1;
            margin: 3px;
        }

        .notificationArea {
            padding: 20px;
            background: #CFEAF1;
            margin: 3px;
        }


        /* Responsive layout - when the screen is less than 400px wide, make the navigation links stack on top of each other instead of next to each other */
        @media screen and (max-width: 400px) {
            .navbar a {
                float: none;
                width: 100%;
            }
        }
    </style>

</head>
<body>

<div class="header">
    <h1>Repository Information</h1>
</div>

<div class="navbar">
    <div class="repositoryActions">
        <span class="Titels">Repository name: </span>
        <span class="NamesFiled" id="RepositoryNameFiled"> </span>
        <br/>
        <span class="Titels" >Remote repository name: </span>
        <span class="NamesFiled" id="RemoteRepositoryNameFiled"> </span>
        <br/>
        <div class="goBackButton">
            <input type="submit" onclick="goBackButtonClick()" value="Go back"/>
        </div>
    </div>
</div>

<div class="branchesInfo">
    <div class="branchesMenu">
        <div class="branchesMenuContent">
            <div class="allBranchesList">
                <h2>Branches</h2>
                <h3>Branches in repository:</h3>
                <ul class="branchesList">
                </ul>
            </div>
            <div class="headBranch">
                <span> Head branch: </span>
                <span id="headBranchField"> </span>
            </div>
            <h3>Create new branch</h3>
            <div class="createBranch">
                <span> New branch name: </span>
                <br/>
                <input type="text" class="branchNameToCreate" value=""/>
                <br/>
                <span> Commit SHA-1 to point to:</span>
                <br/>
                <input type="text" class="commitSha1ToPoint" value=""/>
                <br/>
                <input type="submit" onclick="createBranch()" value="Create"/>
            </div>
            <h3>Delete Branch</h3>
            <div class="deleteBranch">
                <span> Branch name to delete: </span>
                <br/>
                <input type="text" class="branchNameToDelete" value=""/>
                <br/>
                <input type="submit" onclick="deleteBranch()" value="Delete">
            </div>
            <h3>Checkout</h3>
            <div class="checkOutBranch">
                <span> Branch name to checkout to: </span>
                <br/>
                <input type="text" class="branchNameToCheckOut" value=""/>
                <br/>
                <input type="submit" onclick="checkOutBranch()" value="CheckOut"/>
            </div>
        </div>
    </div>
</div>

<div class = "Commits">
    <h2>Commits, folders and files</h2>
    <div class="CommitsInformationList">
        <div class="allCommitList">
            <h3>Commits of head branch:</h3>
            <ul class="commitList"></ul>
            <h3>Files of chosen commit:</h3>
        </div>
        <div class="commitTree">
            <ul class="commitFiles" id="commitFiles"></ul>
        </div>
    </div>
</div>

<div class="WC">
    <h2>Working copy</h2>
    <div class="tree">
        <h3>W.C files:</h3>
        <ul class ="WcTree" id="WcTree"></ul>
        <br>
        <h5 >File full path: </h5>
        <input type="text" name="fileFullPath" class = "fileFullPath" id="fileFullPath"  size="35">
        <h3>File content:</h3>
        <div class="fileContent">
            <textarea class = "inputContent" id="fileContent" rows="7" cols="50"> </textarea>
            <form>
                <button class = "saveBlobContentButton" id="saveBlobContent">Save</button>
                <br>
                <button class = "deleteFileButton" id="deleteFileButton">Delete file</button>
                <br>
                <h5 class = "FolderNameToCreate">Folder name to create: </h5>
                <input type="text" name="FolderNameToCreate" class = "FolderNameToCreate" id="folderNameToCreateId">
                <br>
                <button class = "createFolderButton" id="createFolderButton">Create folder</button>
                <br>
                <button class = "deleteFolderButton" id="deleteFolderButton">Delete folder</button>
                <br>
            </form>
        </div>
        <table>
            <tr>
                <th>
                    <h5 class = "FileNameToCreate">File name to create: </h5>
                </th>
                <th>
                    <h5 class = "CommitMessage">Commit message: </h5>
                </th>
            </tr>

           <tr>
               <th><input type="text" name="FileNameToCreate" class = "FileNameToCreate" id="fileNameToCreateId"></th>
               <th> <input type="text" name="CommitMessage" class = "CommitMessage" id="CommitMessage"></th>
           </tr>
            <tr>
                <th><button class = "createFileButton" id="createFileButton">Create file</button></th>
                <th><button class = "CommitButton" id="commitButton" onclick="OnCommitClicked()">Commit</button></th>
            </tr>

        </table>
        </div>
    </div>
</div>

<div class="CollaborationMenu">
    <h2>Collaboration</h2>
    <div class ="PushBranchMenu">
        <span> Branch name to push to remote repository: </span>
        <br/>
        <input type="text" class="branchNameToPush" value=""/>
        <br/>
        <input type="submit" onclick="pushBranch()" value="Push Branch"/>
    </div>
    <div class ="PullBranchMenu">
        <span> Branch name to pull information for from remote repository: </span>
        <br/>
        <input type="text" class="branchNameToPull" value=""/>
        <br/>
        <input type="submit" onclick="pullBranch()" value="Pull Branch"/>
    </div>
</div>

<div class = "PRArea">
    <div class="PullRequestMenu">
        <h2>Create pull request</h2>
        <span> Branch name on local repository to push: </span>
        <br/>
        <input type="text" class="localBranchNameToPush" value=""/>
        <br/><span> Branch name to merge with on the remote repository: </span>
        <br/>
        <input type="text" class="remoteBranchNameToMerge" value=""/>
        <br/><span> Message: </span>
        <br/>
        <input type="text" class="messageOfPullRequest" value=""/>
        <br/>
        <input type="submit" onclick="pullRequest()" value="Create pull request"/>
    </div>

    <div class="ManagePullRequestMenu">
        <h2>My pull requests</h2>
        <table class = "PullRequestsTable" >
            <thead class = "PullRequestsThead">
            </thead>
            <tbody class="PullRequestsTableBody">
            </tbody>
        </table>
    </div>

    <div class="allChangedCommits">
        <h3>Changed commits:</h3>
        <ul class="CommitsList">
        </ul>
    </div>

    <div class="allChangedFiles">
        <h3>Details:</h3>
        <div class="updatedFiles">
            <h4>Updated files:</h4>
            <ul ></ul>
        </div>
        <div class="deletedFiles">
            <h4>Deleted files:</h4>
            <ul></ul>
        </div>
        <div class="addedFiles">
            <h4>Added files:</h4>
            <ul class="addedFiles"></ul>
        </div>
    </div>

    <div class="ChangedFileContent">
        <h4>File content:</h4>
        <textarea class = "inputContent" id="changedFileContent" rows="7" cols="100"> </textarea>
    </div>
</div>

<div class ="notificationArea" id="notificationArea">
    <h2>Notifications</h2>
    <div id="notificationList" class="span6">

    </div>
</div>

</body>
</html>

