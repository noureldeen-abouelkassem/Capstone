package com.example.android.lovemeter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListOfFriendsActivity extends AppCompatActivity {

    @BindView(R.id.rvListOfFriends)
    RecyclerView rvListOfFriends;
    private String userEmail;
    FriendsAdapter friendsAdapter;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;
    private static DataSnapshot dataSnapshot;
    private long testsCount;

    public long getTestsCount() {
        return testsCount;
    }

    public void setTestsCount(long testsCount) {
        this.testsCount = testsCount;
    }

    public static DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public static void setDataSnapshot(DataSnapshot dataSnap) {
       dataSnapshot = dataSnap;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ListOfFriendsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_friends);
        ButterKnife.bind(this);
        friendsAdapter = new FriendsAdapter(this, MainActivity.getUserModels(), new FriendsAdapter.FriendsClick() {
            @Override
            public void OnClick(int position, String userEmail) {
                setUserEmail(userEmail);
                mfirebaseDatabase = FirebaseDatabase.getInstance();
                mdatabaseReference = mfirebaseDatabase.getReference("users");
                mdatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot local : dataSnapshot.getChildren()) {
                            UserModel userModel = local.getValue(UserModel.class);
                            if (userModel != null && userModel.getmEmail().equals(getUserEmail())) {
                                setTestsCount(local.child("tests").getChildrenCount());
                                setDataSnapshot(local);
                                TestActivity.start(ListOfFriendsActivity.this,getTestsCount(),userModel.getmFirstName(),userModel.getmSecondName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        rvListOfFriends.setAdapter(friendsAdapter);
        rvListOfFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvListOfFriends.setHasFixedSize(true);
    }

}
