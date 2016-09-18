package cn.ximoon.unionlistview;

import java.io.Serializable;

/**
 * @Author: Zora
 * @Email: zorabai@meilapp.com
 * @CreateTime: 2016/4/28 23:04
 * @Description:
 */
public class KeyMap implements Serializable, ContactView.ContactData {

    private String key;

    public String getKey(){
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public KeyMap() {
    }

    public KeyMap(String key) {
        this.key = key;
    }

    @Override
    public String getData() {
        return key;
    }

    @Override
    public String getSlug() {
        return key;
    }
}
