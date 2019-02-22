package vamsidesu5.com.spokesv2.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class Database {
    private DatabaseReference ref;

    public Database(String path) {
        ref = FirebaseDatabase.getInstance().getReference(path);
    }

    public void updateChild(Map<String, Object> payload) {
        ref.updateChildren(payload);
    }
}
