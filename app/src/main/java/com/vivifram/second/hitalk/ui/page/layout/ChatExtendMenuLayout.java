package com.vivifram.second.hitalk.ui.page.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vivifram.second.hitalk.R;
import com.zuowei.utils.common.DisplayUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 按+按钮出来的扩展按钮
 *
 */
public class ChatExtendMenuLayout extends GridView{

    protected Context context;
    private List<ChatMenuItemModel> itemModels = new ArrayList<ChatMenuItemModel>();

    public ChatExtendMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public ChatExtendMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatExtendMenuLayout(Context context) {
        super(context);
        init(context, null);
    }
    
    private void init(Context context, AttributeSet attrs){
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChatExtendMenu);
        int numColumns = ta.getInt(R.styleable.ChatExtendMenu_numColumns, 4);
        ta.recycle();
        
        setNumColumns(numColumns);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setGravity(Gravity.CENTER_VERTICAL);
        setVerticalSpacing(DisplayUtil.dip2px(context, 8));
    }
    
    /**
     * 初始化
     */
    public void init(){
        setAdapter(new ItemAdapter(context, itemModels));
    }
    
    /**
     * 注册menu item
     * 
     * @param name
     *            item名字
     * @param drawableRes
     *            item背景
     * @param itemId
     *             id
     * @param listener
     *            item点击事件
     */
    public void registerMenuItem(String name, int drawableRes, int itemId, ChatExtendMenuItemClickListener listener) {
        ChatMenuItemModel item = new ChatMenuItemModel();
        item.name = name;
        item.image = drawableRes;
        item.id = itemId;
        item.clickListener = listener;
        itemModels.add(item);
    }
    
    /**
     * 注册menu item
     * 
     * @param nameRes
     *            item名字的resource id
     * @param drawableRes
     *            item背景
     * @param itemId
     *             id
     * @param listener
     *            item点击事件
     */
    public void registerMenuItem(int nameRes, int drawableRes, int itemId, ChatExtendMenuItemClickListener listener) {
        registerMenuItem(context.getString(nameRes), drawableRes, itemId, listener);
    }
    
    
    private class ItemAdapter extends ArrayAdapter<ChatMenuItemModel>{

        private Context context;

        public ItemAdapter(Context context, List<ChatMenuItemModel> objects) {
            super(context, 1, objects);
            this.context = context;
        }
        
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ChatMenuItem menuItem = null;
            if(convertView == null){
                convertView = new ChatMenuItem(context);
            }
            menuItem = (ChatMenuItem) convertView;
            menuItem.setImage(getItem(position).image);
            menuItem.setText(getItem(position).name);
            menuItem.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    if(getItem(position).clickListener != null){
                        getItem(position).clickListener.onClick(getItem(position).id, v);
                    }
                }
            });
            return convertView;
        }
        
        
    }
    
    
    public interface ChatExtendMenuItemClickListener{
        void onClick(int itemId, View view);
    }
    
    
    class ChatMenuItemModel{
        String name;
        int image;
        int id;
        ChatExtendMenuItemClickListener clickListener;
    }
    
    class ChatMenuItem extends LinearLayout {
        private ImageView imageView;
        private TextView textView;

        public ChatMenuItem(Context context, AttributeSet attrs, int defStyle) {
            this(context, attrs);
        }

        public ChatMenuItem(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        public ChatMenuItem(Context context) {
            super(context);
            init(context, null);
        }

        private void init(Context context, AttributeSet attrs) {
            LayoutInflater.from(context).inflate(R.layout.chat_menu_item_layout, this);
            imageView = (ImageView) findViewById(R.id.image);
            textView = (TextView) findViewById(R.id.text);
        }

        public void setImage(int resid) {
            imageView.setBackgroundResource(resid);
        }

        public void setText(int resid) {
            textView.setText(resid);
        }

        public void setText(String text) {
            textView.setText(text);
        }
    }
}
