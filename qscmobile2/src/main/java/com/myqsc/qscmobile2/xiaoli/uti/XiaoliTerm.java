package com.myqsc.qscmobile2.xiaoli.uti;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by richard on 13-8-31.
 */
public class XiaoliTerm {
    String termName = null;
    XiaoliRange range = null;
    public XiaoliTerm(JSONObject jsonObject, String name) throws JSONException, ParseException {
        range = new XiaoliRange(jsonObject);
        this.termName = name;
    }
}
