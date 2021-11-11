package com.sangria.auth.utils;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;

public class CommonUtils {
	
    public static <T> T copyData(Object fromObject, Class<T> clazz) {
        return JSON.parseObject(JSONObject.toJSONString(fromObject), clazz);
    }

    public static <T> IPage<T> copyPageData(IPage fromObject, Class<T> clazz) {
        if (fromObject == null) {
            return null;
        }
        ArrayList<T> data = new ArrayList<T>();
        if (fromObject.getRecords() != null) {
            for (Object record : fromObject.getRecords()) {
                data.add(copyData(record, clazz));
            }
            fromObject.setRecords(data);
        }
        return fromObject;
    }

}
