package cn.ximoon.unionlistview;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ContactView contact_container;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contact_container = (ContactView) findViewById(R.id.contact_container);
        List<KeyMap> keys = new ArrayList<KeyMap>() {
            {
                add(new KeyMap("a"));
                add(new KeyMap("b"));
                add(new KeyMap("c"));
                add(new KeyMap("d"));
                add(new KeyMap("e"));
                add(new KeyMap("f"));
                add(new KeyMap("g"));
                add(new KeyMap("h"));
                add(new KeyMap("i"));
                add(new KeyMap("j"));
                add(new KeyMap("k"));
                add(new KeyMap("l"));
                add(new KeyMap("m"));
                add(new KeyMap("n"));
            }
        };
        final List<ValueMap> values = new ArrayList<ValueMap>() {
            {

                add(new ValueMap("a", "ant"));
                add(new ValueMap("a", "activity"));
                add(new ValueMap("a", "android"));
                add(new ValueMap("b", "break"));
                add(new ValueMap("b", "bottom"));
                add(new ValueMap("b", "broadcast"));
                add(new ValueMap("c", "convertView"));
                add(new ValueMap("c", "container"));
                add(new ValueMap("d", "debug"));
                add(new ValueMap("e", "exception"));
                add(new ValueMap("e", "equals"));
                add(new ValueMap("f", "for"));
                add(new ValueMap("g", "git"));
                add(new ValueMap("g", "gravity"));
                add(new ValueMap("g", "gridlayout"));
                add(new ValueMap("h", "http"));
                add(new ValueMap("h", "hash"));
                add(new ValueMap("h", "header"));
                add(new ValueMap("h", "handler"));
                add(new ValueMap("h", "holder"));
                add(new ValueMap("i", "if"));
                add(new ValueMap("i", "iterator"));
                add(new ValueMap("j", "java"));
                add(new ValueMap("j", "json"));
                add(new ValueMap("k", "key"));
                add(new ValueMap("l", "looper"));
                add(new ValueMap("l", "long"));
                add(new ValueMap("l", "listener"));
                add(new ValueMap("m", "main"));
                add(new ValueMap("m", "manifest"));
                add(new ValueMap("n", "notification"));
                add(new ValueMap("n", "notify"));
                add(new ValueMap("n", "net"));
            }
        };

        contact_container.setLeftData(keys);
        contact_container.setRightData(values);

        contact_container.setLeftData(keys);
        contact_container.setRightData(values);
        // 当前主索引对应的value的个数
        int size = 0;
        // 当前主索引的位置
        int position = 0;
        for (int i = 0; i < values.size(); i++) {
            ValueMap valueMap = values.get(i);
            // 判断当前右侧列表的当前条目是否属于当前索引覆盖的范围
            if (i == 0 || valueMap.getName().equals(values.get(i - 1).getName())) {
            // 根据主索引获取当前索引下的所有item，每获取一个item，size+1
                size++;
                // 设置右侧列表当前item对左侧索引列表的指示关系
                contact_container.pushLink(i, position);
                continue;
            }
            // 如果不是当前索引的覆盖范围则证明当前item进入到下一个索引位置，并记录当前item对应的新索引位置
            contact_container.pushContrastLink(i, position + 1);
            // 设置索引对应得右侧列表的起始位置
            if (position == 0){
                contact_container.pushLink(position, i - size);
            }else{
                contact_container.pushLink(position, i - size - 1);
            }
            // 索引下移
            position++;
//            size重置
            size = 0;
        }

//        contact_container.pushLink(0, 0);
//        contact_container.pushContrastLink(0, 0);

        contact_container.setOnRightListItemClickListener(new ContactView.OnContactClickListener() {

            @Override
            public void onClick(View v, int position) {

            }

        });

        final EditText et = (EditText) findViewById(R.id.et);
        final TextView tv = (TextView) findViewById(R.id.tv);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv.setText(et.getText());
            }
        });
    }

}
