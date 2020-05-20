package appliation.com.nearmarket.database;

import android.content.Context;
import android.os.AsyncTask;
//import static com.facebook.FacebookSdk.getApplicationContext;
import java.util.List;

public class CartData {

    private Context context;

    public CartData(Context context){
        this.context = context;
    }
    public List<CartDatabase> getCart(){
        try {
            return new GetCart().execute().get();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    class GetCart extends AsyncTask<Void,Void,List<CartDatabase>>{
        @Override
        protected List<CartDatabase> doInBackground(Void... voids) {
            return DatabaseClient
                    .getInstance(context)
                    .getAppDatabase()
                    .cartDao()
                    .getAll();

/*
            return StepsDatabase.
                    getInstance(getApplicationContext())
                    .getDatabase()
                    .stepsDao()
                    .getALl();*/
        }
    }
}
