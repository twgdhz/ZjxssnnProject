package com.zjxfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zjxfood.activity.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/2.
 */
public class TestListAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private ArrayList<String> mList;
    private ArrayList<ArrayList<String>> mList2;
    private LayoutInflater mInflater;
    public TestListAdapter(Context context, ArrayList<String> list,ArrayList<ArrayList<String>> list2){
        this.mContext = context;
        this.mList = list;
        this.mList2 = list2;
        mInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mList2.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        GroupHolder mHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.text_list_item2,null);
            mHolder = new GroupHolder();
            mHolder.txt = (TextView) convertView.findViewById(R.id.test_list_item2_text);
            // groupHolder.img = (ImageView) convertView
            // .findViewById(R.id.img);
            convertView.setTag(mHolder);
        } else {
            mHolder = (GroupHolder) convertView.getTag();
        }
        mHolder.txt.setText(mList.get(i));
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.test_list_item3,null);
            itemHolder = new ItemHolder();
            itemHolder.txt = (TextView) convertView.findViewById(R.id.test_list_item3_text);
            itemHolder.mListView = (ListView) convertView.findViewById(R.id.test_list_item3_list);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }

        itemHolder.txt.setText(mList2.get(i).get(
                i1));
        final ItemHolder finalItemHolder = itemHolder;
        itemHolder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalItemHolder.mListView.getVisibility()==View.VISIBLE){
                    finalItemHolder.mListView.setVisibility(View.GONE);
                }else{
                    TestAdapter2 mAdapter = new TestAdapter2(mContext);
                    finalItemHolder.mListView.setAdapter(mAdapter);
                    finalItemHolder.mListView.setVisibility(View.VISIBLE);
                }

            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    class GroupHolder {
        public TextView txt;
    }

    class ItemHolder {
        public TextView txt;
        ListView mListView;
    }
}
