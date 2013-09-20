package com.myqsc.mobile2.xiaoli.uti;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliReMap extends XiaoliRange {
    Calendar from = null, to = null;
    public XiaoliReMap(JSONObject jsonObject) throws JSONException, ParseException {
        super(jsonObject.getString("from"), jsonObject.getString("to"));
        this.from = startTime;
        this.to = endTime;
    }

    public Calendar doRemap(Calendar calendar){
        if (from.get(Calendar.DATE) == calendar.get(Calendar.DATE))
            return to;
        return calendar;
    }
}
