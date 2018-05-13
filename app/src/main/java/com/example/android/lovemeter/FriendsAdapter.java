package com.example.android.lovemeter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noureldeen on 1/20/2018.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {
    private final Context context;
    private final List<UserModel> userModels;
    private final FriendsClick friendsClick;

    public interface FriendsClick {
        void OnClick(int position,String userEmail);
    }

    public FriendsAdapter(Context context, List<UserModel> userModels, FriendsClick friendsClick) {
        this.context = context;
        this.userModels = userModels;
        this.friendsClick = friendsClick;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final FriendsViewHolder holder, int position) {
        if (holder != null) {
            holder.bindFriend(userModels.get(holder.getAdapterPosition()).getmProfilePicture(), userModels.get(holder.getAdapterPosition()).getmFirstName() + " " + userModels.get(holder.getAdapterPosition()).getmSecondName(),userModels.get(holder.getAdapterPosition()).getmEmail());
            holder.userProfilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friendsClick.OnClick(holder.getAdapterPosition(),userModels.get(holder.getAdapterPosition()).getmEmail());
                }
            });
            holder.userFullName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friendsClick.OnClick(holder.getAdapterPosition(),userModels.get(holder.getAdapterPosition()).getmEmail());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivUserProfilePicture)
        ImageView userProfilePicture;
        @BindView(R.id.tvUserFullName)
        TextView userFullName;
        @BindView(R.id.tvUserEmail)
        TextView userEmail;

        FriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindFriend(String userPP, String userName,String userMail) {
            Glide.with(context).load(userPP).into(userProfilePicture);
            userFullName.setText(userName);
            userEmail.setText(userMail);
        }
    }
}
