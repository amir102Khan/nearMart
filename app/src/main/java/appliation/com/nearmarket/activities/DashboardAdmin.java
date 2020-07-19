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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import appliation.com.nearmarket.Fragment.AppStatus;
import appliation.com.nearmarket.Fragment.Basket;
import appliation.com.nearmarket.Fragment.Home;
import appliation.com.nearmarket.Fragment.Profile;
import appliation.com.nearmarket.Fragment.UploadAdditional;
import appliation.com.nearmarket.Fragment.UploadBanner;
import appliation.com.nearmarket.Fragment.UploadProduct;
import appliation.com.nearmarket.R;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityDashboardAdminBinding;

public class DashboardAdmin extends BaseActivity {
    private ActivityDashboardAdminBinding binding;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard_admin);

        setUpToggle();
        implementListener();
        switchToFragment(new UploadProduct());
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
            case R.id.item_logout:
                logout();
                break;

            case R.id.item_add_banner:
                switchToFragment(new UploadBanner());
                break;

            case R.id.item_upload_product:
                switchToFragment(new UploadProduct());
                break;

            case R.id.item_set_app_status:
                switchToFragment(new AppStatus());
                break;
            case R.id.item_additional_services:
                switchToFragment(new UploadAdditional());
                break;
        }

        binding.drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
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

    private void switchToFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    private void logout(){
        sp.clearData();
        startActivity(new Intent(this,Login.class));
        finish();
    }
}
