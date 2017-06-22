package com.sdk.lib.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 16-5-9.
 */
public class SPUtil {

    private static final String TAG = "SPUtil";
    public static final String PREF_NAME = "setting";
    private SharedPreferences mPreferences;
    private static SPUtil mInstance;
    private Context mContext;

    private SPUtil(Context sContext) {
        mContext = sContext.getApplicationContext();
        mPreferences = mContext.getSharedPreferences(PREF_NAME, 0);
    }

    public static SPUtil getInstance(Context sContext) {
    	synchronized (SPUtil.class) {
    		if (mInstance == null) {
    			mInstance = new SPUtil(sContext);
    		}
		}
    	return mInstance;
    }

    public SPUtil save(String key, Object value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, Boolean.valueOf(value.toString()));
        } else if (value instanceof Long) {
            editor.putLong(key, Long.valueOf(value.toString()));
        } else if (value instanceof Float) {
            editor.putFloat(key, Float.valueOf(value.toString()));
        } else if (value instanceof Integer) {
            editor.putInt(key, Integer.valueOf(value.toString()));
        }
        editor.commit();
        return mInstance;
    }

    public Object get(String key, Object value) {
        if (value instanceof String) {
            return mPreferences.getString(key, (String)value);
        } else if (value instanceof Boolean) {
            return mPreferences.getBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            return mPreferences.getLong(key, Long.parseLong(value.toString()));
        } else if (value instanceof Float) {
            return mPreferences.getFloat(key, -1.0f);
        } else if (value instanceof Integer) {
            return mPreferences.getInt(key, Integer.parseInt(value.toString()));
        }
        return null;
    }

    public void setArrayListString(String key, ArrayList<String> list) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (list != null && list.size() > 0) {
            editor.putStringSet(key, new HashSet<String>(list));
            editor.commit();
        }
    }

    public ArrayList<String> getArrayListString(String key) {
        Set<String> set = mPreferences.getStringSet(key, null);
        if (set != null) {
            ArrayList<String> setList = new ArrayList<String>(set);
            return setList;
        }
        return null;
    }

    public long getLong(String key, long longDefault) {
        return mPreferences.getLong(key, longDefault);
    }

    public int getInt(String key, int intDefault) {
        return mPreferences.getInt(key, intDefault);
    }

    public String getString(String key, String strDefault) {
        return mPreferences.getString(key, "");
    }

    public boolean getBoolean(String key, boolean blDefault) {
        return mPreferences.getBoolean(key, blDefault);
    }
    
    public void setLong(String key, long longDefault) {
        save(key, longDefault);
    }

    public void setInt(String key, int intDefault) {
    	save(key, intDefault);
    }

    public void setString(String key, String strDefault) {
    	save(key, strDefault);
    }

    public void setBoolean(String key, boolean blDefault) {
    	save(key, blDefault);
    }
}
