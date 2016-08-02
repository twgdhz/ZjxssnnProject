package com.zjxfood.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;
import com.zjxfood.interfaces.MallTypeInterImpl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/14.
 */
public class MallExpandAdapter extends BaseExpandableListAdapter{

    private Context mContext;
//    private ArrayList<String> mList;
    private ArrayList<HashMap<String,Object>> mList,mList2,mList3;
    private LayoutInflater mInflater;
    private MallTypeInterImpl mImpl;

    public MallExpandAdapter(Context context,ArrayList<HashMap<String,Object>> list,MallTypeInterImpl impl){
        this.mList = list;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mImpl = impl;
    }
    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if(mList.get(i).get("Childrens")!=null) {
            mList2 = Constants.getJsonArray(mList.get(i).get("Childrens").toString());
            return mList2.size();
        }else{
            return 0;
        }
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
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        GroupHolder mHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.mall_left_menu_item,null);
            mHolder = new GroupHolder();
            mHolder.txt = (TextView) convertView.findViewById(R.id.menu_item_text);
            convertView.setTag(mHolder);
        } else {
            mHolder = (GroupHolder) convertView.getTag();
        }
        mHolder.txt.setText(mList.get(i).get("name").toString());
        return convertView;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
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
        if(mList.get(i).get("Childrens")!=null){
            mList2 = Constants.getJsonArray(mList.get(i).get("Childrens").toString());
            if(mList2!=null && mList2.get(i1).get("name")!=null) {
                itemHolder.txt.setText(mList2.get(i1).get("name").toString());
            }
            itemHolder.mListView.setVisibility(View.GONE);
        }

        final ItemHolder finalItemHolder = itemHolder;
        itemHolder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList2 = Constants.getJsonArray(mList.get(i).get("Childrens").toString());
                mList3 = Constants.getJsonArray(mList2.get(i1).get("Childrens").toString());
                if(mList3!=null && mList3.size()>0){
                    Log.i("三级分类","=========显示========"+mList2.get(i1).get("Childrens"));
                    if(finalItemHolder.mListView.getVisibility()==View.VISIBLE){
                        finalItemHolder.mListView.setVisibility(View.GONE);
                    }else{
                        MallThreeAdapter mAdapter = new MallThreeAdapter(mContext,mList3);
                        finalItemHolder.mListView.setAdapter(mAdapter);
                        finalItemHolder.mListView.setVisibility(View.VISIBLE);

                        finalItemHolder.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                mList2 = Constants.getJsonArray(mList.get(i).get("Childrens").toString());
                                mList3 = Constants.getJsonArray(mList2.get(i1).get("Childrens").toString());
                                mImpl.onclick(position,mList3);
                            }
                        });
                    }
                }else{

                    Log.i("三级分类","=========没有数据========");
                    mImpl.onclick(i1,mList2);
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
        TextView txt;
    }
    class ItemHolder {
        TextView txt;
        ListView mListView;
    }
}
