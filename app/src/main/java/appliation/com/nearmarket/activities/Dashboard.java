package appliation.com.nearmarket.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;


import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.android.material.navigation.NavigationView;
import com.razorpay.PaymentResultListener;

import java.util.ArrayList;

import appliation.com.nearmarket.Fragment.Basket;
import appliation.com.nearmarket.Fragment.Categories;
import appliation.com.nearmarket.Fragment.Home;
import appliation.com.nearmarket.Fragment.Profile;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.database.DatabaseClient;
import appliation.com.nearmarket.databinding.ActivityDashboardBinding;
import appliation.com.nearmarket.interfaces.RazorPayCallbacks;

public class Dashboard extends BaseActivity implements View.OnClickListener, PaymentResultListener {


    private ActivityDashboardBinding binding;
    private ActionBarDrawerToggle toggle;
    private   RazorPayCallbacks razorPayCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard);

        setUpToggle();
       // getData();
        addIcon();
        setSelectedTabListener();
        implementListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public  void setRazorPayCallbacks(RazorPayCallbacks razorPayCallbacks){
    this.razorPayCallbacks = razorPayCallbacks;
    }

    private void getData(){
        if (getIntent() != null){
            if (getIntent().getIntExtra(MOVE_TO,HOME) == BASKET) {
                switchToFragment(new Basket());
                binding.bottomNavigation.setSelectedIndex(BASKET);
            }
            else {
                switchToFragment(new Home());
            }
        }
        else {
            switchToFragment(new Home());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finishAffinity();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(binding.frame.getId());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void implementListener(){
        binding.nvView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                setNavigationView(menuItem);
                return false;
            }
        });
    }

    private void setNavigationView(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.nav_logout:
                logout();
                break;

            case R.id.nav_my_order:
                startActivity(new Intent(mContext,MyOrders.class));
                break;
        }

        binding.drawerLayout.closeDrawers();
    }

    private void logout(){
        emptyLocalData();
        sp.clearData();
        startActivity(new Intent(this,Login.class));
        finish();
    }

    private void emptyLocalData(){
        class DeleteData extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(mContext)
                        .getAppDatabase()
                        .cartDao()
                        .deleteAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //  hideLoader();

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

                // showToast(String.valueOf(values.length));

            }
        }


        DeleteData saveData = new DeleteData();
        saveData.execute();
    }

    private void setUpToggle(){
        toggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,binding.toolbar.mToolbar,R.string.drawer_open,R.string.drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);

        binding.toolbar.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        setSupportActionBar(binding.toolbar.mToolbar);

        if (getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addIcon(){
        binding.bottomNavigation.setBarBackgroundColor(Color.parseColor("#FFFFFF"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_dashboard_black_24dp),"Home","#FFA500"));
    //    tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_grid_on_black_24dp),"Types","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_shopping_basket_black_24dp),"Basket","#EA3373"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.ic_person_outline_black_24dp),"Profile","#CEEE26"));

        binding.bottomNavigation.setTabList(tabs);
        binding.bottomNavigation.attachTabs(mContext);
    }

    private void setSelectedTabListener(){
        binding.bottomNavigation.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
               switch (selectedIndex){
                   case HOME :
                       switchToFragment(new Home());
                       break;
                  /* case TYPE:
                       switchToFragment(new Categories());
                       break;*/
                   case BASKET:
                       switchToFragment(new Basket());
                       break;
                   case PROFILE:
                       switchToFragment(new Profile());
                       break;

               }
            }
        });

    }

    private void switchToFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onPaymentSuccess(String s) {
        razorPayCallbacks.onSuccess(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        razorPayCallbacks.onError(i,s);
    }
}
