package logic.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatUserManager {
    /*
    Adding and retrieving users is synchronized and in that manner - these actions are thread safe
    Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
    of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
     */

    private final Set<String> usersSet;

    public ChatUserManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
}
