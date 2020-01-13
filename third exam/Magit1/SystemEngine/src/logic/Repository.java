package logic;

import logic.pullRequest.PullRequest;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Repository {

    private String m_Location;
    private String m_Name = null;
    private Branch m_HeadBranch = null;
    private final Map<String,Blob> m_Blobs = new HashMap<>();
    private final Map<String,Folder> m_Folders = new HashMap<>();
    private final Map<String,Commit> m_Commits = new HashMap<>();
    private final Map<String,Branch> m_Branches = new HashMap<>();
    private String m_RemoteRepositoryLocation = null;
    private String m_RemoteRepositoryName = null;
    private final String DATE_FORMAT = "dd.MM.yyyy-hh:mm:ss:sss";
    private List<PullRequest> m_PullRequests = new ArrayList<>();



    public Repository(String i_Location, String i_Name) {
        m_Location = i_Location;
        m_Name = i_Name;
    }

    public Repository(String i_Location) {
        m_Location = i_Location;
    }

    public String getM_Location() {
        return m_Location;
    }

    public List<PullRequest> getM_PullRequests() {
        return m_PullRequests;
    }

    public void setM_PullRequests(List<PullRequest> m_PullRequests) {
        this.m_PullRequests = m_PullRequests;
    }

    public  Map<String,Blob>  getM_Blobs() {
        return m_Blobs;
    }

    public  Map<String,Folder>  getM_Folders() {
        return m_Folders;
    }

    public Map<String, Commit> getM_Commits() {
        return m_Commits;
    }

    public Map<String, Branch> getM_Branches() {
        return m_Branches;
    }

    public String getM_Name() {
        return m_Name;
    }

    public String getM_RemoteRepositoryLocation() {
        return m_RemoteRepositoryLocation;
    }

    public String getM_RemoteRepositoryName() {
        return m_RemoteRepositoryName;
    }

    public Branch isSha1OfCommitThatRemoteBranchPointingOn(String i_CommitSha1){
        Branch RemoteBranchThatPointingOnThisSha1OfCommit = null;

        for (Map.Entry<String, Branch> entry : m_Branches.entrySet()) {
            if(entry.getValue().getM_PointedCommitId().equals(i_CommitSha1) && entry.getValue().getM_IsRemote()){
                RemoteBranchThatPointingOnThisSha1OfCommit = entry.getValue();
                break;
            }
        }

        return RemoteBranchThatPointingOnThisSha1OfCommit;
    }

    public void setM_Name(String i_Name){
        m_Name = i_Name;
    }

    public void setM_Location(String m_Location) {
        this.m_Location = m_Location;
    }

    public void setM_RemoteRepository(String i_RemoteRepositoryLocation){
        m_RemoteRepositoryLocation = i_RemoteRepositoryLocation;
    }

    public List<Commit> getCommitsOfHeadBranch(){
        List<Commit> result = new ArrayList<>();
        Commit currentCommit = m_Commits.get(getHeadBranch().getM_PointedCommitId());

        result.add(currentCommit);
        getCommitsOfHeadBranchRec(currentCommit, result);

        return result;
    }

    public void setM_RemoteRepositoryName(String m_RemoteRepositoryName) {
        this.m_RemoteRepositoryName = m_RemoteRepositoryName;
    }

    public List<String> getListOfPointedBranchesOnGivenCommitSha1(String i_CommitSha1){
        List<String> result = new ArrayList<>();

        for(Map.Entry<String, Branch> entry : m_Branches.entrySet()){
            if(entry.getValue().getM_PointedCommitId().equals(i_CommitSha1)) {
                result.add(entry.getValue().getM_Name());
            }
        }

        return result;
    }

    private void getCommitsOfHeadBranchRec(Commit i_CurrentCommit, List<Commit> i_CommitsOfHeadBranch){

        if(i_CurrentCommit.getM_PrecedingCommitId() == null){
            return;
        }
        else if(i_CurrentCommit.getM_PrecedingCommitId().isEmpty())
        {
            return;
        }
        else if(i_CurrentCommit.getM_PrecedingCommitId().get(0).isEmpty()){
            return;
        }
        else{
            List<String> precendingCommitId = i_CurrentCommit.getM_PrecedingCommitId();
            for(String commitSha1 : precendingCommitId){
                i_CommitsOfHeadBranch.add(m_Commits.get(commitSha1));
                getCommitsOfHeadBranchRec(m_Commits.get(commitSha1), i_CommitsOfHeadBranch);
            }
        }

    }

    public static Repository CopyRepository(Repository i_RepositoryToCopy) {
        Repository cloneRepository = new Repository(i_RepositoryToCopy.getM_Location());
        cloneRepository.copyBranches(i_RepositoryToCopy);
        cloneRepository.copyCommits(i_RepositoryToCopy);
        cloneRepository.copyBlobs(i_RepositoryToCopy);
        cloneRepository.copyFolders(i_RepositoryToCopy);
        cloneRepository.setM_Name(i_RepositoryToCopy.getM_Name());
        return cloneRepository;
    }

    private void copyFolders(Repository i_RepositoryToCopy) {
        for(Map.Entry<String,Folder> folderEntry : i_RepositoryToCopy.getM_Folders().entrySet()) {
            Folder oldFolder = folderEntry.getValue();
            Folder folderToAdd = new Folder(oldFolder.isM_IsRoot());
            for(Map.Entry<String,Folder.Item> itemEntry : oldFolder.getM_Items().entrySet()){
                Folder.Item itemValue = itemEntry.getValue();
                Folder.Item newItemToAttach = new Folder.Item(itemValue.getM_Name(),itemValue.getM_Id(),itemValue.getM_Type(),itemValue.getLastUpdater(),itemValue.getLastUpdateDate());
                folderToAdd.getM_Items().put(itemEntry.getKey(),newItemToAttach);
            }
            m_Folders.put(folderEntry.getKey(),folderToAdd);
        }
    }

    private void copyBlobs(Repository i_RepositoryToCopy) {
        for(Map.Entry<String,Blob> bloblEntry : i_RepositoryToCopy.getM_Blobs().entrySet()){
            Blob blolbToAdd = new Blob(bloblEntry.getValue().getM_Content());
            this.getM_Blobs().put(bloblEntry.getKey(),blolbToAdd);
        }
    }

    private void copyCommits(Repository i_RepositoryToCopy) {
        for(Map.Entry<String,Commit> commitEntry : i_RepositoryToCopy.getM_Commits().entrySet()){
            Commit commitToCopy = commitEntry.getValue();
            Commit commitToAdd = new Commit(commitToCopy.getM_RootFolderId(),commitToCopy.getM_Message(),commitToCopy.getM_Author(),commitToCopy.getM_DateOfCreation());
            for(String precendingCommit : commitToCopy.getM_PrecedingCommitId()){
                commitToAdd.getM_PrecedingCommitId().add(precendingCommit);
            }
            this.getM_Commits().put(commitEntry.getKey(),commitToAdd);
        }
    }

    private void copyBranches(Repository i_RepositoryToCopy) {
        for(Map.Entry<String,Branch> branchEntry : i_RepositoryToCopy.getM_Branches().entrySet()){
            Branch branchToCopy = branchEntry.getValue();
            Branch branchToAdd = new Branch(branchToCopy.getM_Name(),branchToCopy.getM_PointedCommitId(),branchToCopy.getM_TrackingAfter(),branchToCopy.getM_IsRemote(),branchToCopy.getM_Tracking());
            if(branchToCopy.isM_IsHead()){
                branchToAdd.setM_IsHead(true);
            }
            this.getM_Branches().put(branchEntry.getKey(),branchToAdd);
        }
    }

    public Commit getLastCommit() {
        Comparator<Commit> comparator = (o1, o2) -> {
            Commit firstNode = o1;
            Commit secondNode = o2;
            SimpleDateFormat general = new SimpleDateFormat(DATE_FORMAT);
            try {
                Date firstDate = general.parse(firstNode.getM_DateOfCreation());
                Date secondDate = general.parse(secondNode.getM_DateOfCreation());
                if (firstDate.after(secondDate)) {
                    return -1;
                } else if (firstDate.before(secondDate)) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        };

        Stream<Map.Entry<String, Commit>> sorted =
                m_Commits.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(comparator));

        return sorted.findFirst().get().getValue();
    }

    public void setHeadBranch(Branch i_HeadBranch){
        m_HeadBranch = i_HeadBranch;
    }

    public Branch getHeadBranch(){
        Branch headBranch = null;
        for(Map.Entry<String,Branch> entryBranch : m_Branches.entrySet()){
            if(entryBranch.getValue().isM_IsHead()){
                headBranch = entryBranch.getValue();
            }
        }
        return headBranch;
    }

    public ViewMagitFileNode buildTreeViewOfCommitFile(String i_CommidSHA1){
        Commit currentCommit = m_Commits.get(i_CommidSHA1);
        Folder rootFolder = m_Folders.get(currentCommit.getM_RootFolderId());
        String folderName = m_Name;
        ViewMagitFileNode rootNode = new ViewMagitFileNode(folderName,folderName,true, null);

        buildTreeViewOfCommitFilesRec(rootFolder, rootNode);

        return rootNode;
    }

    private void buildTreeViewOfCommitFilesRec(Folder CurrentFolder,ViewMagitFileNode rootNode) {
        for (Map.Entry<String ,Folder.Item> entryItem: CurrentFolder.getM_Items().entrySet()) {
            Folder.Item item = entryItem.getValue();
            if(item.getM_Type() == Folder.Item.eItemType.BLOB) {
                ViewMagitFileNode newNode = new ViewMagitFileNode(null,item.getM_Name(),false, null);
                rootNode.getChildrens().add(newNode);
            }
            else{
                ViewMagitFileNode newNode = new ViewMagitFileNode(null,item.getM_Name(),true, null);
                rootNode.getChildrens().add(newNode);
                Folder nextFolder = m_Folders.get(item.getM_Id());
                buildTreeViewOfCommitFilesRec(nextFolder, newNode);
            }
        }
    }

    public ViewMagitFileNode buildTreeViewOfWCFiles() throws IOException {
        String rootFolderPath = m_Location;
        String folderName = m_Name;
        ViewMagitFileNode rootNode = new ViewMagitFileNode(folderName,folderName,true, m_Location);

        buildTreeViewOfWCFilesRec(rootFolderPath,rootNode);

        return rootNode;
    }

    private void buildTreeViewOfWCFilesRec(String i_CurrentPath, ViewMagitFileNode i_TreeItem) throws IOException {
        List<String> folderFileNames = Creator.getFilesAndFoldersNamesOfAllFilesInAFolder(i_CurrentPath);

        for (String fileInFolderName : folderFileNames) {
            if (!fileInFolderName.toLowerCase().equals(".magit")) {
                if (!fileInFolderName.toLowerCase().contains(".")) { // folder
                    String folderPath = Paths.get(i_CurrentPath, fileInFolderName).toString();
                    ViewMagitFileNode folderNode = new ViewMagitFileNode(fileInFolderName, fileInFolderName, true, folderPath);
                    i_TreeItem.getChildrens().add(folderNode);
                    buildTreeViewOfWCFilesRec(folderPath, folderNode);
                } else { // blob
                    String blobPath = Paths.get(i_CurrentPath, fileInFolderName).toString();
                    String blobContent = Creator.readFileAsString(blobPath);
                    ViewMagitFileNode fileNode = new ViewMagitFileNode(blobContent, fileInFolderName, false, blobPath);
                    i_TreeItem.getChildrens().add(fileNode);
                }
            }
        }

    }

    public String getCommitSha1(Commit i_CommitToFind) {
        for(Map.Entry<String,Commit> commitEntry : m_Commits.entrySet()){
            Commit currentCommit = commitEntry.getValue();
            if(currentCommit == i_CommitToFind){
                return commitEntry.getKey();
            }
        }
        return  null;
    }

    public void PushBranchToRR(String i_NewBrunchName, String i_PointedCommitSha1) throws IOException {
        //add brunch to RR
        Path RRLocation = Paths.get(m_RemoteRepositoryLocation);
        Path LRLocation = Paths.get(m_Location + "/.magit/branches/" + i_NewBrunchName);
        String remoteBranchToTrackAfter = this.addBrunchToRR(i_NewBrunchName, LRLocation, RRLocation);

        //change in LR this brunch to RTB
        String path = RRLocation.toString();
        String idStr = path.substring(path.lastIndexOf('\\') + 1);
        Path LRTLocation = Paths.get(m_Location + "/.magit/branches/" + idStr + "/" + i_NewBrunchName);
        Files.copy(LRLocation, LRTLocation);
        this.updateTrackingAfterBranch(i_NewBrunchName, remoteBranchToTrackAfter);

    }

    public void updateTrackingAfterBranch(String i_newBrunchName, String i_remoteBranchToTrackAfter) {
        String pointedCommit = m_Branches.get(i_remoteBranchToTrackAfter).getM_PointedCommitId();
        String trackingAfter = m_Branches.get(i_remoteBranchToTrackAfter).getM_Name();
        Branch updeteBranchToAdd = m_Branches.get(i_newBrunchName);
        updeteBranchToAdd.setM_Tracking(true);
        updeteBranchToAdd.setM_TrackingAfter(trackingAfter);
        updeteBranchToAdd.setM_PointedCommitId(pointedCommit);
    }

    private String addBrunchToRR(String m_newBrunchName,Path i_LRLocation, Path i_RRLocation) throws IOException {
        boolean isExist = false;
        Path rrPathToBrunch = Paths.get(i_RRLocation + "/.magit/branches/" + m_newBrunchName);
        Files.copy(i_LRLocation, rrPathToBrunch);

        String brunchCommitSha = m_Branches.get(m_newBrunchName).getM_PointedCommitId();
        Path lrPathToCommit = Paths.get(m_Location + "/.magit/objects/" + brunchCommitSha);
        Path rrPathToCommit = Paths.get(i_RRLocation + "/.magit/objects");
        File rrFolder = new File(rrPathToCommit.toString());

        for (File fileEntry : rrFolder.listFiles()) {
            if (fileEntry.getName().equals(brunchCommitSha)) {
                isExist = true;
                break;
            }
        }
        if(!isExist) {
            rrPathToCommit = Paths.get(i_RRLocation + "/.magit/objects/"+ brunchCommitSha);
            Files.copy(lrPathToCommit, rrPathToCommit);
            rrPathToCommit = Paths.get(i_RRLocation + "/.magit/objects");
            addHeadFolderToRR(brunchCommitSha, rrPathToCommit);
        }

        return m_newBrunchName;
    }

    private void addHeadFolderToRR(String brunchCommitSha, Path i_RRLocation) throws IOException {

        boolean isExist = false;
        File rrFolder = new File(i_RRLocation.toString());
        String headFolderSHA = null;
        headFolderSHA = m_Commits.get(brunchCommitSha).getM_RootFolderId();
        for (File fileEntry : rrFolder.listFiles()) {
            if (fileEntry.getName().equals(headFolderSHA)) {
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            Path lrPathToHeadFolder = Paths.get(m_Location + "/.magit/objects/" + headFolderSHA);
            Path rrPathToHeadFolder = Paths.get(i_RRLocation + "/" + headFolderSHA);
            Files.copy(lrPathToHeadFolder, rrPathToHeadFolder);
        }

        Folder headFolder = m_Folders.get(headFolderSHA);
        Map<String, Folder.Item> itemHeadFolder = headFolder.getM_ItemsClone();
        checkItemsInRR(rrFolder.listFiles(), itemHeadFolder, i_RRLocation);
    }

    private void checkItemsInRR(File[] i_RRItems, Map<String, Folder.Item> i_LRItems , Path i_RRLocation) throws IOException {
        boolean isExist = false;

        for (Map.Entry<String, Folder.Item> entry : i_LRItems.entrySet()) {
            String k = entry.getKey();
            Folder.Item v = entry.getValue();
            for (File fileEntry : i_RRItems) {
                if (k.equals(fileEntry.getName())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Path lrPathItem = Paths.get(m_Location + "/.magit/objects/" + k);
                Path rrPathItem = Paths.get(i_RRLocation + "/" + k);
                Files.copy(lrPathItem, rrPathItem);
                if (v.getM_Type() == Folder.Item.eItemType.FOLDER) {
                    Folder headFolder = m_Folders.get(k);
                    Map<String, Folder.Item> itemFolder = headFolder.getM_ItemsClone();
                    newFolderInRR(itemFolder, i_RRLocation);
                }
            }
            isExist = false;

        }
    }

    private void newFolderInRR(Map<String, Folder.Item> i_Item ,Path i_RRLocation ) throws IOException {
        for(Map.Entry<String, Folder.Item> entry : i_Item.entrySet()){
            String k = entry.getKey();
            Folder.Item v = entry.getValue();
            Path lrPathItem = Paths.get(m_Location + "/.magit/objects/" + k);
            Path rrPathItem = Paths.get(i_RRLocation + "/" + k);
            Files.copy(lrPathItem, rrPathItem);
            if (v.getM_Type() == Folder.Item.eItemType.FOLDER) {
                Folder headFolder = m_Folders.get(k);
                Map<String, Folder.Item> itemFolder = headFolder.getM_ItemsClone();
                newFolderInRR(itemFolder, i_RRLocation);
            }
        }
    }

    public void deleteBranch(Branch i_BranchToDelete,boolean i_IsTracking) {
        String remoteBranch = null;
        String remoteRepoName = null ;

        //אם הוא RTB נמחוק את הRB בנוסף
        if(i_IsTracking){
            remoteBranch = i_BranchToDelete.getM_TrackingAfter();
            remoteRepoName = extractRepositoryNameFromPath(this.getM_RemoteRepositoryLocation());
            this.getM_Branches().remove(remoteBranch);
            File remoteBranchToDelete = new File(String.valueOf(Paths.get(this.getM_Location(),
                    ".magit", "branches",remoteRepoName,i_BranchToDelete.getM_Name())));
            FileUtils.deleteQuietly(remoteBranchToDelete);
        }

        File branchToDelete = new File(String.valueOf(Paths.get(this.getM_Location(),
                ".magit", "branches", i_BranchToDelete.getM_Name())));
        FileUtils.deleteQuietly(branchToDelete);
        this.getM_Branches().remove(i_BranchToDelete.getM_Name());
    }

    private String extractRepositoryNameFromPath(String i_RepositoryFullPath){
        String name = i_RepositoryFullPath;
        int indexName = name.lastIndexOf("/");
        if(indexName == -1){
            indexName = name.lastIndexOf("\\");
        }
        return name.substring(indexName + 1);
    }

    public void getChangedCommitsAfterPullRequest(List<String> i_CommitsSHA1List, List<String> i_CommitsInfo, Branch i_BaseBranch, Branch i_TargetBranch){
        Commit baseCommit = getM_Commits().get(i_BaseBranch.getM_PointedCommitId());
        Commit targetCommit = getM_Commits().get(i_TargetBranch.getM_PointedCommitId());
        getCommitsInformation(i_CommitsSHA1List, i_CommitsInfo, baseCommit, targetCommit);
    }

    private void getCommitsInformation(List<String> i_CommitsSHA1List, List<String> i_CommitsInformation, Commit i_BaseCommit, Commit  i_TargetCommit){
        String baseCommitSha1 = getCommitSha1(i_BaseCommit);
        String targetCommitSha1 = getCommitSha1(i_TargetCommit);
        if(!baseCommitSha1.equals(targetCommitSha1)){
            i_CommitsInformation.add(i_TargetCommit.toString());
            i_CommitsSHA1List.add(targetCommitSha1);

            for(String precendingCommitSha1 : i_TargetCommit.getM_PrecedingCommitId()) {
                if (precendingCommitSha1 != null && !precendingCommitSha1.isEmpty()) {
                    Commit firstNextCommit = getM_Commits().get(precendingCommitSha1);
                    getCommitsInformation(i_CommitsSHA1List, i_CommitsInformation, i_BaseCommit, firstNextCommit);
                }
            }

            /*if(i_TargetCommit.getSecondPrecedingSha1() != null && !i_TargetCommit.getSecondPrecedingSha1().isEmpty()){
                Commit secondNextCommit = getCommits().get(i_TargetCommit.getSecondPrecedingSha1());
                getCommitsInformation(i_CommitsSHA1List, i_CommitsInformation, i_BaseCommit, secondNextCommit);
            }*/
        }
    }

}
