package com.myqsc.qscmobile2.curriculum.uti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoClassData {
    final static String PREFERENCE = "KEBIAO_DATA";

    final static int WEEK_BOTH = 0, WEEK_ODD = 1, WEEK_EVEN = 2;

    String name, teacher, place;
    int week = 0, year = 0, time = 0;

    public KebiaoClassData(){

    }

    public static List<KebiaoClassData> parse (JSONArray jsonArray) throws JSONException{
        List<KebiaoClassData> list = new ArrayList<KebiaoClassData>();
        for (int i = 0; i != jsonArray.length(); ++i){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String rawYear = jsonObject.getString("year");
            int year = Integer.valueOf(rawYear.substring(0, 4));

            List<KebiaoClassData> temp = parseYear(jsonObject.getJSONArray("data"), year);
            for (int j = 0; j != temp.size(); ++j)
                list.add(temp.get(j));
        }
        return list;
    }

    public static List<KebiaoClassData> parseYear(JSONArray jsonArray, int year) throws JSONException{
        List<KebiaoClassData> list = new ArrayList<KebiaoClassData>();

        for (int i = 0; i != jsonArray.length(); ++i){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String name = jsonObject.getString("name");
            String teacher = jsonObject.getString("teacher");

            JSONArray classes = jsonObject.getJSONArray("class");

            for (int j = 0; j != classes.length(); ++j){
                JSONObject object = classes.optJSONObject(j);

                KebiaoClassData data = new KebiaoClassData();
                data.name = name;
                data.teacher = teacher;
                data.place = object.getString("place");
                data.time = object.getInt("weekday");
                data.year = year;

                String week = object.getString("week");
                if (week.compareTo("both") == 0)
                    data.week = WEEK_BOTH;
                    if (week.compareTo("odd") == 0)
                        data.week = WEEK_ODD;
                    else
                        data.week = WEEK_BOTH;

                list.add(data);
            }
        }
        return list;
    }
}
