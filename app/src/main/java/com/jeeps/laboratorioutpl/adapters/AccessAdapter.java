package com.jeeps.laboratorioutpl.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jeeps.laboratorioutpl.R;
import com.jeeps.laboratorioutpl.model.access.AccessWithRoom;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccessAdapter extends RecyclerView.Adapter<AccessAdapter.AccessViewHolder> {

    private static final String ENTER = "ENTRADA";
    private static final String EXIT = "SALIDA";
    private List<AccessWithRoom> accessList;
    private Context context;

    public AccessAdapter(List<AccessWithRoom> accessList, Context context) {
        this.accessList = accessList;
        this.context = context;
    }

    @NonNull
    @Override
    public AccessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.access_list_item, parent, false);
        AccessViewHolder viewHolder = new AccessViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccessViewHolder holder, int position) {
        holder.bindAccess(accessList.get(position));
    }


    @Override
    public int getItemCount() {
        return accessList.size();
    }

    public class AccessViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.roomName) TextView roomName;
        @BindView(R.id.accessDate) TextView accessDate;
        @BindView(R.id.accessHour) TextView accessHour;
        @BindView(R.id.accessTypeImageView) ImageView accessTypeImageView;

        public AccessViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void bindAccess(AccessWithRoom access) {
            roomName.setText(access.getSala().getName());
            accessDate.setText(access.getDate());
            accessHour.setText(access.getHour());
            if (access.getTypeAccess().equals(ENTER))
                accessTypeImageView.setImageResource(R.drawable.enter);
            else if (access.getTypeAccess().equals(EXIT))
                accessTypeImageView.setImageResource(R.drawable.exit);
        }
    }
}
