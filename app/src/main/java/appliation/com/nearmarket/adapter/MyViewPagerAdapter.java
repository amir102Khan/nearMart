package appliation.com.nearmarket.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appliation.com.nearmarket.R;

public class MyViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> banners;

    public MyViewPagerAdapter(Context context,ArrayList<String> banners){
        this.context = context;
        this.banners = banners;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container,final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.object_details_image, container, false);
        final ImageView pagerImage = layout.findViewById(R.id.pagerImage);
        if (checkInternetConnection()){

            try {

                Picasso.with(context)
                        .load(banners.get(position))
                        .placeholder(R.drawable.logo)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .error(R.drawable.logo)
                        .into(pagerImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context)
                                        .load(banners.get(position))
                                        .error(R.drawable.logo)
                                        .into(pagerImage, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso","Could not fetch image");
                                            }
                                        });
                            }
                        });
            }
            catch (Exception e){
                pagerImage.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.logo));
            }


        }
        else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

        container.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return banners.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }

    public boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMgr != null && (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected());
    }
}
