package logic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Writer {

    public static String writeFolder(Folder i_Folder) {
        StringWriter sw = new StringWriter();
        i_Folder.getM_Items().forEach((k, v) ->
        {
            sw.write(v.getM_Name() + ";" + v.getM_Id() + ";" + v.getM_Type() + ";" + v.getLastUpdater() + ";" + v.getLastUpdateDate() + "\n");
        });
        return sw.toString();
    }

    public static String writeCommit(Commit i_Commit) {
        StringWriter sw = new StringWriter();
        try{
            sw.write(i_Commit.getM_RootFolderId() + ";" + i_Commit.getM_PrecedingCommitId().get(0) + ";" + i_Commit.getM_Message() + ";" + i_Commit.getM_Author() + ";" + i_Commit.getM_DateOfCreation() + "\n");
        }
        catch (Exception ex){ //if there's no precending commits - simply write an empty string
            sw.write(i_Commit.getM_RootFolderId() + ";" + "" + ";" + i_Commit.getM_Message() + ";" + i_Commit.getM_Author() + ";" + i_Commit.getM_DateOfCreation() + "\n");
        }
        return sw.toString();
    }

    public static String writeBlob(Blob i_Blob) {
        StringWriter sw = new StringWriter();
        sw.write(i_Blob.getM_Content());

        return sw.toString();
    }

    public static void writeBlob (Path i_Path, Blob i_Blob){
        try {
            Files.write(i_Path, i_Blob.getM_Content().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void WriteToTextFile(Path textFilePath,String i_ContentToAdd) throws IOException {
        Files.write(textFilePath,i_ContentToAdd.getBytes());
    }

    public static void WriteBranchSHA1ToFile(Path i_Path,String i_PointedCommitSha1) throws IOException {
        Files.delete(i_Path);
        WriteToTextFile(i_Path,i_PointedCommitSha1);
    }
    public static void writeToFile(String i_FileName, String i_Content) throws IOException{
        try (java.io.Writer out1 = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(i_FileName), "UTF-8")))
        {
            out1.write(i_Content);
        }
        catch (IOException e) {
            throw e;
        }
    }

}
