package com.example.android.lovemeter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.lovemeter.data.ResultsContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardActivity extends AppCompatActivity {

    @BindView(R.id.rv_myTest)
    RecyclerView rvMyTest;
    @BindView(R.id.tv_noTests)
    TextView tvNoTests;
    public String userEmail;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public static void start(Context context, String userEmail) {
        Intent starter = new Intent(context, BoardActivity.class);
        starter.putExtra("userEmail", userEmail);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("userEmail");
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("users");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot local : dataSnapshot.getChildren()) {
                    UserModel userModel = local.getValue(UserModel.class);
                    if (userModel.getmEmail().equals(userEmail)) {
                        long testsCount = local.child("tests").getChildrenCount();
                        List<TestModel> testModels = new ArrayList<>();
                        if (testsCount == 0) {
                            tvNoTests.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < testsCount; i++) {
                                QuestionModel testerModel = local.child("tests").child("test " + String.valueOf(i + 1)).getChildren().iterator().next().child("friendName").getValue(QuestionModel.class);
                                String testerName = testerModel.getQuestion() + " " + testerModel.getAnswer();
                                String testNumber = "test " + String.valueOf(i + 1);
                                String[] args = new String[]{testNumber};
                                Cursor cursor = getContentResolver().query(ResultsContract.ResultEntry.CONTENT_URI, null,  ResultsContract.ResultEntry.COLUMN_TEST_NAME + "=?", args, null);
                                if (cursor != null && cursor.getCount() == 0) {
                                    testModels.add(new TestModel(testerName + " (NEW) ", testNumber));
                                    cursor.close();
                                }else if(cursor!=null){
                                    testModels.add(new TestModel(testerName, testNumber));
                                    cursor.close();
                                }
                            }
                            TestsAdapter testsAdapter = new TestsAdapter(BoardActivity.this, testModels, new TestsAdapter.TestsClick() {
                                @Override
                                public void OnClick(int position, String testerName, String testNumber) {
                                    AnswerActivity.start(BoardActivity.this, testNumber, userEmail, testerName);
                                }
                            });
                            if(testsAdapter.getItemCount()==0){
                                tvNoTests.setVisibility(View.VISIBLE);
                            }else {
                                rvMyTest.setAdapter(testsAdapter);
                                rvMyTest.setLayoutManager(new LinearLayoutManager(BoardActivity.this, LinearLayoutManager.VERTICAL, false));
                                rvMyTest.hasFixedSize();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
