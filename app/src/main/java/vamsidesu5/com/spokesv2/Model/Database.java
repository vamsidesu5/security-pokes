package vamsidesu5.com.spokesv2.Model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private DatabaseReference ref;

    public Database(String path) {
        ref = FirebaseDatabase.getInstance().getReference(path);
    }

    public void updateChild(Map<String, Object> payload) {
        ref.updateChildren(payload);
    }

    public Map<String, Object> constructPayload (String node, Object data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(node, data);
        return payload;
    }

    public Map<String, Object> constructPayload(List<String> nodes, List<Object> data) {
        Map<String, Object> payload = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            payload.put(nodes.get(i), data.get(i));
        }
        return payload;
    }

    public void updateDatabasePath(String path) {
        ref = FirebaseDatabase.getInstance().getReference(path);
    }

    public DatabaseReference getRef() {
        return ref;
    }
}
