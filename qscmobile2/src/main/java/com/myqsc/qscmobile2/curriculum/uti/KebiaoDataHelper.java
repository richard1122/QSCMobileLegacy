package com.myqsc.qscmobile2.curriculum.uti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;

import com.myqsc.qscmobile2.network.DataUpdater;
import com.myqsc.qscmobile2.network.UpdateHelper;
import com.myqsc.qscmobile2.support.database.structure.UserIDStructure;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.HandleAsyncTaskMessage;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.myqsc.qscmobile2.login.uti.PersonalDataHelper;
import com.myqsc.qscmobile2.uti.Utility;
import com.myqsc.qscmobile2.xiaoli.uti.XiaoliHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by richard on 13-8-29.
 */
public class KebiaoDataHelper {
    Context mContext = null;
    List<KebiaoClassData> todayKebiaolist = null;

    public KebiaoDataHelper(Context context) {
        this.mContext = context;
        mContext.registerReceiver(updateReceiver, new IntentFilter(BroadcastHelper.BROADCAST_CARD_REDRAW));
    }


    public void clear() {
        mContext
                .getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .remove(DataUpdater.JW_KEBIAO)
                .commit();
    }

    public void set(String result) {
        clear();
        mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                .edit()
                .putString(DataUpdater.JW_KEBIAO, result)
                .commit();
    }

    public List<KebiaoClassData> getDay(final Calendar calendar) {
        try {
            getTodayKebiao(KebiaoClassData.parse(
                    new JSONArray(
                            mContext.getSharedPreferences(Utility.PREFERENCE, 0)
                                    .getString(DataUpdater.JW_KEBIAO, null))), calendar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert todayKebiaolist != null;
        return todayKebiaolist;
    }

    private List<KebiaoClassData> getTodayKebiao(List<KebiaoClassData> allKebiaoList, Calendar calendar) {
        todayKebiaolist = new ArrayList<KebiaoClassData>();
        XiaoliHelper xiaoliHelper = new XiaoliHelper(mContext);
        calendar = xiaoliHelper.doRemap(calendar);
        int week = xiaoliHelper.checkParity(calendar, false);
        char term = xiaoliHelper.getTerm(calendar, false);
        int year = xiaoliHelper.getYear(calendar, false);

        for (KebiaoClassData data : allKebiaoList) {
            if (data.year != year) {
                continue;
            }
            if (data.term.indexOf(term) == -1) {
                continue;
            }

            if (data.week != Utility.WEEK_BOTH && data.week != week) {
                continue;
            }

            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekday == 1)
                weekday = 8;
            --weekday;
            if (data.time != weekday)
                continue;

            LogHelper.d("today: " + data);
            todayKebiaolist.add(data);
        }

        Collections.sort(todayKebiaolist, new Comparator<KebiaoClassData>() {
            @Override
            public int compare(KebiaoClassData kebiaoClassData, KebiaoClassData kebiaoClassData2) {
                return kebiaoClassData.classes[0] - kebiaoClassData2.classes[0];
            }
        });
        return todayKebiaolist;
    }

    final BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("实时课表".compareTo(intent.getExtras().getString("card")) == 0 ||
                    DataUpdater.JW_KEBIAO.compareTo(intent.getExtras().getString("card")) == 0) {
                KebiaoClassData.list = null;
                LogHelper.d("kebiao reseted");
            }
        }
    };

}
