package vamsidesu5.com.spokesv2.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vamsidesu5.com.spokesv2.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Notification> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sendUser;
        public TextView timestamp;

        public MyViewHolder(View view) {
            super(view);
            sendUser = (TextView) view.findViewById(R.id.sendUser);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
        }
    }

    public RecyclerViewAdapter(List<Notification> updatedList) {
        this.list = updatedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification notification = list.get(position);
        holder.sendUser.setText(notification.getSendUser());
        holder.timestamp.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}