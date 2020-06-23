package appliation.com.nearmarket.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import appliation.com.nearmarket.Model.SignUpUserModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.activities.Dashboard;
import appliation.com.nearmarket.core.BaseFragment;
import appliation.com.nearmarket.databinding.FragmentProfileBinding;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


public class Profile extends BaseFragment implements View.OnClickListener {

    public Profile() {
        // Required empty public constructor
    }


    private FragmentProfileBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private File profileImage;
    private StorageReference storageReference;
    private String userPsswd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,null,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(USER).child(sp.getString(USER_ID));

        if (checkInternetConnection()){
            getUserData();
        }
        else {
            showToast(getString(R.string.internet_not_there));
        }
        storageReference = FirebaseStorage.getInstance().getReference();

        binding.btnUpdateProfile.setOnClickListener(this);
        binding.profileImageProfile.setOnClickListener(this);
    }

    private void getUserData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SignUpUserModel userModel = dataSnapshot.getValue(SignUpUserModel.class);
                setData(userModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setData(SignUpUserModel userModel){
        binding.edProfileName.setText(userModel.getUsername());
        binding.edProfilePhoneNumber.setText(sp.getString(USER_ID));
        binding.edProfileEmail.setText(userModel.getEmail());

        userPsswd = userModel.getPassword();
        if (userModel.getUsername() != null){
            Picasso.with(mContext)
                    .load(userModel.getUserImage())
                    .error(R.drawable.ic_dummy_user)
                    .placeholder(R.drawable.ic_dummy_user)
                    .into(binding.profileImageProfile);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == binding.btnUpdateProfile){
            if (checkInternetConnection()){
                uploadImage();
            }
            else {
                showToast(getString(R.string.internet_not_there));
            }
        }
        else if (v == binding.profileImageProfile){
            dialogSelectImage();
        }
    }

    public void dialogSelectImage() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.diaog_select_image);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        final TextView camera, gallery, tvRemovePhoto;
        TextView tv_back;
        camera = dialog.findViewById(R.id.tv_take_photo);
        gallery = dialog.findViewById(R.id.tv_select_from_gallery);
        tv_back = dialog.findViewById(R.id.tv_back);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Toast.makeText(mContext, "123", Toast.LENGTH_SHORT).show();
                // showToast("123");
                EasyImage.openCamera(mContext, 0);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EasyImage.openGallery(mContext, 0);
            }
        });


        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void uploadImage(){
        if (profileImage != null && profileImage.exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(profileImage.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(profileImage));
            } catch (Throwable t) {
                Log.e("ERROR", "Error compressing file." + t.toString());
                t.printStackTrace();
            }
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCancelable(false);

            //getting the storage reference
            final StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis());

            sRef.putFile(Uri.fromFile(profileImage)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //dismissing the progress dialog
                    progressDialog.dismiss();
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updateProfile(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    // showToast("image not uploaded");
                    showToast(e.getMessage());
                    String ss = e.getMessage();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //displaying the upload progress
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });
        }
    }

    private void updateProfile(final String imageUrl){
       final String name = binding.edProfileName.getText().toString();
       final String email = binding.edProfileEmail.getText().toString();
        binding.loader.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    SignUpUserModel userModel = new SignUpUserModel(name,email,userPsswd,imageUrl);
                    databaseReference.setValue(userModel);

                binding.loader.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.loader.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, mContext, new DefaultCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                File file = imageFiles.get(0);
                binding.profileImageProfile.setImageURI(Uri.fromFile(file));
                profileImage = file;
            }
        });
    }
}
