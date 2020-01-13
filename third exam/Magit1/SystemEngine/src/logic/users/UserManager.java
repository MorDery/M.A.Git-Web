package logic.users;

import logic.MAGit;
import java.util.*;

public class UserManager {

    private final Map<String,MAGit> m_UserToMagitMap;

    public UserManager() {
        m_UserToMagitMap = new HashMap<>();
    }

    public synchronized void addUser(String username) {
        MAGit newMagit = new MAGit(username);
        m_UserToMagitMap.put(username,newMagit);
    }

    public boolean isUserExists(String username) {
        return m_UserToMagitMap.containsKey(username);
    }

    public List<String> getListOfUsersNamesWithRepositories(){
        List<String> usersNames =  new ArrayList<>();

        for (Map.Entry<String,MAGit> magit: m_UserToMagitMap.entrySet()) {
            if(magit.getValue().getRepositories().size() > 0) {
                usersNames.add(magit.getValue().getM_ActiveUserName());
            }
        }

        return usersNames;
    }

    public MAGit getMagitUser(String userName) {
        return m_UserToMagitMap.get(userName);
    }
}