package com.sangria.client.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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
    
    public static String generateUniqueId(String prefix, int numOfRandomInt) {
    	String result = prefix;
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	result += sdf.format(date);
    			
    	int num = numOfRandomInt > 3 ? 3 : numOfRandomInt;
    	for(int i = 1; i <= num; i++) {
    		int randomInt = new Random().nextInt(20);
    		result += String.valueOf(randomInt);
    	}
    	
    	return result;
    }
    
    public static String getTimeNow() {
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	String now = sdf.format(date);
    	return now;
    }

}
