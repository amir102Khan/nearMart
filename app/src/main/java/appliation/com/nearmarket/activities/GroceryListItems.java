package appliation.com.nearmarket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.adapter.GrocerListAdapter;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityGroceryListItemsBinding;
import appliation.com.nearmarket.interfaces.OnAdapterItemClick;

public class GroceryListItems extends BaseActivity implements OnAdapterItemClick {

    private ActivityGroceryListItemsBinding binding;
    private ArrayList<String> list = new ArrayList<>();
    private GrocerListAdapter grocerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_grocery_list_items);
        Common.setToolbarWithBackAndTitle(mContext,"Grocery",true,R.drawable.ic_chevron_left_black_24dp);
        setEmptyAdapter();
        setData();
    }

    private void setEmptyAdapter(){
        binding.rv.setLayoutManager(layoutManager);
        grocerListAdapter = new GrocerListAdapter(mContext,list,this);
        binding.rv.setAdapter(grocerListAdapter);
    }

    private void setData(){
        list.add(BRANDED_FOODS);
        list.add(PERSONAL_CARE);
        list.add(HEALTH);
        list.add(HOME_CARE);
        grocerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, String data) {

        startActivity(new Intent(mContext,GroceryItems.class)
                .putExtra(GROCERY_TYPE,list.get(position))
        .putExtra(MAIN_CATEGORY,getIntent().getStringExtra(MAIN_CATEGORY)));
    }

}
