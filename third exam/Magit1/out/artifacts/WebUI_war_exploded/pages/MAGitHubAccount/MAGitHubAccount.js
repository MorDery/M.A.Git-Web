var REFRESH_RATE = 2000;
var CHECKVALIDATIONOFXML_URL = buildUrlWithContextPath("CheckValidationOfXML");
var CREATEREPOSITORYFROMXML_URL = buildUrlWithContextPath("CreateRepositoryFromXML");
var STATUS_URL = buildUrlWithContextPath("Status");
var REPOSITORYLIST_URL = buildUrlWithContextPath("LoadRepositoryList");
var SELECTEDREPOSITORYBYUSER_URL = buildUrlWithContextPath("SelectedRepository");
var OTHERUSERSLIST_URL = buildUrlWithContextPath("OtherUsersWithRepositoryInSystem");
var OTHERUSERSREPOSITORIES_URL = buildUrlWithContextPath("SelectedOtherUserRepositories");
var FORKREPOSITORY_URL = buildUrlWithContextPath("ForkRepository");
var NOTIFICATION_URL = buildUrlWithContextPath("GetNotifications");

window.onload = function () {
    refreshMagitHubPage();
    setInterval(refreshMagitHubPage, REFRESH_RATE);
};

function refreshMagitHubPage(){
    refreshOtherUsersInSystemList();
    refreshRepositoryList();
    refreshNotificationList();
}

function loadRepositoryClicked(event) {
    var file = event.target.files[0];
    var reader = new FileReader();
    reader.onload = function() {
        var content = reader.result;
        var creatorName = getUserName();
        $.ajax(
            {
                url: CHECKVALIDATIONOFXML_URL,
                type: 'POST',
                data: {
                    file: content,
                    creator: creatorName
                },
                success: loadRepositoryCallBack,
                error: errorInFileSystem
            }
        );
    };

    $.ajax // Getting creator's name.
    ({
        url: STATUS_URL,
        type: 'GET',
        success: function (json) {
            creatorName = json.userName;
            reader.readAsText(file);
        }
    });
}

function getUserName() {
    var result;
    $.ajax
    ({
        async: false,
        url: STATUS_URL,
        type: 'GET',
        success: function (json) {
            result = json.m_ActiveUserName;
        }
    });
    return result;
}

function loadRepositoryCallBack(repositoryInformationJson) {
    var result;

    if (repositoryInformationJson.m_IsValid) {
        if(repositoryInformationJson.m_IsRepositoryAlreadyExists){
            if (confirm("There is already a repository in this location. Do you want to override?") ) {
                result = "true";
            } else {
                result = "false";
            }
        }
        else{
            result = "notExist";
        }

        $.ajax
        ({
            url: CREATEREPOSITORYFROMXML_URL,
            data: {
                isRepositoryNeedToBeDeleted : result
            },
            type: 'GET',
            success : refreshRepositoryList,
            error: errorInFileSystem
        });
    }
}

function errorInFileSystem(jqXHR){
    if(jqXHR.status && jqXHR.status==400){
        alert(jqXHR.responseText);
    }else{
        alert("Something went wrong");
    }
}

function refreshRepositoryList() {
    $.ajax
    ({
        url: REPOSITORYLIST_URL,
        type: 'GET',
        success: refreshRepositoryListCallback
    });
}

function refreshRepositoryListCallback(json) {
    var repositoryTable = $('.repositoryTable tbody');
    repositoryTable.empty();
    var RepositoriesList = json.m_RepositoryListInformation;

    RepositoriesList.forEach(function (repo) {
        var tr = $(document.createElement('tr'));
        var tdRepoName = $(document.createElement('td')).text(repo.m_RepositoryName);
        var tdActiveBranchName = $(document.createElement('td')).text(repo.m_ActiveBranchName);
        var tdNumOfBranches = $(document.createElement('td')).text(repo.m_NumberOfBranches);
        var tdLastCommitDate = $(document.createElement('td')).text(repo.m_LastCommitDate);
        var tdLastCommitMessage = $(document.createElement('td')).text(repo.m_LastCommitMessage);
        var tdRepositoryLocation = $(document.createElement('input')).text(repo.m_RepositoryLocation);
        tdRepositoryLocation.hide();

        tdRepoName.css("cursor", "pointer");
        tdRepoName.appendTo(tr);
        tdActiveBranchName.appendTo(tr);
        tdNumOfBranches.appendTo(tr);
        tdLastCommitDate.appendTo(tr);
        tdLastCommitMessage.appendTo(tr);
        tdRepositoryLocation.appendTo(tr);

        tr.appendTo(repositoryTable);
    });

    var tr = $('.tableBody tr');
    for (var i = 0; i < tr.length; i++) {
        tr[i].onclick = createRepositoryPage;
    }
}

function createRepositoryPage(){
    var td = event.currentTarget.children[5];
    var repositoryLocation = td.innerText;
    $.ajax
    ({
        url: SELECTEDREPOSITORYBYUSER_URL,
        data: {
            repositoryLocation : repositoryLocation
        },
        type: 'GET',
        success: displaySelectedRepository
    });
}

function displaySelectedRepository() {
    $.ajax
    ({
        url: NOTIFICATION_URL,
        type: 'POST'
    });
    window.location.href = resolveUrl("/pages/repositoryInformation/repositoryInformation.jsp");
}

function refreshOtherUsersInSystemList(){

    $.ajax
    ({
        async: false,
        url: OTHERUSERSLIST_URL,
        type: 'GET',
        success: attachOtherUsersNameToList
    });
}

function attachOtherUsersNameToList(otherUsersListJson){
    var otherUsersListInHtml = $('.otherUsersNames ul');
    otherUsersListInHtml.empty();
    var otherUsersListWithRepositoryInSystem = otherUsersListJson.m_OtherUsers;

    otherUsersListWithRepositoryInSystem.forEach(function (otherUser) {
        var listElement = $(document.createElement('li')).text(otherUser);
        listElement.attr("id", otherUser);
        listElement.css("cursor", "pointer");
        listElement.appendTo(otherUsersListInHtml);
    })

    var otherUsersList = $('.otherUsersNames li');
    for (var i = 0; i < otherUsersList.length; i++) {
        otherUsersList[i].onclick = showSelectedUserInformation;
    }
}

function showSelectedUserInformation(){
    var selectedOtherUserName = event.currentTarget.id;
    $.ajax
    ({
        url: OTHERUSERSREPOSITORIES_URL,
        data: {
            otherUserName : selectedOtherUserName
        },
        type: 'GET',
        success: function(jsonFromServlet){
            addOtherUserRepositoriesToTable(jsonFromServlet,selectedOtherUserName);
        }
    });
}

function addOtherUserRepositoriesToTable(otherUserRepositoriesJson,selectedOtherUserName){
    createTableHeads();
    var tableInHtml = $('.otherUserRepositoryTable tbody');
    tableInHtml.empty();
    var RepositoriesList = otherUserRepositoriesJson.m_RepositoryListInformation;

    RepositoriesList.forEach(function (repo) {
        var tr = $(document.createElement('tr'));
        var tdRepoName = $(document.createElement('td')).text(repo.m_RepositoryName);
        var tdActiveBranchName = $(document.createElement('td')).text(repo.m_ActiveBranchName);
        var tdNumOfBranches = $(document.createElement('td')).text(repo.m_NumberOfBranches);
        var tdLastCommitDate = $(document.createElement('td')).text(repo.m_LastCommitDate);
        var tdLastCommitMessage = $(document.createElement('td')).text(repo.m_LastCommitMessage);
        var tdForkButton = $(document.createElement('input')).text("Fork");

        tdForkButton.attr("type","submit");
        tdForkButton.attr("value","Fork");
        tdForkButton.attr("class", selectedOtherUserName);
        tdForkButton.attr("id", repo.m_RepositoryLocation);
        tdForkButton.on('click', forkSelectedRepository);

        tdRepoName.appendTo(tr);
        tdActiveBranchName.appendTo(tr);
        tdNumOfBranches.appendTo(tr);
        tdLastCommitDate.appendTo(tr);
        tdLastCommitMessage.appendTo(tr);
        tdForkButton.appendTo(tr);
        tr.appendTo(tableInHtml);
    });
}

function createTableHeads(){
    var tableInHtml = $('.otherUserRepositoryTable thead');
    tableInHtml.empty();
    var theadRepoName = $(document.createElement('th')).text("Repository name");
    var theadActiveBranchName = $(document.createElement('th')).text("Active branch name");
    var theadNumOfBranches = $(document.createElement('th')).text("Number of branches");
    var theadLastCommitDate = $(document.createElement('th')).text("Last commit date");
    var theadLastCommitMessage = $(document.createElement('th')).text("Last commit message");
    var theadForkOption = $(document.createElement('th')).text("Fork repository");

    theadRepoName.appendTo(tableInHtml);
    theadActiveBranchName.appendTo(tableInHtml);
    theadNumOfBranches.appendTo(tableInHtml);
    theadLastCommitDate.appendTo(tableInHtml);
    theadLastCommitMessage.appendTo(tableInHtml);
    theadForkOption.appendTo(tableInHtml);
}

function forkSelectedRepository(){
    var remoteRepositoryLocation = event.currentTarget.id;
    var userNameOfRemoteRepository = event.target.className;
    var localRepositoryName = prompt("Please enter local repository name:");
    if (localRepositoryName != null) {
        $.ajax
        ({
            url: FORKREPOSITORY_URL,
            data: {
                remoteRepositoryLocation : remoteRepositoryLocation,
                userNameOfRemoteRepository : userNameOfRemoteRepository,
                localRepositoryName : localRepositoryName
            },
            type: 'GET',
            error: errorInFileSystem
        });
    }
}

function refreshNotificationList(){
    var pageCalling = "second";
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

//entries = the added chat strings represented as a single string
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
    if(!entry.m_IsShownOnSecondPage){
        var entryElement = createNotificationEntry(entry);
        $("#notificationList").append(entryElement).append("<br>");
    }
}

function createNotificationEntry (notificationEntry){
    return $("<span class=\"success\">").append(notificationEntry);
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