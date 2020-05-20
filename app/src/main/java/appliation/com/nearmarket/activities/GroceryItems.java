package appliation.com.nearmarket.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import appliation.com.nearmarket.Model.AddToCartModel;
import appliation.com.nearmarket.Model.BucketModel;
import appliation.com.nearmarket.Model.ProductModel;
import appliation.com.nearmarket.Model.SignUpUserModel;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.adapter.GroceryListAdapter;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.database.CartDatabase;
import appliation.com.nearmarket.database.DatabaseClient;
import appliation.com.nearmarket.databinding.ActivityGroceryItemsBinding;
import appliation.com.nearmarket.interfaces.OnAdapterItemClickWithType;

public class GroceryItems extends BaseActivity implements OnAdapterItemClickWithType, View.OnClickListener {

    private ActivityGroceryItemsBinding binding;
    private String title;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<ProductModel> prodcuts = new ArrayList<>();
    private List<BucketModel> carts = new ArrayList<>();
    private GroceryListAdapter adapter ;
    private DatabaseReference bucketDatabase;
    private int POSITION_TO_WORK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_items);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_grocery_items);
        title = getIntent().getStringExtra(GROCERY_TYPE);
        Common.setToolbarWithBackAndTitle(mContext,title,true,R.drawable.ic_chevron_left_black_24dp);
        setEmptyAdapter();
        firebaseDatabase = FirebaseDatabase.getInstance();
        String mainCategory = getIntent().getStringExtra(MAIN_CATEGORY);
        databaseReference = firebaseDatabase.getReference(PRODUCTS_TYPE).child(mainCategory).child(title);
        bucketDatabase = firebaseDatabase.getReference(BUCKET).child(sp.getString(USER_ID)).child(CARTS);
        if (checkInternetConnection()){
            showLoader();
            data();
        }
        else {
            showToast(getString(R.string.internet_not_there));
        }
        binding.buttonAddToCart.setOnClickListener(this);
    }

    private void data(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProductModel model= dataSnapshot.getValue(ProductModel.class);
                    prodcuts.add(model);
                }
                adapter.notifyDataSetChanged();
                hideLoader();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoader();
            }
        });
    }


    private void hideLoader(){
        binding.loader.setVisibility(View.GONE);
    }

    private void showLoader(){
        binding.loader.setVisibility(View.VISIBLE);
    }

    private void setEmptyAdapter(){
        binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new GroceryListAdapter(mContext,prodcuts,this);
        binding.rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, int type, String data) {
        POSITION_TO_WORK = position;
        if (type == ADD){
            int count = prodcuts.get(position).getCount();
            count = count + 1;
            prodcuts.get(position).setCount(count);
            adapter.notifyDataSetChanged();
            //addProductToBucket();
            updateCart();
        }
        else if (type == SUB){
            int count = prodcuts.get(position).getCount();
            if (count > 0){
                count = count - 1;
                prodcuts.get(position).setCount(count);
                adapter.notifyDataSetChanged();
               // addProductToBucket();
                updateCart();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basket,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_basket){
            startActivity(new Intent(mContext,Dashboard.class)
            .putExtra(MOVE_TO,BASKET));
        }
        return super.onOptionsItemSelected(item);
    }

    private void addProductToBucket(){
        showLoader();
        final int count = prodcuts.get(POSITION_TO_WORK).getCount();
        final int quantity = count * Integer.valueOf(prodcuts.get(POSITION_TO_WORK).getMinimumQty());
        final String quantityOrder = String.valueOf(quantity) + prodcuts.get(POSITION_TO_WORK).getUnit();
        final int itemTotalPrice = count * Integer.valueOf(prodcuts.get(POSITION_TO_WORK).getPrice());

        bucketDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String key = prodcuts.get(POSITION_TO_WORK).getProductKey();
                if (dataSnapshot.child(key).exists()){
                    showToast("updated");
                    bucketDatabase.child(key).child("count").setValue(count);
                    bucketDatabase.child(key).child("quantity").setValue(quantityOrder);
                }
                else {
                    BucketModel bucketModel = new BucketModel(prodcuts.get(POSITION_TO_WORK).getName(),
                            prodcuts.get(POSITION_TO_WORK).getPrice(),
                            prodcuts.get(POSITION_TO_WORK).getProductKey(),
                            quantityOrder,
                            count,
                            String.valueOf(itemTotalPrice));


                    if (count == 0){
                        bucketDatabase.child(key).removeValue();
                    }
                    else {
                        bucketDatabase.child(key).setValue(bucketModel);
                    }
                }

               // String key = bucketDatabase.push().getKey();

               // String key = databaseReference.push().getKey();
                /*AddToCartModel addToCartModel = new AddToCartModel(carts);

                bucketDatabase.child(carts.get())setValue(addToCartModel);*/

                    hideLoader();
                  //  databaseReference.child(phone).setValue(userModel);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideLoader();
            }
        });
    }

    private void deleteProductTOBucket(){

    }

    private void updateCart(){

        final int count = prodcuts.get(POSITION_TO_WORK).getCount();
        final int quantity = count * Integer.valueOf(prodcuts.get(POSITION_TO_WORK).getMinimumQty());
        final String quantityOrder = String.valueOf(quantity) + prodcuts.get(POSITION_TO_WORK).getUnit();
        final int itemTotalPrice = count * Integer.valueOf(prodcuts.get(POSITION_TO_WORK).getPrice());

        addProductInCartToLocal(quantityOrder,count,itemTotalPrice);
        /*if (carts.size()> 0){
            for (int i = 0; i < carts.size(); i ++){
                if (carts.get(i).getProductKey().equals(prodcuts.get(POSITION_TO_WORK).getProductKey())){
                    carts.get(i).setCount(count);
                    carts.get(i).setQuantity(quantityOrder);
                    carts.get(i).setItemTotalPrice(String.valueOf(itemTotalPrice));
                    return;
                }
            }
            addNewProduct(quantityOrder,count,itemTotalPrice);
        }
        else {
            addNewProduct(quantityOrder,count,itemTotalPrice);
        }*/
    }

    private void addNewProduct(String quantityOrder, int count, int itemTotalPrice){
        BucketModel bucketModel = new BucketModel(prodcuts.get(POSITION_TO_WORK).getName(),
                prodcuts.get(POSITION_TO_WORK).getPrice(),
                prodcuts.get(POSITION_TO_WORK).getProductKey(),
                quantityOrder,
                count,
                String.valueOf(itemTotalPrice));

        carts.add(bucketModel);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.buttonAddToCart){
            List<BucketModel> bb = carts;
            int ii = 0;
          //  addProductToBucket(carts);
            //carts.clear();
        }
    }

    private void addProductInCartToLocal(final String quantityOrder, final int count, final int itemTotalPrice){
        showLoader();
        class SaveData extends AsyncTask<Void,Void,Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                // creating database

                CartDatabase cartDatabase = new CartDatabase();
                cartDatabase.setProductKey(prodcuts.get(POSITION_TO_WORK).getProductKey());
                cartDatabase.setCount(count);
                cartDatabase.setQuantity(quantityOrder);
                cartDatabase.setItemTotalPrice(String.valueOf(itemTotalPrice));
                cartDatabase.setPrice(prodcuts.get(POSITION_TO_WORK).getPrice());
                cartDatabase.setName(prodcuts.get(POSITION_TO_WORK).getName());
                cartDatabase.setProductImage(prodcuts.get(POSITION_TO_WORK).getImage());
                cartDatabase.setQty(prodcuts.get(POSITION_TO_WORK).getMinimumQty());
                cartDatabase.setUnit(prodcuts.get(POSITION_TO_WORK).getUnit());
                // adding in database

                DatabaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .cartDao()
                        .insert(cartDatabase);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideLoader();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

               // showToast(String.valueOf(values.length));

            }
        }


        SaveData saveData = new SaveData();
        saveData.execute();
    }
}
