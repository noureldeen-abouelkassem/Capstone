package com.example.android.lovemeter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.et_TestText)
    EditText etTestText;
    @BindView(R.id.rb_No)
    RadioButton rbNo;
    @BindView(R.id.rb_Yes)
    RadioButton rbYes;
    @BindView(R.id.btnAddToTest)
    Button btnAddToTest;
    @BindView(R.id.radioGroup2)
    RadioGroup radioGroup2;
    @BindView(R.id.tvLast)
    TextView tvLast;
    private DataSnapshot dataSnapshot;
    private long testsCount;
    private int counter = 1;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Map<String, QuestionModel> stringAnswerModelMap = new HashMap<>();
    private String friendFirstName;
    private String friendSecondName;

    public String getFriendSecondName() {
        return friendSecondName;
    }

    public void setFriendSecondName(String friendSecondName) {
        this.friendSecondName = friendSecondName;
    }

    public String getFriendFirstName() {
        return friendFirstName;
    }

    public void setFriendFirstName(String friendFirstName) {
        this.friendFirstName = friendFirstName;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        if (counter <= 10) {
            this.counter = counter;
        }
    }

    public long getTestsCount() {
        return testsCount;
    }

    public void setTestsCount(long testsCount) {
        this.testsCount = testsCount;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }

    public static void start(Context context, long testsCount,String friendFirstName,String friendSecondName) {
        Intent starter = new Intent(context, TestActivity.class);
        starter.putExtra("testsCount", testsCount);
        starter.putExtra("friendFirstName",friendFirstName);
        starter.putExtra("friendSecondName",friendSecondName);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        btnAddToTest.setText(getString(R.string.btnAddQuestion) + " " + getCounter());
        setDataSnapshot(ListOfFriendsActivity.getDataSnapshot());
        Intent intent = getIntent();
        if (intent != null) {
            setTestsCount(intent.getLongExtra("testsCount", 0));
            setFriendFirstName(intent.getStringExtra("friendFirstName"));
            setFriendSecondName(intent.getStringExtra("friendSecondName"));
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = dataSnapshot.getRef();
        if (getTestsCount() == 0) {
            databaseReference = dataSnapshot.getRef().child("tests").child("test" + " " + "1");
        } else {
            databaseReference = dataSnapshot.getRef().child("tests").child("test" + " " + String.valueOf(getTestsCount() + 1));
        }
    }

    @OnClick(R.id.btnAddToTest)
    public void onViewClicked() {
        if ((etTestText.getText().toString().isEmpty()) || ((!rbYes.isChecked()) && (!rbNo.isChecked()))) {
            Toast.makeText(this, "You should provide a question and an answer", Toast.LENGTH_SHORT).show();
        } else {
            if (getCounter() < 9) {
                stringAnswerModelMap.put(String.valueOf(getCounter()), new QuestionModel(etTestText.getText().toString(),rbYes.isChecked() ? "1" : "0"));
                clearViews();
                setCounter(getCounter() + 1);
                btnAddToTest.setText(getString(R.string.btnAddQuestion) + " " + getCounter());
            }else if (getCounter() == 9) {
                btnAddToTest.setText(getString(R.string.addLastQuestion));
                stringAnswerModelMap.put(String.valueOf(getCounter()), new QuestionModel(etTestText.getText().toString(),rbYes.isChecked() ? "1" : "0"));
                clearViews();
                setCounter(getCounter() + 1);
            } else if (getCounter() == 10) {
                stringAnswerModelMap.put(String.valueOf(getCounter()), new QuestionModel(etTestText.getText().toString(),rbYes.isChecked() ? "1" : "0"));
                stringAnswerModelMap.put("friendName",new QuestionModel(getFriendFirstName(),getFriendSecondName()));
                databaseReference.push().setValue(stringAnswerModelMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ListOfFriendsActivity.start(TestActivity.this);
                    }
                });
            }
        }
    }

    void clearViews() {
        etTestText.setText("");
        radioGroup2.clearCheck();
    }
}
