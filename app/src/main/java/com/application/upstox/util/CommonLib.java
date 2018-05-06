package com.application.upstox.util;

import com.application.upstox.model.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by harsh.jain on 5/6/18.
 */

public class CommonLib {

    public static float getHighest(List<Data> dataList)
    {
        Collections.sort(dataList, new Comparator<Data>() {
            @Override
            public int compare(Data lhs, Data rhs) {
                return (int) (lhs.getHigh()-rhs.getHigh());
            }
        });

        return dataList.get(dataList.size()-1).getHigh();
    }

    public static float getLowest(List<Data> dataList)
    {
        Collections.sort(dataList, new Comparator<Data>() {
            @Override
            public int compare(Data lhs, Data rhs) {
                return (int) (lhs.getLow()-rhs.getLow());
            }
        });

        return dataList.get(0).getLow();
    }
}
