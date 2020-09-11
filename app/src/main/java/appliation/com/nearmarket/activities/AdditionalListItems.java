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
import appliation.com.nearmarket.databinding.ActivityAdditionalListItemsBinding;
import appliation.com.nearmarket.interfaces.OnAdapterItemClick;

public class AdditionalListItems extends BaseActivity implements OnAdapterItemClick {

    private ActivityAdditionalListItemsBinding binding;

    private ArrayList<String> list = new ArrayList<>();
    private GrocerListAdapter grocerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_additional_list_items);
        Common.setToolbarWithBackAndTitle(mContext,"Additional Service",true,R.drawable.ic_chevron_left_black_24dp);
        setEmptyAdapter();
        setData();
    }

    private void setEmptyAdapter(){
        binding.rv.setLayoutManager(layoutManager);
        grocerListAdapter = new GrocerListAdapter(mContext,list,this);
        binding.rv.setAdapter(grocerListAdapter);
    }

    private void setData(){
        list.add(PHOTOGRAPHY);
        list.add(EVENT_MANAGEMENT);
        grocerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, String data) {

        startActivity(new Intent(mContext,AdditonalServices.class)
                .putExtra(ADDITIONAL_TYPE,list.get(position)));
    }
}
