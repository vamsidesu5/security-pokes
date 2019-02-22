package vamsidesu5.com.spokesv2.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.widget.ListView;

import vamsidesu5.com.spokesv2.Model.User;

public class FriendsViewModel extends ViewModel {
    private User currUser = User.getInstance();

    public void updateFriendsList(ListView friendsList) {

    }
}
