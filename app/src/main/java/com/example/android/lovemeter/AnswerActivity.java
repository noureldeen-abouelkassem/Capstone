package com.example.android.lovemeter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import butterknife.OnClick;

public class AnswerActivity extends AppCompatActivity {

    @BindView(R.id.tvQuestion)
    TextView tvQuestion;
    @BindView(R.id.rbYes)
    RadioButton rbYes;
    @BindView(R.id.rbNo)
    RadioButton rbNo;
    @BindView(R.id.rgYesNo)
    RadioGroup rgYesNo;
    @BindView(R.id.btnAnswer)
    Button btnAnswer;
    String testNumber;
    String userEmail;
    String testerName;
    int testResult = 0;

    public String getTestNumber() {
        return testNumber;
    }

    public String getTesterName() {
        return testerName;
    }

    private int counter = 0;
    List<QuestionModel> questionModels = new ArrayList<>();

    public int getTestResult() {
        return testResult;
    }

    public void setTestResult(int testResult) {
        this.testResult = testResult;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        if (counter<=10) {
            this.counter = counter;
        }
    }

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    public static void start(Context context, String testNumber, String userEmail, String testerName) {
        Intent starter = new Intent(context, AnswerActivity.class);
        starter.putExtra("testNumber", testNumber);
        starter.putExtra("userEmail", userEmail);
        starter.putExtra("testerName", testerName);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            testNumber = intent.getStringExtra("testNumber");
            testerName = intent.getStringExtra("testerName");
            userEmail = intent.getStringExtra("userEmail");
            testerName = testerName.replace("(NEW)","");
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("users");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot local : dataSnapshot.getChildren()) {
                    UserModel userModel = local.getValue(UserModel.class);
                    if (userModel.getmEmail().equals(userEmail)) {
                        for (int i = 1; i <= 10; i++) {
                            QuestionModel questionModel = local.child("tests").child(testNumber).getChildren().iterator().next().child(String.valueOf(i)).getValue(QuestionModel.class);
                            questionModels.add(questionModel);

                        }
                    }
                }
                tvQuestion.setText(questionModels.get(counter).getQuestion());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int calcResult(int result) {
        return (result * 100)/10;
    }

    @OnClick(R.id.btnAnswer)
    public void onViewClicked() {
        if (getCounter()<9) {
            if (questionModels.get(getCounter()).getAnswer().equals("1") && rbYes.isChecked()) {
                testResult += 1;
                setCounter(getCounter() + 1);
                tvQuestion.setText(questionModels.get(counter).getQuestion());
            } else if (questionModels.get(getCounter()).getAnswer().equals("0") && rbNo.isChecked()) {
                testResult += 1;
                setCounter(getCounter() + 1);
                tvQuestion.setText(questionModels.get(counter).getQuestion());
            } else if (!(rbYes.isChecked()) && !(rbNo.isChecked())) {
                Toast.makeText(this, "You should answer the question !", Toast.LENGTH_SHORT).show();
            } else if ((questionModels.get(getCounter()).getAnswer().equals("0") && rbYes.isChecked()) || (questionModels.get(getCounter()).getAnswer().equals("1") && rbNo.isChecked())) {
                setCounter(getCounter() + 1);
                tvQuestion.setText(questionModels.get(counter).getQuestion());
            }
            rgYesNo.clearCheck();
        }else if(getCounter()==9){
            if(!(rbYes.isChecked()) && !(rbNo.isChecked())){
                Toast.makeText(this, "You should answer the question !", Toast.LENGTH_SHORT).show();
            }else {
                if (questionModels.get(getCounter()).getAnswer().equals("1") && rbYes.isChecked()) {
                    testResult += 1;
                    setCounter(getCounter() + 1);
                } else if (questionModels.get(getCounter()).getAnswer().equals("0") && rbNo.isChecked()) {
                    testResult += 1;
                    setCounter(getCounter() + 1);
                } else if ((questionModels.get(getCounter()).getAnswer().equals("0") && rbYes.isChecked()) || (questionModels.get(getCounter()).getAnswer().equals("1") && rbNo.isChecked())) {
                    setCounter(getCounter() + 1);
                }
                rgYesNo.clearCheck();
                tvQuestion.setText("");
                btnAnswer.setText("Show Result !");
            }
        }else {
            tvQuestion.setText("you love your friend" + " " + testerName + " " + String.valueOf(calcResult(testResult)) + " %");
            ContentValues contentValues = new ContentValues();
            contentValues.put(ResultsContract.ResultEntry.COLUMN_TESTER_NAME, getTesterName());
            contentValues.put(ResultsContract.ResultEntry.COLUMN_TEST_NAME, getTestNumber());
            contentValues.put(ResultsContract.ResultEntry.COLUMN_RESULT, getTestResult());
            String[] args = new String[]{testNumber};
            Cursor cursor = getContentResolver().query(ResultsContract.ResultEntry.CONTENT_URI, null,  ResultsContract.ResultEntry.COLUMN_TEST_NAME + "=?", args, null);
            if(cursor!=null&&cursor.getCount()==0){
                getContentResolver().insert(ResultsContract.ResultEntry.CONTENT_URI, contentValues);
                cursor.close();
            }
        }
    }
}
