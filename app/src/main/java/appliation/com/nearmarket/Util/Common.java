package appliation.com.nearmarket.Util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import appliation.com.nearmarket.R;

public class Common {
    public static boolean validateEditText(String text) {
        return !TextUtils.isEmpty(text) && text.trim().length() > 0;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void setToolbarWithBackAndTitle(final Context ctx, String title, Boolean value, int backResource) {
        Toolbar toolbar = ((AppCompatActivity) ctx).findViewById(R.id.toolbar);
        ((AppCompatActivity) ctx).setSupportActionBar(toolbar);
        TextView title_tv = toolbar.findViewById(R.id.title_tv);
        ActionBar actionBar = ((AppCompatActivity) ctx).getSupportActionBar();
        if (actionBar != null) {
            if (backResource != 0){
                toolbar.setNavigationIcon(backResource);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((AppCompatActivity) ctx).onBackPressed();
                    }
                });
            }

            actionBar.setDisplayShowHomeEnabled(value);
            actionBar.setDisplayShowTitleEnabled(false);
            title_tv.setText(title);
        }
    }

    /**
     * get required date
     * @param inputFormat
     * @param outputFormat
     * @param selectedDate
     * @return date
     */
    public static String getDate(String inputFormat,String outputFormat,String selectedDate){
        SimpleDateFormat parseDateFormat = new SimpleDateFormat(inputFormat);
        Date date = null;
        try {
            date = parseDateFormat.parse(selectedDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        SimpleDateFormat requiredDateFormat = new SimpleDateFormat(outputFormat);
        String value = requiredDateFormat.format(date);
        return  value;
    }
}
