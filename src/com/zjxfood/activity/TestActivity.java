package com.zjxfood.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.zjxfood.adapter.TestListAdapter;
import com.zjxfood.model.Student;
import com.zjxfood.view.DrawView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/20.
 */
public class TestActivity extends Activity{
    private DrawView bDrawl;
    private Student s;
//    private ListView mListView;
    private TestListAdapter mAdapter;
    private ArrayList<String> mList;
    private ArrayList<ArrayList<String>> mList2;
    private ExpandableListView mExpandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_expand_list);//将view视图放到Activity中显示
        init();
//        ArrayList<Student> al = new ArrayList<Student>();
//        al.add(new Student("Currency"));
//        al.add(new Student("UserId"));
//        al.add(new Student("partner"));
//        Collections.sort(al);
//        printElements(al);
//        Log.i("排序","================="+al);
        mList = new ArrayList<String>();
        for(int i=0;i<3;i++){
            mList.add("测试"+i);
        }
        mList2 = new ArrayList<ArrayList<String>>();
        mList2.add(mList);
        for(int i=0;i<3;i++){
            mList2.add(mList);
        }
        mAdapter = new TestListAdapter(getApplicationContext(),mList,mList2);
        mExpandList.setAdapter(mAdapter);
//        mListView.setAdapter(mAdapter);
    }

    private void init(){
//        mListView = (ListView) findViewById(R.id.test_expand_list);
        mExpandList = (ExpandableListView) findViewById(R.id.test_expand_list);
    }
//    public void printElements(Collection c) {
//        Iterator it = c.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//            Log.i("printElements","================="+it.next());
//        }
//    }

}
