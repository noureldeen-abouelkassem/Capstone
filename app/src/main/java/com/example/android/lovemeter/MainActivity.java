package com.example.android.lovemeter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.main_activity_btn_logIn)
    Button mainActivityBtnLogIn;
    @BindView(R.id.main_activity_logo)
    ImageView mainActivityLogo;
    @BindView(R.id.main_activity_tv_signUp)
    TextView mainActivityTvSignUp;
    @BindView(R.id.main_activity_et_userName)
    EditText mainActivityEtUserName;
    @BindView(R.id.main_activity_et_password)
    EditText mainActivityEtPassword;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;
    private static List<UserModel> userModels = new ArrayList<>();

    public static List<UserModel> getUserModels() {
        return userModels;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
    private class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            mfirebaseDatabase = FirebaseDatabase.getInstance();
            mdatabaseReference = mfirebaseDatabase.getReference("users");
            mdatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot local : dataSnapshot.getChildren()) {
                        UserModel userModel = local.getValue(UserModel.class);
                        userModels.add(userModel);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new MyAsyncTask().execute();
    }

    @OnClick({R.id.main_activity_btn_logIn, R.id.main_activity_tv_signUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_activity_btn_logIn:
                UserModel localUserModel = new UserModel();
                for (UserModel userModel : userModels) {
                    if (mainActivityEtUserName.getText().toString().equals(userModel.getmUserName())) {
                        localUserModel = userModel;
                    }
                }
                if ((localUserModel.getmPassword() != null) &&localUserModel.getmPassword().equals(mainActivityEtPassword.getText().toString())) {
                    ChooseActivity.start(this,localUserModel.getmProfilePicture(),localUserModel.getmEmail());
                }else{
                    Toast.makeText(this, R.string.usernameorpasswordiswrong, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.main_activity_tv_signUp:
                SignUpActivity.start(this);
                break;
        }
    }
}
