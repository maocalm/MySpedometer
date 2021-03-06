package com.baisi.spedometer.step.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float,
 * Long类型的参数 同样调用getParam就能获取到保存在手机里面的数据
 */
public class SharedPreferencesUtils {
    private Context context;
    /**
     * 保存在手机里面的文件名
     */
    public static final String STEP_NUMBER = "step_number";
    public static final String STEP_SECOND = "stepTimeCountSecond";
    private static final String FILE_NAME = "Spedometer_shareDate";
    public static final String STEP_GOAL = "step_goal";
    public static final String STEP_GOAL_ORDER = "step_goal_order";
    public static final String STEP_GENDER = "step_gender";
    public static final String STEP_HEIGHT = "step_height";
    public static final String STEP_WEIGHT = "step_weight";
    public static final String STEP_Metric = "step_metric";
    public static final String FIRST_COMEIN = "firstComeIn";
    public static final String GENDER = "gender";
    public static final String FEMALE = "female";
    public static final String MALE = "male";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String SWITCHBAR = "switchbar";
    public static final String ISNEWDAY = "is_newday";

    public static final String M_UNIT = "metric_unit";
    public static final String KG_CM = "kg_cm";
    public static final String LBS_FT = "lbs_ft";
    public static final String SWITCH_TRUE = "1";
    public static final String SWITCH_FALSE = "0";

    public static final float WEIGHT_DEFAULT = 60f;
    public static final String GENDER_DEFAULT = "female";
    public static final int Height_DEFAULT = 170;
    public static final String SWITCHBAR_DEFAULT = "1";
    public static final String STEPGOAL_DEFAULT = "1000";
    public static final int STEPGOALORDER_DEFAULT = 2;

    public static final String LAST_HOURSTEP_ALL = "lastHourStepAll";
    public static final String LAST_HOURSTEPSECOND_ALL = "lastHourStepSecondAll";

    public static final String MORNING_DIALOG = "morning_dialog";
    public static final String NOON_DIALOG = "noon_dialog";
    public static final String NIGHT_DIALOG = "night_dialog";


    public String TAG = getClass().getSimpleName();

    // public static SharedPreferencesUtils getInstens(String fileName) {
    // FILE_NAME = fileName;
    // if (sharedPreferencesUtils == null) {
    // synchronized (SharedPreferencesUtils.class) {
    // if (sharedPreferencesUtils == null) {
    // sharedPreferencesUtils = new SharedPreferencesUtils();
    // }
    // }
    // }
    // return sharedPreferencesUtils;
    // }

//    public SharedPreferencesUtils(String FILE_NAME) {
//        this.FILE_NAME = FILE_NAME;
//
//    }

    public SharedPreferencesUtils(Context context) {
        this.context = context;

    }

    public SharedPreferencesUtils() {

    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public void setParam(String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)) {
            editor.putString(key, object.toString());
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.apply();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @return
     */
    // Delete
    public void remove(String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
