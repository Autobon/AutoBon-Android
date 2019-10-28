package cn.com.incardata.http.response;

import java.io.Serializable;
import java.util.Map;

/**
 * 用来传递hashMap参数的类
 * <p>Created by wangyang on 2017/7/10.</p>
 */
public class SerializableMap implements Serializable {

    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
