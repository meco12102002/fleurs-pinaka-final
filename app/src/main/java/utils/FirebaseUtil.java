package utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {

    // Firebase instances as singletons
    private static final FirebaseAuth authInstance = FirebaseAuth.getInstance();
    private static final FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();

    /**
     * Method to get the current user's UID.
     *
     * @return UID of the currently logged-in user or null if not logged in.
     */
    public static String currentUserID() {
        return authInstance.getUid();
    }

    /**
     * Method to get a reference to the current user's data in the database.
     *
     * @return DatabaseReference pointing to the user's data or null if not authenticated.
     * @throws IllegalStateException if there is no authenticated user.
     */
    public static DatabaseReference currentUser() {
        String userId = currentUserID();
        if (userId != null) {
            return databaseInstance.getReference("users").child(userId);
        } else {
            throw new IllegalStateException("No authenticated user. User ID is null.");
        }
    }
}
