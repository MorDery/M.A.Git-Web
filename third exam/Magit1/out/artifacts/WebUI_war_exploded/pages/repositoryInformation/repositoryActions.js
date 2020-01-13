var REFRESH_RATE = 2000;
var FETCHREPOSITORY_URL = buildUrlWithContextPath("FetchRepository");
var CREATEBRANCH_URL = buildUrlWithContextPath("CreateBranch");
var CHECKOUTBRANCH_URL = buildUrlWithContextPath("CheckOutBranch");
var WCINFORMATION_URL = buildUrlWithContextPath("WCInformation");
var SAVEFILECONTENT_URL = buildUrlWithContextPath("SaveFileContent");
var DELETEFILE_URL = buildUrlWithContextPath("DeleteFile");
var CREATEFILE_URL = buildUrlWithContextPath("CreateFile");
var ISWCCLEAN_URL = buildUrlWithContextPath("IsWcClean");
var MAKECOMMIT_URL = buildUrlWithContextPath("MakeCommit");
var COMMITFILES_URL = buildUrlWithContextPath("CommitFiles");
var PUSHBRANCHTOREMOTEREPOSITORY_URL = buildUrlWithContextPath("PushBranch");
var PULLBRANCHTOREMOTEREPOSITORY_URL = buildUrlWithContextPath("PullBranch");
var NOTIFICATION_URL = buildUrlWithContextPath("GetNotifications");
var DELETEBRANCH_URL = buildUrlWithContextPath("DeleteBranch");
var PULLREQUEST_URL = buildUrlWithContextPath("PullRequest");
var CREATERTBANDCHEKOUT_URL = buildUrlWithContextPath("CreateRTBAndCheckout");
var CREATERTBONREMOTEBRANCH = buildUrlWithContextPath("CreateRTBOnRemoteBranch");
var PULLREQUESTACCEPT_URL = buildUrlWithContextPath("AcceptPullRequest");
var PULLREQUESTCANCEL_URL = buildUrlWithContextPath("CancelPullRequest");
var CHANGEDINFORMATIONAFTETPULL_URL = buildUrlWithContextPath("ChangedInformation");
var CHANGEDFILESAFTETPULL_URL = buildUrlWithContextPath("ChangedFiles");
var DELETEFOLDER_URL = buildUrlWithContextPath("DeleteFolder");
var CREATEFOLDER_URL = buildUrlWithContextPath("CreateFolder");


window.onload = function () {
    var selectedRepository = fetchSelectedRepository();
    refreshRepositoryPage();
    refreshWC(selectedRepository);
    setInterval(refreshRepositoryPage, REFRESH_RATE);
};

function refreshRepositoryPage(){
    var selectedRepository = fetchSelectedRepository();
    var branchesListInRepository = fetchSelectedRepositoryBranches();
    var commitListInRepository = fetchSelectedRepositoryCommits();
    var pullRequestsListInRepository = fetchPullRequestsFromRepository();
    refreshBranchList(branchesListInRepository);
    refreshHeadBranch(selectedRepository);
    refreshCommitList(commitListInRepository);
    refreshNotification();
    fetchRepositoryInformation(selectedRepository);
    refreshWC();
    refreshPullRequests(pullRequestsListInRepository);
}

function fetchRepositoryInformation(currentRepository) {
    var repositoryNameInHtml = $('#RepositoryNameFiled');
    var RemoteRepositoryNameInHtml = $('#RemoteRepositoryNameFiled');
    var repositoryNameToDisplay = currentRepository.m_Name;
    repositoryNameInHtml.text(repositoryNameToDisplay);
    var remoteRepositoryNameToDisplay = currentRepository.m_RemoteRepositoryName;
    if(remoteRepositoryNameToDisplay != null){
        RemoteRepositoryNameInHtml.text(remoteRepositoryNameToDisplay);
    }
}

function refreshHeadBranch(Repository) {
    var headBranchInHtml = $('#headBranchField');
    var headBranchToDisplay = Repository.m_HeadBranch;
    headBranchInHtml.text(headBranchToDisplay.m_Name);
}

function refreshBranchList(BranchesList) {
    var branchesListInHtml = $('.allBranchesList ul');
    branchesListInHtml.empty();
    var branchesListInRepository = BranchesList;

    branchesListInRepository.forEach(function (branch) {
        var listElement = $(document.createElement('li')).text(branch.m_Name);
        listElement.appendTo(branchesListInHtml);
    })
}

function refreshCommitList(CommitsInformationList){
    var commitListInHtml = $('.allCommitList ul');
    commitListInHtml.empty();
    var commitListInRepository = CommitsInformationList;

    commitListInRepository.forEach(function (commitInformation) {
        var listElement = $(document.createElement('li')).text(commitInformation.m_CommitText);
        listElement.attr("id", commitInformation.m_CommitSha1);
        listElement.css("cursor","pointer");
        listElement.appendTo(commitListInHtml);
    });

    var commitList = $('.allCommitList li');
    for (var i = 0; i < commitList.length; i++) {

        commitList[i].onclick = showSelectedCommitInformation;
    }
}

function fetchSelectedRepository() {
    var result;

    $.ajax
    ({
        async: false,
        url: FETCHREPOSITORY_URL,
        type: 'GET',
        success: function (jsonFromServlet) {
            result = jsonFromServlet.m_SelectedRepository;
        }
    });

    return result;
}

function fetchSelectedRepositoryBranches() {
    var result;
    $.ajax
    ({
        async: false,
        url: FETCHREPOSITORY_URL,
        type: 'GET',
        success: function (jsonTest) {
            result = jsonTest.m_BranchesList;
        }
    });
    return result;
}

function fetchSelectedRepositoryCommits(){
    var result;
    $.ajax
    ({
        async: false,
        url: FETCHREPOSITORY_URL,
        type: 'GET',
        success: function (repositoryJson) {
            result = repositoryJson.m_CommitsInformationList;
        }
    });
    return result;
}

function fetchPullRequestsFromRepository(Repository){

    var result;
    $.ajax
    ({
        async: false,
        url: FETCHREPOSITORY_URL,
        type: 'GET',
        success: function (repositoryJson) {
            result = repositoryJson.m_PullRequests;
        }
    });
    return result;
}

function createBranch(){
    var branchInputBox = $('.branchNameToCreate');
    var branchNameToCreate = branchInputBox.val();
    var commitSha1InputBox = $('.commitSha1ToPoint');
    var commitSha1ToPointAt = commitSha1InputBox.val();

    $.ajax
    ({
        url: CREATEBRANCH_URL,
        data: {
            branchNameToCreate : branchNameToCreate ,
            commitSha1ToPoint : commitSha1ToPointAt
        },
        type: 'GET',
        success: createBranchStatus
    });
}

function createBranchStatus(createBranchStatusJson){
    var result;

    if (!createBranchStatusJson.m_IsValid) {
        if(createBranchStatusJson.m_IsNeedToCreateRTBOnRemoteBranch) {
            if (confirm("There is a remote branch that pointing on this commit SHA-1. Do you want to create the branch as a remote tracking branch on the remote branch? (If not - The branch will be created normally)")) {
                result = "true";
            } else {
                result = "false";
            }

            $.ajax
            ({
                url: CREATERTBONREMOTEBRANCH,
                data: {
                    pointedCommitSha1 : createBranchStatusJson.m_ObjectReference,
                    userChoice : result,
                    newBranchName : createBranchStatusJson.m_NewBranchNameToCreate
                },
                type: 'GET'
            });

        }
        else{
            alert(createBranchStatusJson.m_ErrorMessage);
        }
    }
    else{
        alert("The branch has been created successfully!")
    }
}

function checkOutBranch(){
    var branchInputBox = $('.branchNameToCheckOut');
    var branchNameToCheckOut = branchInputBox.val();

    $.ajax
    ({
        url: CHECKOUTBRANCH_URL,
        data: {
            branchNameToCheckOut : branchNameToCheckOut
        },
        type: 'GET',
        success: checkoutBranchStatus
    });
}

function checkoutBranchStatus(checkoutBranchStatusJson){
    var result;

    if (!checkoutBranchStatusJson.m_IsValid) {
        if(checkoutBranchStatusJson.m_IsNeedToCreateRTBAndCheckout) {
            if (confirm("Cannot checkout to remote branch. Do you want to create a remote tracking branch and checkout?")) {
                result = "true";
            } else {
                result = "false";
            }

            if(result == "true"){
                $.ajax
                ({
                    url:CREATERTBANDCHEKOUT_URL,
                    data: {
                        RTBToCreateAndCheckOut : checkoutBranchStatusJson.m_ObjectReference
                    },
                    type: 'GET'
                });
            }
            else{
                alert(checkoutBranchStatusJson.m_ErrorMessage);
            }
        }
        else{
            alert(checkoutBranchStatusJson.m_ErrorMessage);
        }
    }
    else{
        alert("Checkout has been made successfully!")
    }
}

function showSelectedCommitInformation(CommitInformation){
    var selectedCommitSha1 = event.currentTarget.id;
    var rootFolderName;
    var childrensOfRootFolder;
    $.ajax
    ({
        async: false,
        url: COMMITFILES_URL,
        type: 'GET',
        data: {
            CommitSHA1: selectedCommitSha1
        },
        success: function (jsonTest) {
            rootFolderName = jsonTest.m_Name;
            childrensOfRootFolder = jsonTest.m_Childrens;
        }
    });

    $('#commitFiles').empty();
    var li = document.createElement("li");
    li.append(document.createTextNode(rootFolderName));
    var ul = document.getElementById("commitFiles");
    ul.appendChild(li);


    var ul2 = document.createElement("ul");
    //ul.setAttribute("id",rootFolderName);
    li.appendChild(ul2);

    makeCommitTree(ul2,childrensOfRootFolder);
}

function makeCommitTree(ul, itemListToAddAsUl){
    itemListToAddAsUl.forEach(function (viewMagitFileNode) {
        var li = document.createElement("li");
        li.append(document.createTextNode(viewMagitFileNode.m_Name));
        //li.setAttribute("id", viewMagitFileNode.m_Name);
        ul.appendChild(li);

        var isFolder = viewMagitFileNode.m_IsFolder;
        if (isFolder) {
            var ul2 = document.createElement("ul");
            var ul2Name = "UL2";
            //ul2.setAttribute("id", ul2Name);
            li.appendChild(ul2);
            makeWCTree(ul2, viewMagitFileNode.m_Childrens);
        }
    })
}

function refreshWC() {
    var rootFolderName;
    var childrensOfRootFolder;
    var path;
    $.ajax
    ({
        async: false,
        url: WCINFORMATION_URL,
        type: 'GET',
        success: function (jsonTest) {
            rootFolderName = jsonTest.m_Name;
            childrensOfRootFolder = jsonTest.m_Childrens;
            path = jsonTest.m_Path;
        }
    });

    $('#WcTree').empty();
    $('.tree ul').append('<li id="root"><a <span> </span></a></li>');
    var paragraph = document.getElementById("root");
    var text = document.createTextNode(rootFolderName);
    paragraph.appendChild(text);

    var li = document.getElementById("root");
    li.setAttribute("id", path);
    li.setAttribute("data-id", "true");

    var ul = document.createElement("ul");
    ul.setAttribute("id",rootFolderName);
    li.appendChild(ul);

    makeWCTree(ul,childrensOfRootFolder);

    var commitList = $('.tree li');
    for (var i = 0; i < commitList.length; i++) {
        var isFolder = commitList[i].getAttribute('data-id');
        if(isFolder==="true") {
            commitList[i].onclick = folderClickFunctions;
        }
    }
}

function makeWCTree(ul, itemListToAddAsUl){
    itemListToAddAsUl.forEach(function (viewMagitFileNode) {
        var li = document.createElement("li");
        li.append(document.createTextNode(viewMagitFileNode.m_Name));
        li.setAttribute("id", viewMagitFileNode.m_Path);
        ul.appendChild(li);
        var isFolder = viewMagitFileNode.m_IsFolder;
        li.setAttribute("data-id", isFolder);

        if(isFolder){
            var ul2 = document.createElement("ul");
            var ul2Name = "UL2";
            ul2.setAttribute("id",ul2Name);
            li.appendChild(ul2);
            makeWCTree(ul2, viewMagitFileNode.m_Childrens);
        }
        else{ // blob file
            li.onclick =  function myFunction(e) {
                e.stopPropagation();
                document.getElementById("fileContent").value = viewMagitFileNode.m_Content;
                document.getElementById("fileFullPath").value = viewMagitFileNode.m_Path;
                document.getElementById("saveBlobContent").onclick = function(){
                    var fileContent = document.getElementById("fileContent").value;

                    $.ajax
                    ({
                        url: SAVEFILECONTENT_URL,
                        data: {
                            filePath : viewMagitFileNode.m_Path,
                            fileContent : fileContent
                        },
                        type: 'GET'
                    });
                    refreshWC();
                };
                document.getElementById("deleteFileButton").onclick = function(){
                    $.ajax
                    ({
                        url: DELETEFILE_URL,
                        data: {
                            filePath : viewMagitFileNode.m_Path
                        },
                        type: 'GET',
                        success:deleteFileStatus
                    })
                };
                document.getElementById("deleteFolderButton").onclick = function () {
                    alert("This is not a folder!");
                };
                document.getElementById("createFileButton").onclick = function () {
                    alert("You must select a folder to create a file!");
                }
            };
        }
    });
}

function folderClickFunctions(e) {
    e.stopPropagation();
    var isFolder = $(this).data("id");
    var currentPath = event.currentTarget.id;

    if (isFolder) {
        document.getElementById("fileFullPath").value = currentPath;

        document.getElementById("createFileButton").onclick = function () {
            var fileName = document.getElementById("fileNameToCreateId").value;
            var fileContent = document.getElementById("fileContent").value;
            if (fileName === "") {
                alert("you cannot create file with empty name!")
            } else {
                $.ajax
                ({
                    url: CREATEFILE_URL,
                    data: {
                        filePath: currentPath,
                        fileNameToCreate: fileName,
                        fileContent: fileContent
                    },
                    type: 'GET',
                    success:createFileStatus
                })
            }
        };

        document.getElementById("deleteFileButton").onclick = function(){
            alert("This is not a file!");
        };

        document.getElementById("deleteFolderButton").onclick = function () {
            $.ajax
            ({
                url: DELETEFOLDER_URL,
                data: {
                    filePath: currentPath
                },
                type: 'GET',
                success: deleteFolderStatus
            })
        };

        document.getElementById("createFolderButton").onclick = function () {
            var fileName = document.getElementById("folderNameToCreateId").value;
            if (fileName === "") {
                alert("you cannot create file with empty name!")
            } else {
                $.ajax
                ({
                    url: CREATEFOLDER_URL,
                    data: {
                        filePath: currentPath,
                        fileNameToCreate: fileName
                    },
                    type: 'GET',
                    success:createFolderStatus
                })
            }
        };

    }
}

function OnCommitClicked(){
    var commitMessage = document.getElementById("CommitMessage").value;
    if(commitMessage === ""){
        alert("You cannot make commit with empty message!");
    }
    else{
        var result;
        $.ajax
        ({
            async: false,
            url: ISWCCLEAN_URL,
            type: 'GET',
            success: function (jsonFromServlet) {
                result = jsonFromServlet.m_IsWcClean;
            }
        });
        if(!result){
            var CommitMessage = document.getElementById("CommitMessage").value;
            $.ajax
            ({
                async: false,
                url: MAKECOMMIT_URL,
                data: {
                    CommitMessage : CommitMessage
                },
                type: 'GET',
                success: function () {
                    alert("Commit has been made successfully!");
                },
                error: errorInFileSystem
            });
        }
        else{
            alert("There are no open changes - no need to commit!")
        }
    }

}

function goBackButtonClick(){
    $.ajax
    ({
        url: NOTIFICATION_URL,
        type: 'POST'
    });
    window.location.href = resolveUrl("/pages/MAGitHubAccount/MAGitHubAccount.jsp");
}

function pushBranch(){
    var branchInputBox = $('.branchNameToPush');
    var branchNameToPush = branchInputBox.val();
    $.ajax
    ({
        url: PUSHBRANCHTOREMOTEREPOSITORY_URL,
        data: {
            branchNameToPush : branchNameToPush
        },
        type: 'GET',
        success:pushBranchStatus
    });
}

function pushBranchStatus(pushBranchStatusJson){
    if (!pushBranchStatusJson.m_IsValid) {
        alert(pushBranchStatusJson.m_ErrorMessage);
    }
    else{
        alert("The branch has been pushed successfully!")
    }
}

function pullBranch(){
    var branchInputBox = $('.branchNameToPull');
    var branchNameToPull = branchInputBox.val();
    $.ajax
    ({
        url: PULLBRANCHTOREMOTEREPOSITORY_URL,
        data: {
            branchNameToPull : branchNameToPull
        },
        type: 'GET',
        success:pullBranchStatus
    });
}

function pullBranchStatus(pullBranchStatusJson){
    if (!pullBranchStatusJson.m_IsValid) {
        alert(pullBranchStatusJson.m_ErrorMessage);
    }
    else{
        alert("The branch has been pulled successfully!")
    }
}

function deleteBranch(){
    var branchInputBox = $('.branchNameToDelete');
    var branchNameToDelete = branchInputBox.val();
    $.ajax
    ({
        url: DELETEBRANCH_URL,
        data: {
            branchNameToDelete : branchNameToDelete
        },
        type: 'GET',
        success: deleteBranchStatus
    });
}

function deleteBranchStatus(deleteBranchStatusJson){
    if (!deleteBranchStatusJson.m_IsValid) {
        alert(deleteBranchStatusJson.m_ErrorMessage);
    }
    else{
        alert("The branch has been deleted successfully!");
    }
}

function refreshNotification(){
    var pageCalling = "third";
    $.ajax
    ({
        url: NOTIFICATION_URL,
        data: {
            pageCalling : pageCalling
        },
        type: 'GET',
        success: appendToNotificationArea
    });
}

function appendToNotificationArea(entries) {
    var notificationList = entries.m_Notifications;
    notificationList.forEach(function (otherUser) {
        appendNotificationEntry(otherUser);
    });

    //$.each(entries || [], appendNotificationEntry);
    // handle the scroller to auto scroll to the end of the chat area
    var scroller = $("#notificationList");
    var height = scroller[0].scrollHeight - $(scroller).height();
    $(scroller).stop().animate({ scrollTop: height }, "slow");
}

function appendNotificationEntry(entry){
    if(!entry.m_IsShownOnThirdPage){
        var entryElement = createNotificationEntry(entry);
        $("#notificationList").append(entryElement).append("<br>");
    }
}

function createNotificationEntry (notificationEntry){
    return $("<span class=\"success\">").append(notificationEntry);
}

function pullRequest() {
    var localBranchInputBox = $('.localBranchNameToPush');
    var targetBranchNameToPush = localBranchInputBox.val();
    var remoteBranchInputBox = $('.remoteBranchNameToMerge');
    var baseBranchName= remoteBranchInputBox.val();
    var messageInputBox = $('.messageOfPullRequest');
    var messageFromUser = messageInputBox.val();

    $.ajax
    ({
        url: PULLREQUEST_URL,
        data: {
            targetBranchNameToPush : targetBranchNameToPush,
            baseBranchName : baseBranchName,
            messageFromUser : messageFromUser
        },
        type: 'GET',
        success:pullRequestBranchStatus
    });
}

function pullRequestBranchStatus(pullRequestStatusJson){
    if (!pullRequestStatusJson.m_IsValid) {
        alert(pullRequestStatusJson.m_ErrorMessage);
    }
    else{
        alert("Pull request was sent!");
    }
}

function refreshPullRequests(pullRequestsList){
    createTableHeads();
    var tableInHtml = $('.PullRequestsTable tbody');

    tableInHtml.empty();
    if(pullRequestsList != null) {
        pullRequestsList.forEach(function (pullRequest) {
            var tr = $(document.createElement('tr'));
            var tdForAction=$(document.createElement('td'));
            var tdForCommit=$(document.createElement('td'));
            var tdUserName = $(document.createElement('td')).text(pullRequest.m_UserNameOfRequester);
            var tdTargetBranchName = $(document.createElement('td')).text(pullRequest.m_TargetBranchName);
            var tdBaseBranchName = $(document.createElement('td')).text(pullRequest.m_BaseBranchName);
            var tdTime = $(document.createElement('td')).text(pullRequest.m_TimeStamp);
            var tdStatus = $(document.createElement('td')).text(pullRequest.m_Status);
            var tdShowCommitButton = $(document.createElement('input')).text("Show commits");
            var tdAcceptButton = $(document.createElement('input')).text("Accept");
            var tdCancelButton = $(document.createElement('input')).text("Cancel");

            tdShowCommitButton.attr("type", "submit");
            tdShowCommitButton.attr("value", "Show commits");
            tdShowCommitButton.on('click', showCommitsInformation);
            tdShowCommitButton.attr("id", pullRequest.m_SerialNumber);

            tdAcceptButton.attr("type", "submit");
            tdAcceptButton.attr("value", "Accept");
            tdAcceptButton.attr("id", pullRequest.m_SerialNumber);
            tdAcceptButton.on('click', acceptPullRequest);

            tdCancelButton.attr("type", "submit");
            tdCancelButton.attr("value", "Cancel");
            tdCancelButton.attr("id", pullRequest.m_SerialNumber);
            tdCancelButton.on('click', cancelPullRequest);

            if(pullRequest.m_Status == "OPEN"){
                tdAcceptButton.appendTo(tdForAction);
                tdCancelButton.appendTo(tdForAction);
            }

            tdUserName.appendTo(tr);
            tdTargetBranchName.appendTo(tr);
            tdBaseBranchName.appendTo(tr);
            tdTime.appendTo(tr);
            tdStatus.appendTo(tr);
            tdShowCommitButton.appendTo(tdForCommit);
            tdForCommit.appendTo(tr);
            tdForAction.appendTo(tr);
            tr.appendTo(tableInHtml);
        });
    }
}

function createTableHeads(){
    var tableInHtml = $('.PullRequestsTable thead');
    tableInHtml.empty();
    var theadUserName= $(document.createElement('th')).text("User name");
    var theadTargetBranchName= $(document.createElement('th')).text("Target branch name");
    var theadBaseBranchName = $(document.createElement('th')).text("Base branch name");
    var theadTime= $(document.createElement('th')).text("Time");
    var theadStatus= $(document.createElement('th')).text("Pull request status");
    var theadCommits = $(document.createElement('th')).text("Commits");
    var theadAction = $(document.createElement('th')).text("Action");

    theadUserName.appendTo(tableInHtml);
    theadTargetBranchName.appendTo(tableInHtml);
    theadBaseBranchName.appendTo(tableInHtml);
    theadTime.appendTo(tableInHtml);
    theadStatus.appendTo(tableInHtml);
    theadCommits.appendTo(tableInHtml);
    theadAction.appendTo(tableInHtml);
}

function acceptPullRequest(){
    var pullRequestSerialNumber = event.currentTarget.id;
    $.ajax
    ({
        url: PULLREQUESTACCEPT_URL,
        data: {
            pullRequestSerialNumber : pullRequestSerialNumber
        },
        type: 'GET',
        success: function () {
            alert("Accepted pull request!");
        }
    });
}

function cancelPullRequest(){
    var pullRequestSerialNumber = event.currentTarget.id;
    var cancelReason = prompt("Please enter reason for cancel:");
    if (cancelReason != null) {
        $.ajax
        ({
            url: PULLREQUESTCANCEL_URL,
            data: {
                pullRequestSerialNumber : pullRequestSerialNumber,
                cancelReason : cancelReason
            },
            type: 'GET',
            success: function () {
                alert("Canceled pull request!");
            }
        });
    }
    else{
        alert("You cannot enter empty reason!");
    }
}

function showCommitsInformation(){
    var pullRequestSerialNumber = event.currentTarget.id;
    var commitsInformationListInHtml = $('.allChangedCommits ul');
    commitsInformationListInHtml.empty();
    var ChangedCommitsList;
    var commitsSHA1List;

    $.ajax
    ({
        async: false,
        url: CHANGEDINFORMATIONAFTETPULL_URL,
        data: {
            pullRequestSerialNumber: pullRequestSerialNumber
        },
        type: 'GET',
        success: function (jsonFromServlet) {
            ChangedCommitsList = jsonFromServlet.m_CommitsInfo;
            commitsSHA1List = jsonFromServlet.m_CommitsSHA1List;
        }
    });

    if(ChangedCommitsList!=null) {
        ChangedCommitsList.forEach(function (CommitsInformation) {
            var listElement = $(document.createElement('li')).text(CommitsInformation);
            listElement.css("cursor", "pointer");
            listElement.appendTo(commitsInformationListInHtml);
        });
    }


    var commitInfoList = $('.allChangedCommits li');
    for (var i = 0; i < commitInfoList.length; i++) {
        var currentCommitSHA1 = commitsSHA1List[i];
        commitInfoList[i].setAttribute("id",currentCommitSHA1 );
        commitInfoList[i].onclick = showSelectedChangedCommitInformation;
    }
}

function showSelectedChangedCommitInformation() {
    var commitSHA1 =  event.currentTarget.id;
    var addedFilesInChangedCommit;
    var deletedFilesInChangedCommit;
    var updatedFilesInChangedCommit;

    var updatedFilesListInHtml = $('.updatedFiles ul');
    updatedFilesListInHtml.empty();
    var deletedFilesListInHtml = $('.deletedFiles ul');
    deletedFilesListInHtml.empty();
    var addedFilesListInHtml = $('.addedFiles ul');
    addedFilesListInHtml.empty();

    $.ajax
    ({
        async: false,
        url: CHANGEDFILESAFTETPULL_URL,
        data: {
            commitSHA1: commitSHA1
        },
        type: 'GET',
        success: function (jsonFromServlet) {
            addedFilesInChangedCommit = jsonFromServlet.m_AddedFiles;
            deletedFilesInChangedCommit = jsonFromServlet.m_DeletedFiles;
            updatedFilesInChangedCommit = jsonFromServlet.m_UpdatedFiles;
        }
    });

    if(addedFilesInChangedCommit!=null) {
        addedFilesInChangedCommit.forEach(function (addedFile) {
            var listElement = $(document.createElement('li')).text(addedFile.m_FullPath);

            listElement.appendTo(addedFilesListInHtml);
        });
    }

    var addedFileList = $('.addedFiles li');
    for (var i = 0; i < addedFileList.length; i++) {
        var currentAddedContent = addedFilesInChangedCommit[i].m_Content;
        addedFileList[i].setAttribute("id",currentAddedContent );
        addedFileList[i].onclick = showFileContent;
    }

    if(deletedFilesInChangedCommit != null) {
        deletedFilesInChangedCommit.forEach(function (deletedFile) {
            var listElement = $(document.createElement('li')).text(deletedFile.m_FullPath);
            listElement.appendTo(deletedFilesListInHtml);
        });
    }

    if(updatedFilesInChangedCommit != null) {
        updatedFilesInChangedCommit.forEach(function (updatedFile) {
            var listElement = $(document.createElement('li')).text(updatedFile.m_FullPath);
            listElement.appendTo(updatedFilesListInHtml);
        });
    }

    var updatedFileList = $('.updatedFiles li');
    for (var i = 0; i < updatedFileList.length; i++) {
        var currentUpdatedContent = updatedFilesInChangedCommit[i].m_Content;
        updatedFileList[i].setAttribute("id",currentUpdatedContent );
        updatedFileList[i].onclick = showFileContent;
    }
}

function showFileContent(){
    var fileContent = event.currentTarget.id;
    if(fileContent && typeof fileContent !== "undefined"){
        document.getElementById("changedFileContent").value = fileContent;
    }
}

function errorInFileSystem(jqXHR){
    if(jqXHR.status && jqXHR.status==400){
        alert(jqXHR.responseText);
    }else{
        alert("Something went wrong");
    }
}

function createFolderStatus(createFolderStatusJson){
    if (!createFolderStatusJson.m_IsValid) {
        alert(createFolderStatusJson.m_ErrorMessage);
    }
    else{
        alert("The Folder has been created successfully!")
    }
}

function deleteFolderStatus(deleteFolderStatusJson){
    if (!deleteFolderStatusJson.m_IsValid) {
        alert(deleteFolderStatusJson.m_ErrorMessage);
    }
    else{
        alert("The Folder has been deleted successfully!")
    }
}

function createFileStatus(createFileStatusJson){
    if (!createFileStatusJson.m_IsValid) {
        alert(createFileStatusJson.m_ErrorMessage);
    }
    else{
        alert("The Text file has been created successfully!")
    }
}

function deleteFileStatus(deleteFileStatusJson){
    if (!deleteFileStatusJson.m_IsValid) {
        alert(deleteFileStatusJson.m_ErrorMessage);
    }
    else{
        alert("The Text file has been deleted successfully!")
    }
}

function getContextPath() {
    var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)){
        base = base.href;
    } else {
        base = document.URL;
    }
    var base = window.location.pathname;
    return base.substr(0, base.indexOf("/", 1));
}

function resolveUrl(url){
    return (getContextPath() + "/" + url).replace("//", "/");
}


