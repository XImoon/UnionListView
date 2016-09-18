package cn.ximoon.unionlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ContactView extends RelativeLayout {

    public interface ContactData{
        String getData();
        String getSlug();
    }

    public static interface OnContactClickListener{
        public void onClick(View v, int position);
    }

    private OnContactClickListener mListener;

    private ListView leftListView;
    private ListView rightListView;

    private ContactAdapter mLeftAdapter;
    private ContactAdapter mRightAdapter;

    private Context mContext;

    /** 左侧联动右侧的关系表*/
    private HashMap<Integer,Integer> linkMap = new HashMap<Integer,Integer>();
    /** 右侧联动左侧的关系表*/
    private HashMap<Integer,Integer> linkMap_ = new HashMap<Integer,Integer>();

    private int left_width;

    private int last_category = 0;

    private boolean isClickCategory = false;


    public ContactView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 控件初始化
     */
    private void init(){
        left_width = mContext.getResources().getDimensionPixelSize(R.dimen.px_300_w750);

        leftListView = new ListView(mContext);
        leftListView.setVerticalScrollBarEnabled(false);
        leftListView.setDividerHeight(0);
        leftListView.setCacheColorHint(mContext.getResources().getColor(R.color.transparent));
        leftListView.setSelector(R.color.transparent);
        rightListView = new ListView(mContext);
        rightListView.setVerticalScrollBarEnabled(false);
        rightListView.setDividerHeight(0);
        rightListView.setCacheColorHint(mContext.getResources().getColor(R.color.transparent));
        rightListView.setSelector(R.color.transparent);

        mLeftAdapter = new ContactAdapter(mContext);
        mLeftAdapter.setShowSpc(true);
        mLeftAdapter.setOnItemClickListener(new OnContactClickListener() {
            @Override
            public void onClick(View v, int position) {
                // 左侧列表点击使右侧列表联动
                setCategory(position,true);
            }
        });
        mRightAdapter = new ContactAdapter(mContext);
        mRightAdapter.setCategory(true);
        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);

        LayoutParams rlp_m = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_1_w750), LayoutParams.MATCH_PARENT);
        rlp_m.leftMargin = left_width-mContext.getResources().getDimensionPixelSize(R.dimen.px_3_w750);
        View spc = new View(mContext);
        spc.setBackgroundColor(mContext.getResources().getColor(R.color.f0));
        this.addView(spc,rlp_m);

        LayoutParams rlp_l = new LayoutParams(left_width, LayoutParams.MATCH_PARENT);
        this.addView(leftListView,rlp_l);

        LayoutParams rlp_r = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rlp_r.leftMargin = left_width;
        this.addView(rightListView,rlp_r);

        rightListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(isClickCategory){
                    isClickCategory = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!isClickCategory) {
                    // 获取右侧列表的第一条item对应的左侧列表索引值
                    int cindex = getCategoryFromContent(firstVisibleItem);
                    if (cindex != -1 && last_category != cindex) {
                        // 刷新视图
                        setCategory(cindex, false);
                        // 设置平滑滚动
                        leftListView.smoothScrollToPosition(cindex);
                    }
                }
            }
        });
    }

    /**
     * 设置左侧导航列表数据
     * @param datas
     */
    public void setLeftData(List<? extends ContactData> datas){
        mLeftAdapter.setData(datas);
        mLeftAdapter.notifyDataSetChanged();
    }

    /**
     * 设置右侧列表数据
     * @param datas
     */
    public void setRightData(List<? extends ContactData> datas){
        mRightAdapter.setData(datas);
        mRightAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新视图使呈现联动关系
     * @param catposition   左侧列表的key
     * @param can           是否需要联动
     */
    public void setCategory(int catposition, boolean can){
        // 记录当前点击位置
        last_category = catposition;
        mLeftAdapter.setFocus(catposition);
        mLeftAdapter.notifyDataSetChanged();
        isClickCategory = can;
        if(can) {
            // 获取右侧列表的关联位置
            int cindex = getContentFromCategory(catposition);
            if (cindex != -1) {
                // 使右侧list跳转到制定位置
                rightListView.setSelection(cindex);
            }
        }
    }

    /**
     * 获取左侧列表的联动位置
     * @param c   右侧列表当前的位置
     * @return    左侧列表的联动位置
     */
    public int getCategoryFromContent(int c){
        if(linkMap_.containsKey(c)){
            int cindex = linkMap_.get(c);
            if(cindex >= 0 && cindex < mLeftAdapter.getCount()){
                return cindex;
            }
        }
        return -1;
    }

    /**
     * 返回右侧list的关联位置
     * @param c 左侧listview的索引位置
     * @return
     */
    public int getContentFromCategory(int c){
        if(linkMap.containsKey(c)){
            int cindex = linkMap.get(c);
            if(cindex >= 0 && cindex < mRightAdapter.getCount()){
                return cindex;
            }
        }
        return -1;
    }

    /**
     * 设置右侧列表的item监听器
     * @param l
     */
    public void setOnRightListItemClickListener(OnContactClickListener l){
        mRightAdapter.setOnItemClickListener(l);
    }

    /**
     *  设置左侧列表的联动关系
     * @param cat           左侧索引位置
     * @param content       右侧列表对应位置
     */
    public void pushLink(int cat,int content){
        linkMap.put(cat,content);
    }

    /**
     * 设置右侧联动的关系
     * @param content   右侧索引位置
     * @param cat       左侧列表对应位置
     */
    public void pushContrastLink(int content,int cat){
        linkMap_.put(content,cat);
    }

    /**
     * 联动列表默认监听器
     */
    public static class ContactAdapter extends BaseAdapter implements OnClickListener {
        private Context mContext;
        private List<? extends ContactData> mDatas;
        private int focus = 0;
        private boolean showSpc = false;

        private boolean isCategory = false;

        private ViewHolder currentFocus;
        private OnContactClickListener mListener;

        public ContactAdapter(Context context){
            mContext = context;
        }
        public ContactAdapter(Context context, List<? extends ContactData> datas){
            mContext = context;
            mDatas = datas;
        }

        public void setData(List<? extends ContactData> datas){
            mDatas = datas;
        }

        public void setShowSpc(boolean show){
            showSpc = show;
        }

        public void setCategory(boolean is){
            isCategory = is;
        }

        public void setFocus(int position){
            focus = position;
        }

        public void setOnItemClickListener(OnContactClickListener l){
            mListener = l;
        }

        @Override
        public int getCount() {
            if(mDatas == null){
                return 0;
            }
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewholder;
            if(convertView == null){
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.view_contact,parent,false);
                viewholder.mContainer = (RelativeLayout)convertView.findViewById(R.id.contact_container);
                viewholder.mSpc = convertView.findViewById(R.id.contact_spc);
                viewholder.mText = (TextView)convertView.findViewById(R.id.contact_lefttext);
                convertView.setTag(viewholder);
                convertView.setOnClickListener(this);
            }else{
                viewholder = (ViewHolder)convertView.getTag();
            }
            viewholder.position = position;
            viewholder.mText.setText(mDatas.get(position).getData());
            // 配置右侧列表中的索引字符的标志
            if(mDatas.get(position).getSlug() == null && isCategory){
                convertView.findViewById(R.id.left_line).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.right_line).setVisibility(View.VISIBLE);
            }else{
                convertView.findViewById(R.id.left_line).setVisibility(View.GONE);
                convertView.findViewById(R.id.right_line).setVisibility(View.GONE);
            }
            // 配置联动关系显示style：文字的显示样式和红色分割线
            if(showSpc) {
                if (position == focus) {
                    currentFocus = viewholder;
                    viewholder.mSpc.setVisibility(View.VISIBLE);
                    viewholder.mText.setTextColor(mContext.getResources().getColor(R.color.color_f94972));
                } else {
                    viewholder.mSpc.setVisibility(View.GONE);
                    viewholder.mText.setTextColor(mContext.getResources().getColor(R.color.black_90));
                }
            }else{
                viewholder.mSpc.setVisibility(View.GONE);
            }


            return convertView;
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                Object holder = v.getTag();
                if(holder != null && holder instanceof ViewHolder) {
                    mListener.onClick(v, ((ViewHolder) holder).position);
                }
            }
        }

        private static class ViewHolder{
            public int position;
            public RelativeLayout mContainer;
            public View mSpc;
            public TextView mText;
        }
    }
}
