package vamsidesu5.com.spokesv2.Model;

import android.graphics.Color;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import vamsidesu5.com.spokesv2.R;

public class FriendsViewAdapter extends RecyclerView.Adapter<FriendsViewAdapter.ViewHolder> {

    private List<String> list;
    private int curr = 1;



    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button friendButton;

        public ViewHolder(View view) {
            super(view);
            friendButton = (Button) view.findViewById(R.id.friendButton);
            //colorArray[0] = -16711681;
            //colorArray[1] = -16711936;
            //colorArray[2] = -65281;
        }
    }

    public FriendsViewAdapter(List<String> updatedList) {
        this.list = updatedList;
        //colors.add(colorArray[++curr]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String s = list.get(position);
        holder.friendButton.setText(s);
        holder.friendButton.setTextColor(Color.WHITE);
        Log.i("this is curr", curr + "");
        switch (curr % 5) {
            case 0:
                Log.i("this is curr2", curr + "");
                holder.friendButton.setBackgroundColor(Color.rgb(103, 58, 183));
                break;

            case 1:
                Log.i("this is curr1", curr + "");
                holder.friendButton.setBackgroundColor(Color.rgb(156, 39, 176));
                break;

            case 2:
                Log.i("this is curr3", curr + "");
                holder.friendButton.setBackgroundColor(Color.rgb(3, 169, 244));
                break;

            case 3:
                Log.i("this is curr4", curr + "");
                holder.friendButton.setBackgroundColor(Color.rgb(33, 150, 243));
                break;

            case 4:
                holder.friendButton.setBackgroundColor(Color.rgb(63, 81, 181));
                break;
        }
        curr++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}