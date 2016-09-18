package cn.ximoon.unionlistview;

import java.io.Serializable;

/**
 * @Author: Zora
 * @Email: zorabai@meilapp.com
 * @CreateTime: 2016/4/28 23:06
 * @Description:
 */
public class ValueMap implements Serializable, ContactView.ContactData{

    private String name;
    private String values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public ValueMap(String name, String values) {
        this.name = name;
        this.values = values;
    }

    public ValueMap() {
    }

    @Override
    public String getData() {
        return values;
    }

    @Override
    public String getSlug() {
        return name;
    }
}
