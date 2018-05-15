package com.example.android.lovemeter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseActivity extends AppCompatActivity {
    @BindView(R.id.btnTestAFriend)
    Button btnTestAFriend;
    @BindView(R.id.btnCheckYourBoard)
    Button btnCheckYourBoard;
    @BindView(R.id.imageView)
    ImageView imageView;
    String userEmail;

    public static void start(Context context, String profilePicture,String userEmail) {
        Intent starter = new Intent(context, ChooseActivity.class);
        starter.putExtra("profilePicture", profilePicture);
        starter.putExtra("userEmail", userEmail);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra(getString(R.string.profilepicture)) != null) {
            Glide.with(this).load(Uri.parse(intent.getStringExtra(getString(R.string.profilepicture)))).into(imageView);
            imageView.setRotation(90);
            userEmail = intent.getStringExtra(getString(R.string.useremail));
        }
    }

    @OnClick({R.id.btnTestAFriend, R.id.btnCheckYourBoard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnTestAFriend:
                ListOfFriendsActivity.start(this);
                break;
            case R.id.btnCheckYourBoard:
                BoardActivity.start(this,userEmail);
                break;
        }
    }
}
