package com.example.android.lovemeter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.sign_up_firstName)
    EditText signUpFirstName;
    @BindView(R.id.sign_up_secondName)
    EditText signUpSecondName;
    @BindView(R.id.sign_up_userName)
    EditText signUpUserName;
    @BindView(R.id.sign_up_password)
    EditText signUpPassword;
    @BindView(R.id.sign_up_email)
    EditText signUpEmail;
    @BindView(R.id.sign_up_phoneNumber)
    EditText signUpPhoneNumber;
    @BindView(R.id.sign_up_btn_takePhoto)
    Button signUpBtnTakePhoto;
    @BindView(R.id.sign_up_btn_pickAPhoto)
    Button signUpBtnPickAPhoto;
    @BindView(R.id.sign_up_btn_signUp)
    Button signUpBtnSignUp;
    @BindView(R.id.sign_up_profilePicture)
    ImageView signUpIvProfilePicture;
    private static final int CAMERA_REQUEST = 1313;
    private static final int PICK_IMAGE = 1414;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private RequestPermissionHandler requestPermissionHandler;
    private Uri imageUriForFirebase = null;

    public static void start(Context context) {
        Intent starter = new Intent(context, SignUpActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mdatabaseReference = mfirebaseDatabase.getReference("users");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("users_pp");
        requestPermissionHandler = new RequestPermissionHandler();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, getString(R.string.title), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && data.getExtras() != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            signUpIvProfilePicture.setImageBitmap(bitmap);
            signUpIvProfilePicture.setRotation(90);
            Uri tempUri = getImageUri(getApplicationContext(), bitmap);
            File finalFile = new File(getRealPathFromURI(tempUri));
            imageUriForFirebase = Uri.parse(finalFile.toURI().toString());
        } else if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                signUpIvProfilePicture.setImageBitmap(selectedImage);
                imageUriForFirebase = imageUri;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.somethingwentwrong, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, R.string.youhaventpickedimage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    void addUser(Task<Uri> uri) {
        uri.addOnSuccessListener(this, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mdatabaseReference.push()
                        .setValue(new UserModel(signUpFirstName.getText().toString()
                                , signUpSecondName.getText().toString()
                                , signUpUserName.getText().toString()
                                , signUpEmail.getText().toString()
                                , signUpPhoneNumber.getText().toString()
                                , signUpPassword.getText().toString()
                                , uri.toString()));
                MainActivity.start(SignUpActivity.this);
                finish();
            }
        });
    }

    @OnClick({R.id.sign_up_btn_takePhoto, R.id.sign_up_btn_pickAPhoto, R.id.sign_up_btn_signUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up_btn_takePhoto:
                requestPermissionHandler.requestPermission(this, new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 123, new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_REQUEST);
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(SignUpActivity.this, R.string.requestpermissionfailed, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.sign_up_btn_pickAPhoto:
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
                break;
            case R.id.sign_up_btn_signUp:
                boolean gate = true;
                for (UserModel userModel : MainActivity.getUserModels()) {
                    if (userModel.getmEmail().equals(signUpEmail.getText().toString()) || userModel.getmUserName().equals(signUpUserName.getText().toString())) {
                        Toast.makeText(this, R.string.youhaveregisteredbefore, Toast.LENGTH_SHORT).show();
                        finish();
                        MainActivity.start(this);
                        gate = false;
                        break;
                    }
                }
                if (gate) {
                    Toast.makeText(this, R.string.waittouploadimage, Toast.LENGTH_SHORT).show();
                    if (imageUriForFirebase == null) {
                        imageUriForFirebase = Uri.parse("android.resource://com.example.android.lovemeter/" + R.drawable.love_meter);
                    }
                    final StorageReference storageReference = mStorageReference.child(imageUriForFirebase.getLastPathSegment());
                    storageReference.putFile(imageUriForFirebase).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            addUser(storageReference.getDownloadUrl());
                        }
                    });
                    break;
                }
        }
    }
}
