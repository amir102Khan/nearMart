package appliation.com.nearmarket.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import appliation.com.nearmarket.Model.ProductModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityUpdateProductBinding;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class UpdateProductActivity extends BaseActivity implements View.OnClickListener {

    private ActivityUpdateProductBinding binding;
    private ArrayList<String> categories = new ArrayList<>();
    private String[] mainCategory = {GROCERY, VEGETABLES, GIFTS, ADDITIONAL_SERVICE};
    private String selectedMainCategory = "";
    private String selectedSubCategory = "";
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final int PERMISSION_CAMERA = 101;
    private final int PERMISSION_GALLERY = 102;
    private String productImage = "";
    private String productName;
    private String productPrice;
    private String unit;
    private String minQuantity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private File file;

    //firebase objects
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_product);
        Common.setToolbarWithBackAndTitle(mContext, "Admin", false, 0);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PRODUCTS_TYPE);
        //  databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        setAdapter();
        implementListener();

    }

    private void setAdapter() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, mainCategory);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnCategory.setAdapter(arrayAdapter);
        setSubCategory();
    }

    private void setSubCategory() {
        categories.clear();
        switch (selectedMainCategory) {
            case GROCERY:
                categories.add(BRANDED_FOODS);
                categories.add(HEALTH);
                categories.add(HOME_CARE);
                categories.add(PERSONAL_CARE);
                break;
            case VEGETABLES:
                categories.add(FRESH_FRUITS);
                categories.add(FRESH_VEGGIES);
                break;
            case GIFTS:
                categories.add(CUSTOMIZED_ITEMS);
                categories.add(CUSTOMIZED_FRAMES);
                categories.add(CUSTOMIZED_TSHIRTS);
                categories.add(CUSTOMIZED_CHOCLATE);
                break;
            case ADDITIONAL_SERVICE:
                categories.add(PHOTOGRAPHY);
                categories.add(EVENT_MANAGEMENT);
                break;
            default:
                categories.add(BRANDED_FOODS);
                categories.add(HEALTH);
                categories.add(HOME_CARE);
                categories.add(PERSONAL_CARE);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnSubCategory.setAdapter(arrayAdapter);
    }

    private void implementListener() {
        binding.productImage.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMainCategory = mainCategory[position];
                setSubCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spnSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubCategory = categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view == binding.productImage) {
            if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                requestPermissions(PERMISSIONS, PERMISSION_CAMERA);
            } else {
                // openCamera();
                dialogSelectImage();
            }
        } else if (view == binding.btnSubmit) {
            validation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED /*&& grantResults[2] == PackageManager.PERMISSION_GRANTED*/) {
                //openCamera();
                dialogSelectImage();
            } else {
                Toast.makeText(this, "Permissions Required to use camera", Toast.LENGTH_SHORT).show();
            }
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

    private void openCamera() {
        EasyImage.openCamera(mContext, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                file = imageFiles.get(0);
                binding.productImage.setImageURI(Uri.fromFile(file));
                productImage = Uri.fromFile(file).toString();
            }
        });
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void submitImage() {
        if (file != null && file.exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(file));
            } catch (Throwable t) {
                Log.e("ERROR", "Error compressing file." + t.toString());
                t.printStackTrace();
            }
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCancelable(false);

            //getting the storage reference
            final StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis());

            sRef.putFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //dismissing the progress dialog
                    progressDialog.dismiss();
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            submitProduct(uri.toString());
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

    private void submitProduct(String productImage) {
        showLoader();
        final String key = databaseReference.child(selectedMainCategory).push().getKey();

        final ProductModel productModel = new ProductModel(productImage,
                productName,
                productPrice,
                key,
                selectedMainCategory,
                selectedSubCategory,
                unit,
                minQuantity,"",false);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(selectedMainCategory).child(selectedSubCategory).child(key).setValue(productModel);
                clearData();
                binding.loader.setVisibility(View.VISIBLE);
                hideLoader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoader();
            }
        });


    }

    private void clearData() {
        Toast.makeText(mContext, "Product Submitted", Toast.LENGTH_SHORT).show();
        binding.productImage.setImageDrawable(getDrawable(R.drawable.imagedummybag));
        binding.edPrice.setText("");
        binding.edProductName.setText("");
        binding.edUnit.setText("");
        binding.edMinQty.setText("");
        binding.spnCategory.setSelection(0);
        binding.spnSubCategory.setSelection(0);
    }


    private void showLoader() {
        binding.btnSubmit.setClickable(false);
        binding.loader.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        binding.btnSubmit.setClickable(true);
        binding.loader.setVisibility(View.GONE);
    }

    private void validation() {
        productName = binding.edProductName.getText().toString();
        productPrice = binding.edPrice.getText().toString();
        minQuantity = binding.edMinQty.getText().toString();
        unit = binding.edUnit.getText().toString();
        if (!Common.validateEditText(productName)) {
            showSnackBar(binding.getRoot(), "Name is empty");
        } else if (!Common.validateEditText(productPrice)) {
            showSnackBar(binding.getRoot(), "Price is empty");
        } else if (selectedMainCategory.equals("")) {
            showSnackBar(binding.getRoot(), "please select main category");
        } else if (selectedSubCategory.equals("")) {
            showSnackBar(binding.getRoot(), "Price select sub category");
        }
        else if (file != null && !file.exists()) {
            showSnackBar(binding.getRoot(),"Please select Image");
        }
        else {
            if (checkInternetConnection()) {
                submitImage();
            } else {
                showToast(getString(R.string.internet_not_there));
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_logout) {
            logout();
        }
        else if (item.getItemId() == R.id.item_add_banner){
            startActivity(new Intent(mContext,AddBanner.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        sp.clearData();
        startActivity(new Intent(mContext, Login.class));
        finish();
    }

   /* private void uploadImage(){
        final StorageReference ref = storageRef.child("images/mountains.jpg");
       UploadTask uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }*/
}
