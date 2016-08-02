package com.zjxfood.application;

import java.util.LinkedList;
import java.util.List;

import com.zjxfood.common.Constants;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class ExitApplication extends Application{
	private List activityList = new LinkedList();
	private List scenicList = new LinkedList();
	private List addressList = new LinkedList();
	private List myList = new LinkedList();
	private List myJhList = new LinkedList();
	private List myMallList = new LinkedList();
	private List mMyAccountList = new LinkedList();
	private List mMyOrderList = new LinkedList();
	private List mCommList = new LinkedList();
	private List mMallDetailList = new LinkedList();
	private List mRouteList = new LinkedList();
	private static ExitApplication instance;
	private List mMallList = new LinkedList();
	private List mCashList = new LinkedList();
	private ExitApplication(){
		
	}
	// 单例模式中获取唯一的MyApplication实例
	public static ExitApplication getInstance(){
		if(null==instance){
			instance = new ExitApplication();
		}
		return instance;
	}
	//添加activity到容器中
	public void addCashList(Activity activity){
		mCashList.add(activity);
	}
	public void addMallDetail(Activity activity){
		mMallDetailList.add(activity);
	}
	public void addMallList(Activity activity){
		mMallList.add(activity);
	}

	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	
	public void addScenicActivity(Activity activity){
		scenicList.add(activity);
	}
	public void addAddressActivity(Activity activity){
		addressList.add(activity);
	}
	
	public void addMyActivity(Activity activity){
		myList.add(activity);
	}
	public void addJhActivity(Activity activity){
		myJhList.add(activity);
	}
	public void addMallActivity(Activity activity){
		myMallList.add(activity);
	}
	public void addMyAccountActivity(Activity activity){
		mMyAccountList.add(activity);
	}
	
	public void addMyOrderActivity(Activity activity){
		mMyOrderList.add(activity);
	}
	
	public void addMyCommodityActivity(Activity activity){
		mCommList.add(activity);
	}
	public void addRouteActivity(Activity activity){
		mRouteList.add(activity);
	}
	// 遍历所有Activity并finish
    public void exit()
    {
        for(int i=0;i<activityList.size();i++){
        	((Activity)activityList.get(i)).finish();
        } 
        Constants.clearUserInfo();
    }
    public void finishAll(){
    	for(int i=0;i<scenicList.size();i++){
        	((Activity)scenicList.get(i)).finish();
        } 
    }
    public void finishCash(){
    	for(int i=0;i<mCashList.size();i++){
        	((Activity)mCashList.get(i)).finish();
        } 
    }
    public void finishAddress(){
    	for(int i=0;i<addressList.size();i++){
        	((Activity)addressList.get(i)).finish();
        } 
    }
    //关掉关于“我的”所有界面
    public void finishMy(){
    	for(int i=0;i<myList.size();i++){
        	((Activity)myList.get(i)).finish();
        } 
    }
    //关掉关于“激活支付”的界面
    public void finishJh(){
    	for(int i=0;i<myJhList.size();i++){
        	((Activity)myJhList.get(i)).finish();
        } 
    }
  //关掉关于“商城支付”的界面
    public void finishMall(){
    	for(int i=0;i<myMallList.size();i++){
        	((Activity)myMallList.get(i)).finish();
        } 
    }
  //关掉关于“我的账户”的界面
    public void finishMyAccount(){
    	for(int i=0;i<mMyAccountList.size();i++){
        	((Activity)mMyAccountList.get(i)).finish();
        } 
    }
    public void finishMallList(){
    	for(int i=0;i<mMallList.size();i++){
        	((Activity)mMallList.get(i)).finish();
        } 
    }
  //关掉关于“商家订单”的界面
    public void finishMyOrder(){
    	for(int i=0;i<mMyOrderList.size();i++){
        	((Activity)mMyOrderList.get(i)).finish();
        } 
    }
  //关掉商家的界面
    public void finishMyCommdity(){
    	for(int i=0;i<mCommList.size();i++){
        	((Activity)mCommList.get(i)).finish();
        } 
    }
  //关掉商城库存不足的界面
    public void finishMallDetail(){
    	for(int i=0;i<mMallDetailList.size();i++){
        	((Activity)mMallDetailList.get(i)).finish();
        } 
    }
    //关掉定位
    public void finishRoute(){
    	for(int i=0;i<mRouteList.size();i++){
        	((Activity)mRouteList.get(i)).finish();
        } 
    }
}
