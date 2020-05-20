package appliation.com.nearmarket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import appliation.com.nearmarket.R;
import appliation.com.nearmarket.Util.Common;
import appliation.com.nearmarket.core.BaseActivity;
import appliation.com.nearmarket.databinding.ActivityAdditionalListItemsBinding;

public class AdditionalListItems extends BaseActivity {

    private ActivityAdditionalListItemsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_additional_list_items);
        Common.setToolbarWithBackAndTitle(mContext,"Additional",true,R.drawable.ic_chevron_left_black_24dp);
    }
}
