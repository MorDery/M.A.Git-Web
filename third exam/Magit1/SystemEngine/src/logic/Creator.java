package logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Creator {

    public static  void createParentsFoldersByPath(String path) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    public static void createWC(MAGit i_MyMAGit) {
        String folderSHA = null;
        String commitSHA = null;
        Map<String, Folder.Item> items = null;
        String pathWC = "WC";

        Path wcPath = Paths.get(i_MyMAGit.getActiveRepository().getM_Location() + "/" + pathWC);
        try {
            Files.createDirectories(wcPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Branch> branch : i_MyMAGit.getActiveRepository().getM_Branches().entrySet()) {
            Branch v = branch.getValue();
            if (v.isM_IsHead()) {
                commitSHA = v.getM_PointedCommitId();
                break;
            }
        }
        if (commitSHA != null && !(commitSHA.equals(""))) {
            folderSHA = i_MyMAGit.getActiveRepository().getM_Commits().get(commitSHA).getM_RootFolderId();
        }

        if (folderSHA != null ) {
            items = i_MyMAGit.getActiveRepository().getM_Folders().get(folderSHA).getM_Items();
            if (items != null) {
                createItemsInWC(i_MyMAGit, items, wcPath );
            }
        }
    }

    public static void createItemsInWC(MAGit i_MyMAGit, Map<String, Folder.Item> i_MyItems, Path i_Path) {
        for (Map.Entry<String, Folder.Item> entry : i_MyItems.entrySet()) {
            Folder.Item v = entry.getValue();
            if (v.getM_Type() == Folder.Item.eItemType.BLOB) {
                findBlob(v.getM_Id(), i_MyMAGit.getActiveRepository().getM_Blobs(), Paths.get(i_Path + "/" + v.getM_Name()));
            } else {
                findFolder(i_MyMAGit, v.getM_Id(), i_MyMAGit.getActiveRepository().getM_Folders(), Paths.get(i_Path + "/" + v.getM_Name()));
            }
        }

    }

    public static void findBlob (String i_Id, Map < String, Blob > i_Blobs, Path i_Path ){
        Writer.writeBlob(i_Path, i_Blobs.get(i_Id));
    }

    public static void findFolder (MAGit i_MyMAGit,String i_Id, Map < String, Folder > i_Folders, Path i_Path ) {
        try {
            Files.createDirectories(i_Path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        createItemsInWC(i_MyMAGit, i_Folders.get(i_Id).getM_Items(), i_Path);
    }

    public static List<String> getFileNamesOfFilesInAFolder(String i_FolderFullPath) {
        List<String> result = new ArrayList<String>();

        File[] files = new File(i_FolderFullPath).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                result.add(file.getName());
            }
        }

        return result;
    }

    public static Commit createCommitFromSplittedCOntent(String [] commitContent) {
        String rootFolderId = commitContent[0];
        String precedingCommitId = commitContent[1];
        String message = commitContent[2];
        String author = commitContent[3];
        String dateOfCreation = commitContent[4].trim();
        Commit commit = new Commit(rootFolderId,message,author,dateOfCreation,precedingCommitId);
        return commit;
    }

    public static List<String> getFilesAndFoldersNamesOfAllFilesInAFolder(String i_FolderFullPath) {
        List<String> result = new ArrayList<String>();

        File[] files = new File(i_FolderFullPath).listFiles();

        for (File file : files) {
            result.add(file.getName());
        }

        return result;
    }

    public static String readFileAsString(String i_FilePaths)throws IOException {
        return new String(Files.readAllBytes(Paths.get(i_FilePaths)));
    }

    public  static void createRepository(Repository i_RepositoryToCreate) {
        String pathObjects = ".magit/objects";
        String pathBranches = ".magit/branches";
        String pathMagit = ".magit";
        String pathRemoteRepository = null ;
        Path objectsPath = Paths.get(i_RepositoryToCreate.getM_Location() + "/" + pathObjects);
        Path branchesPath = Paths.get(i_RepositoryToCreate.getM_Location() + "/" + pathBranches);
        Path magitPath = Paths.get(i_RepositoryToCreate.getM_Location() + "/" + pathMagit);
        Path remoteRepositoryBranchesPath = null;
        boolean isRemoteRepository = false ;

        if(i_RepositoryToCreate.getM_RemoteRepositoryLocation() != null){
            isRemoteRepository = true ;
            pathRemoteRepository = extractRepositoryNameFromPath(i_RepositoryToCreate.getM_RemoteRepositoryLocation());
            remoteRepositoryBranchesPath = Paths.get( branchesPath + "/" + pathRemoteRepository);
        }

        try {
            Files.createDirectories(objectsPath);
            Files.createDirectories(branchesPath);
            if(isRemoteRepository){
                Files.createDirectories(remoteRepositoryBranchesPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        i_RepositoryToCreate.getM_Blobs().forEach((k, v) ->
        {
            FileZipper.Zip(Paths.get(objectsPath + "/" + k), Writer.writeBlob(v));
        });

        i_RepositoryToCreate.getM_Folders().forEach((k, v) ->
        {
            FileZipper.Zip(Paths.get(objectsPath + "/" + k), Writer.writeFolder(v));
        });

        i_RepositoryToCreate.getM_Commits().forEach((k, v) ->
        {
            FileZipper.Zip(Paths.get(objectsPath + "/" + k), Writer.writeCommit(v));
        });
        boolean finalIsRemoteRepository = isRemoteRepository;
        Path finalRemoteRepositoryBranchesPath = remoteRepositoryBranchesPath;
        i_RepositoryToCreate.getM_Branches().forEach((k, v) ->
        {
            try {
                if(v.isM_IsHead()) {
                    Files.write(Paths.get(branchesPath + "/" + "Head.txt"), k.getBytes());
                }
                try {
                    Files.write(Paths.get(branchesPath + "/" + k), v.getM_PointedCommitId().getBytes());
                }
                catch (Exception ex){
                    if(finalIsRemoteRepository){
                        Files.write(Paths.get(finalRemoteRepositoryBranchesPath + "/" + extractNameFromGeneralString(k)), v.getM_PointedCommitId().getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try{
            if(finalIsRemoteRepository){
                Files.write(Paths.get(magitPath + "/" + "RemoteRepository.txt"), i_RepositoryToCreate.getM_RemoteRepositoryLocation().getBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String extractNameFromGeneralString(String i_CloneFullPathFromUser) {
        String separator = "\\";
        String[] splittedPathContent = i_CloneFullPathFromUser.split(Pattern.quote(separator));
        return splittedPathContent[splittedPathContent.length - 1 ];
    }

    private static String extractRepositoryNameFromPath(String i_RepositoryFullPath){
        String name = i_RepositoryFullPath;
        int indexName = name.lastIndexOf("/");
        if(indexName == -1){
            indexName = name.lastIndexOf("\\");
        }
        return name.substring(indexName + 1);
    }
}
