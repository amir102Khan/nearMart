package appliation.com.nearmarket.Util;

import android.content.Context;
import android.content.SharedPreferences;

import appliation.com.nearmarket.interfaces.Constants;

public class SharedPref {

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public String PREFS_NAME = "nearMarket";


    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        editor = sharedPreferences.edit();
    }

    public void setBoolean(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    public void setString(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }

    public void clearData(){
        String userType = getString(Constants.USER_TYPE);
        editor.clear();
        setString(Constants.USER_TYPE,userType);
        editor.commit();
    }
}
