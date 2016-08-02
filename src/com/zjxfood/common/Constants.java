package com.zjxfood.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.project.util.DensityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constants {
	public final static float TARGET_HEAP_UTILIZATION = 0.75f;
	public static Bitmap mGiftBitmap;
	public static String mUserName, mPassWord, mId = "0", mPayPassword,
			mShMoney = "", mFid, mIsjh = "", mUserCode = "",LevelId="",UserLevelMemo="",unionid;
	public static ArrayList<HashMap<String, Object>> mProvinceList;
	public static ArrayList<HashMap<String, Object>> mCityLists;
	public static ArrayList<HashMap<String, Object>> mAreaLists;
	public static ArrayList<HashMap<String, Object>> mAllCityList;
	public static ArrayList<HashMap<String, Object>> mAllMerchantList;
	public static int grabNum = 0;
	public static final int[] gifts = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 5, 5, 8 };
	public static int onLine = 0;// 是否在线
	public static String fileUrl = "/sdcard/appImage/shishangnannv/";

	public static double longt, lat;// 经纬度
	public static String mRealname = "";
	public static String code;
//	public static HashMap<String,ArrayList<HashMap<String,Object>>> mCarsMap;
	public static HashMap<String,HashMap<String,Object>> mCarsMap = new HashMap<>();
	/**
	 * 支付宝支付参数
	 */
	// 商户PID
	public static final String PARTNER = "2088501069663028";
	// 商户收款账号
	public static final String SELLER = "2439659529@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKmI0FHolf4vSpy3q0PmRoSQQOaiGwrDOMeSohK56afuv9p13ZBksgaY2sW/dhIoy8D0c30zxi6cL/uKMZUXwcv4n3DNu49SbHUxyY1unn2UhgYpHM7NdVt0diZuYzmjoriPD6rONUBMTEjIZ9Qp4Uq8XG+exxx3vzo1pGxZFgHNAgMBAAECgYAUnAQvIOYzC063tIBGddT2wo6ROv2slkTtxf6b5ivodVavWQrBRex6btSRm4/f70OtxknP255pBiWzH36dkFDHp142RdS6pW7j8l1NI5ygowAkACb6SR506XFyTNJ1PdJc4lbD7aAmeB6Tx/rGT8441icBwf8JxfpMDhtcu67upQJBANO/rF92yJrd+56tT24XJJO8/gC+WLyg/wZvSu7NZQ+xWp4qq8xlQWMYr2Q502LiSeEYu+QRIntQ7vGCxAdXra8CQQDM9rizczFDaYrakKyjIHvAE9YeLwT8+trXqsYW57v0k//NXf+rV137g/99nF51Cnx1WFWEQLM6zD+fBWLQrYNDAkB40br21X3s75Asn9LcjBPwwDtmDTXwmyQodcQZsdRiOd3stYhFHdlLQ8B/vYbhuyBQcZ5zqVs1ZZ6Q6v4S+nI9AkAMiOoyz1Cr9sBvLXnhYBMxZAUsQ2PitNS9a8xBilqMUqDKRn1kVSuCPkhZvj5omgOSiZxxQiOz7NnXN9UbPgFRAkA4GZsre/ArkDvD8sqwq/+Bm+etvSsiZ1bj5u6T0+k4xmmnisUxRYedzhv25SttyjKB8CtskJt9MgQLhX7Hda+G";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpiNBR6JX+L0qct6tD5kaEkEDmohsKwzjHkqISuemn7r/add2QZLIGmNrFv3YSKMvA9HN9M8YunC/7ijGVF8HL+J9wzbuPUmx1McmNbp59lIYGKRzOzXVbdHYmbmM5o6K4jw+qzjVATExIyGfUKeFKvFxvnsccd786NaRsWRYBzQIDAQAB";
	public static final int SDK_PAY_FLAG = 1;

	public static final int SDK_CHECK_FLAG = 2;
	private final static double PI = 3.14159265358979323;// 圆周率
	private final static double r = 6371229;// 地球的半径
//	public static String partner = "&partner=app_android";
//	public static String key = "mxyh89g50q5iz7nbvp018vwn4svdkqvi";
	public static String partner = "&partner=app_android_new";
	public static String key = "mxyh89g50q5iz7nbvp018vwn4svdk007";
	/**
	 * 微信支付参数
	 */
	// 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data
	// android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
	public static final String APP_ID = "wxdb9b9f4b7b6c902f";
	public static final String APP_ID2 = "wx52c57940a775bcba";
	// 商户号
	public static final String MCH_ID = "1370787502";
	// AppSecret
	public static final String AppSecret = "fde392e92d75cdc9c3b5ae53d7515a29";
	public static final String AppSecret2 = "e41cd209a02fe036e4357a12feaf0e20";
	// API密钥，在商户平台设置
//	public static final String API_KEY = "ZJXssnn13579ZJXssnn13579ZJXdssnn";
	public static final String API_KEY = "05faf8f4d1ae29d8e2210c434546c428";
	// 通过code获取access_token
	public static final String gettoken = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	public static String access_token;
	public static String openid;
	public static String nickname,wxheadimgurl;
	// 获取微信用户信息
	public static final String userinfo = "https://api.weixin.qq.com/sns/userinfo?";
	/**
	 * 请求接口
	 */
	// 请求头
	 public static final String apiHead = "http://api.zjxssnn.com/api/";
	public static final String uploadPath = "http://api.zjxssnn.com/api/";

	// public static final String uploadPath = "http://192.168.1.100/api/";
//	 public static final String apiHead = "http://192.168.1.100/api/";

	// public static final String apiHead = "http://api.hexnews.com/api/";

//	 public static final String apiHead = "http://open.hexnews.com/api/";
	// 登录
	public static final String log = apiHead+ "user/getUser?partner=app_android_new&sign=";
	public static final String log2 = apiHead + "user/getUser?";
	// 注册
	// public static final String reg = apiHead
	// + "user/Register?partner=app_android_new&sign=";
	// public static final String reg2 = apiHead + "user/Register?";
	public static final String reg3 = apiHead + "user/registernew?";
	//
	// 检测手机号是否已经注册
	public static final String checkPhoneIsExist = apiHead
			+ "user/getUser?partner=app_android_new&sign=";
	// 获取省
//	public static final String getProvince = apiHead
//			+ "buyaddress/getProvince?partner=app_android_new&sign=";
	public static final String getProvince2 = apiHead
			+ "buyaddress/getProvince?";
	// 获取省id获取城市
	public static final String getCity = apiHead
			+ "buyaddress/getCityForProvinceid?partner=app_android_new&sign=";
	public static final String getCity2 = apiHead
			+ "buyaddress/getCityForProvinceid?";

	// 根据市级id获取区域 cityid-市级id
	public static final String getArea = apiHead
			+ "buyaddress/getAreaForCityId?partner=app_android_new&sign=";
	public static final String getArea2 = apiHead
			+ "buyaddress/getAreaForCityId?";
	// 签到
	public static final String getSign = apiHead
			+ "user/getUserQd?partner=app_android_new&sign=";
	public static final String getSign2 = apiHead + "user/getUserQd?";
	public static final String getSign3 = apiHead + "useraccount/sign?";
	// 根据店铺id获取店铺信息
	public static final String getIdMerchantInfo = apiHead
			+ "merchant/getMerchantForId?partner=app_android_new&sign=";
	public static final String getIdMerchantInfo2 = apiHead
			+ "merchant/getMerchantForId?";
	// 短信接口
	public static final String getSmsMsg = "http://210.5.152.198:1860/asmx/smsservice.aspx?name=zhongjiaxin&pwd=9EB7B1301C5E7204D69F3B9871A9";
	// 支付结果
	public static final String getPay = apiHead
			+ "user/postFk?partner=app_android_new&sign=";
	public static final String getPay2 = apiHead + "user/postFk?";
	// 修改登录密码
	public static final String modifyPwd = apiHead
			+ "user/postUserForPassword?partner=app_android_new&sign=";
	public static final String modifyPwd2 = apiHead
			+ "user/postUserForPassword?";
	// 修改支付密码
	public static final String modifyPayPwd = apiHead
			+ "user/postUserForPayPass?partner=app_android_new&sign=";
	// 根据用户id获取签到次数
	public static final String getQdCount = apiHead
			+ "user/getQdCount?partner=app_android_new&sign=";
	public static final String getQdCount2 = apiHead + "user/getQdCount?";
	// 新增收获地址
	public static final String addNewAddress = apiHead
			+ "buyaddress/postAddBuyaddress?partner=app_android_new&sign=";
	// 新异步回调地址
	public static final String callback = apiHead
			+ "PaymentNotifyUrl/notifyurl?";

	// 获取所有礼品信息
	// public static final String getAllGift =
	// apiHead+"gift/getAll?partner=app_android_new&sign=";
	// 根据用户id获取地址
	public static final String getAddress = apiHead
			+ "buyaddress/getbuyaddressfor?partner=app_android_new&sign=";
	public static final String getAddress2 = apiHead
			+ "buyaddress/getbuyaddressfor?";

	// 获取所有城市
	public static final String getAllCity = apiHead
			+ "buyaddress/getCity?partner=app_android_new&sign=";
	public static final String getAllCity2 = apiHead + "buyaddress/getCity?";

	// 修改用户名称
	public static final String modifyUserName = apiHead
			+ "user/updateUsername?partner=app_android_new&sign=";
	public static final String modifyUserName2 = apiHead
			+ "user/updateUsername?";
	// 获取用户订单列表
	public static final String getOrderList = apiHead
			+ "order/getorder?partner=app_android_new&sign=";
	public static final String getOrderList2 = apiHead + "order/getorder?";
	public static final String getOrderList3 = apiHead + "order/getuserorders?";
	// 充值食尚币
	public static final String chongzhiBi = apiHead
			+ "scorecard/postcz?partner=app_android_new&sign=";
	public static final String chongzhiBi2 = apiHead
			+ "useraccount/ShmoneyRecharge?";

	// 获取食尚币列表
	public static final String getMonsyList = apiHead
			+ "scorecard/getShmoney?partner=app_android_new&sign=";
	public static final String getMonsyList2 = apiHead
			+ "scorecard/getShmoney?";
	// 重置密码
	public static final String resetLoginPsw = apiHead
			+ "user/postUserForPasswordByMobile?partner=app_android_new&sign=";
	public static final String resetLoginPsw2 = apiHead
			+ "user/postUserForPasswordByMobile?";
	// 添加推荐人ID
	public static final String addUserFid = apiHead
			+ "user/updateUserFid?partner=app_android_new&sign=";
	// 获取用户的所有食尚币
	public static final String getUserShmoney = apiHead
			+ "user/getUsershmoney?partner=app_android_new&sign=";
	public static final String getUserShmoney2 = apiHead
			+ "user/getUsershmoney?";
	// 获取用户是否已经注册
	public static final String isReg = apiHead
			+ "user/getUser?partner=app_android_new&sign=";
	public static final String isReg2 = apiHead + "user/getUser?";
	public static final String isReg3 = apiHead + "user/existsusername?";
	// 获取我的会员/游客的数量
	public static final String getMyUser = apiHead
			+ "user/getMyUser?partner=app_android_new&sign=";
	public static final String getMyUser2 = apiHead + "user/getMyUser?";
	// 获取我的会员/游客的信息
	public static final String getMyUsers = apiHead
			+ "user/getMyUsers?partner=app_android_new&sign=";
	public static final String getMyUsers2 = apiHead + "user/getMyUsers?";
	// 版本号获取更新
	public static final String UPDATEVERSIONXMLPATH = "http://api.zjxssnn.com/update.xml";
	// 上传头像
	public static final String upload = uploadPath
			+ "user/UploadAvatar?partner=app_android_new&sign=";
	// 获取商城订单
	public static final String getMallOrder = apiHead
			+ "giftorder/getAll?partner=app_android_new&sign=";
	public static final String getMallOrder2 = apiHead + "giftorder/getAll?";
	// 加载用户头像
	// public static final String loadHeadImage =
	// "http://api.hexnews.com/content/avatars/avatar-";
	public static String headPath = "";
	// 获取订单详情
	public static final String getOrderInfo = apiHead
			+ "order/getorder?partner=app_android_new&sign=";
	public static final String getOrderInfo2 = apiHead + "order/getorder?";
	// 评论
	public static final String addPl = uploadPath
			+ "pl/addPl?partner=app_android_new&sign=";
	// 根据商家id获取评论
	public static final String getEvaluationById = apiHead
			+ "pl/getPlForMid?partner=app_android_new&sign=";
	public static final String getEvaluationById2 = apiHead + "pl/getPlForMid?";
	// 抢红包
	public static final String getShmoneyByHb = apiHead
			+ "pl/red?partner=app_android_new&sign=";
	public static final String getShmoneyByHb2 = apiHead + "pl/red?";
	// 获取用户激活时间
	public static final String getJhDate = apiHead
			+ "user/getUserJhTime?partner=app_android_new&sign=";
	public static final String getJhDate2 = apiHead + "user/getUserJhTime?";
	// 获取用户未评价订单
	public static final String getOrderNotEval = apiHead
			+ "order/getordernopl?partner=app_android_new&sign=";
	public static final String getOrderNotEval2 = apiHead
			+ "order/getordernopl?";
	// 获取商家
	public static final String getMerchant = apiHead
			+ "merchant/getMerchants?partner=app_android_new&sign=";
	public static final String getMerchant2 = apiHead
			+ "merchant/getMerchants?";
	// public static final String getMerchant3 = apiHead
	// + "merchant/getMerchants?";
	// public static final String getMerchantBy5 = apiHead
	// + "merchant/getMerchantForIndex?";
	public static final String getMerchantBy1 = apiHead
			+ "merchant/getMerchantForIndex?partner=app_android_new&sign=";
	public static final String getMerchantBy2 = apiHead
			+ "merchant/getMerchantForIndex?";
	public static final String getMerchantBy3 = apiHead
			+ "merchant/getrecommendations?";
	// 判断地区是否额外收取运费
	public static final String getOtherYf = apiHead
			+ "buyaddress/getotheryf?partner=app_android_new&sign=";
	public static final String getOtherYf2 = apiHead + "buyaddress/getotheryf?";
	// 创建用户商城购买订单（支付之前）
	public static final String createMallOrder = apiHead
			+ "giftorder/CreateOrder?partner=app_android_new&sign=";
	public static final String createMallOrder2 = apiHead
			+ "giftorder/createordernew?";
	public static final String createMallOrder3 = apiHead
			+ "useraccount/CreateGiftOrder?";
	// 创建用户商家订单（支付之前）
	public static final String createMerchantOrder = apiHead
			+ "order/CreateOrder?partner=app_android_new&sign=";
	public static final String createMerchantOrder2 = apiHead
			+ "order/createordernew?";
	public static final String createMerchantOrder3 = apiHead
			+ "useraccount/CreateMerchantOrder?";
	// 删除收货地址
	public static final String deleteAddress = apiHead
			+ "buyaddress/delete?partner=app_android_new&sign=";
	public static final String deleteAddress2 = apiHead + "buyaddress/delete?";

	// 发送注册验证码
	public static final String sendCode = apiHead
			+ "sms/register?partner=app_android_new&sign=";
	public static final String sendCode2 = apiHead + "sms/register?";
	public static final String sendCode3 = apiHead + "sms/sendvalidatecode?";
	// 获取商城商品，可根据名称、分组获取商品，并可指定排序
	public static final String getMallList = apiHead
			+ "gift/getlist?partner=app_android_new&sign=";
	public static final String getMallList2 = apiHead + "gift/getlist?";
	// 获得商品类型
	public static final String getMallType = apiHead
			+ "gift/getgroup?partner=app_android_new&sign=";
	// 默认地址
	public static final String getDefaultAddress = apiHead
			+ "buyaddress/getdefault?partner=app_android_new&sign=";
	public static final String getDefaultAddress2 = apiHead
			+ "buyaddress/getdefault?";
	// 激活金额
	public static final String getJhMoney = apiHead
			+ "user/getjhmoney?partner=app_android_new&sign=";
	public static final String getJhMoney2 = apiHead + "user/getjhmoney?";
	// 获取用户上级合作商，有美容院取美容院，没有就取代理商
	public static final String getUserMerchant = apiHead
			+ "user/getUserUpperMerchant?partner=app_android_new&sign=";
	public static final String getUserMerchant2 = apiHead
			+ "user/getUserUpperMerchant?";
	// 获取商品尺码大小
	public static final String getMallSize = apiHead
			+ "gift/getchima?partner=app_android_new&sign=";
	public static final String getMallSize2 = apiHead + "gift/getchima?";
	// 查看商品详情
	public static final String getMallDetail = apiHead
			+ "gift/getbyid?partner=app_android_new&sign=";
	public static final String getMallDetail2 = apiHead + "gift/getbyid?";
	public static final String getMallDetail3 = apiHead + "Gift2/ProductInfo?";
	// 查看物流
	public static final String lookup = apiHead
			+ "giftorder/lookup?partner=app_android_new&sign=";
	public static final String lookup2 = apiHead + "giftorder/lookup?";
	// 获取用户食尚币清零剩余天数
	public static final String getUserSignOutDay = apiHead
			+ "user/getTimeOutRemainDayWithAutoClear?partner=app_android_new&sign=";
	public static final String getUserSignOutDay2 = apiHead
			+ "user/getTimeOutRemainDayWithAutoClear?";
	// 根据商户id获取商户信息
	public static final String getMerchantById = apiHead
			+ "merchant/getMerchantForMid?partner=app_android_new&sign=";
	public static final String getMerchantById2 = apiHead
			+ "merchant/getMerchantForMid?";
	// 获取用户订单详情页面
	public static final String getOrderInfoByPayId = apiHead
			+ "order/getorderbypayid?partner=app_android_new&sign=";
	public static final String getOrderInfoByPayId2 = apiHead
			+ "order/getorderdetail?";
	// 获取用户会员/游客数量
	public static final String getmyusernum = apiHead
			+ "user/getmyusernum?partner=app_android_new&sign=";
	public static final String getmyusernum2 = apiHead + "user/getmyusernum?";
	// 获取平台通知公告
	public static final String getnotice = apiHead
			+ "user/getnotice?partner=app_android_new&sign=";
	public static final String getnotice2 = apiHead + "user/getnotice?";
	// 设置默认地址
	public static final String setDefaultAddress = apiHead
			+ "buyaddress/addbuyaddress?partner=app_android_new&sign=";
	public static final String setDefaultAddress2 = apiHead
			+ "buyaddress/addbuyaddress?";
	// 获取餐饮类型
	public static final String getGroup = apiHead
			+ "merchant/getgroups?partner=app_android_new&sign=";
	public static final String getGroup2 = apiHead + "merchant/getgroups?";
	// 商户入驻
	public static final String merchantSettled = uploadPath
			+ "merchant/apply?partner=app_android_new&sign=";
	// 商城首页猜你喜欢
	public static final String getguessyoulike = apiHead
			+ "gift/getguessyoulike?partner=app_android_new&sign=";
	public static final String getguessyoulike2 = apiHead
			+ "gift/getguessyoulike?";
	// 获取商城直属下级分类
	public static final String getMallChildType = apiHead
			+ "GiftGroup/GetChildrenByID?";
	public static final String getMallChildType2 = apiHead
			+ "gift/getgroupbycode?";
	public static final String getMallChildType3 = apiHead
			+ "GiftGroup/GetAllChildrenByID?";
	// 获取商品参数
	public static final String getMallValue = apiHead + "gift/getattributes?";
	// 获取订餐列表
	public static final String getReserveList = apiHead
			+ "Dishes/GetDishesList?";
	// 获取平台用户数量和商家数量
	public static final String getNums = apiHead + "system/getnum?";
	// 添加商品收藏
	public static final String addFavorite = apiHead + "gift/addfavorite?";
	// 获取用户收藏商品
	public static final String getFavorite = apiHead + "gift/getuserfavorites?";
	// 检测用户是否已收藏该商品
	public static final String isFavorite = apiHead
			+ "gift/existsuserfavorite?";
	// 删除商品收藏
	public static final String deleteFavorite = apiHead
			+ "gift/removefavorite?partner=app_android_new&sign=";
	public static final String deleteFavorite2 = apiHead
			+ "gift/removefavorite?";
	// 检测游客是否可以购买商品
	public static final String checkUserBuy = apiHead + "gift/checkuserbuy?";
	// 创建餐饮消费订单
	public static final String createNewMerchant = apiHead
			+ "order/createordernew?";
	// 获取消费币
	public static final String getCurrency = apiHead + "user/getcurrency?";
	// 获取消费币列表
	public static final String getCurrencyList = apiHead
			+ "user/getcurrencydetail?";
	// 创建消费币充值订单
	public static final String createrechargeorder = apiHead
			+ "user/createrechargeorder?";
	// 获得拍卖列表
	public static final String getAuctionList = apiHead
			+ "AuctionService/GetAuctionList?";
	// 获得拍卖商品详情
	public static final String getAuctionDetail = apiHead
			+ "AuctionService/GetAuctionInfo?";
	// 获得竞拍商品记录列表
	public static final String getAuctionRecord = apiHead
			+ "AuctionService/GetAuctionPriceList?";
	// 商品竞价
	public static final String outPrice = apiHead
			+ "AuctionService/GetQuotedPrice?";
	// 创建竞拍订单
	public static final String createAuctionOrder = apiHead
			+ "AuctionOrderService/GetCreateOrder?";

	// 获取竞拍记录列表
	public static final String getAuctionOrderList = apiHead
			+ "AuctionService?";
	// 获取竞拍订单列表
	public static final String getAuctionOrderLists = apiHead
			+ "AuctionOrderService/GetUserOrderList?";
	// 获取竞拍订单详情
	public static final String getAuctionOrderDetail = apiHead
			+ "AuctionOrderService/GetOrderByOrderNum?";
	// 根据地址ID获取地址信息
	public static final String getAddressById = apiHead
			+ "buyaddress/getaddress?";
	// 获取平台广告词
	public static final String getAdListByPosition = apiHead
			+ "Ad/GetAdListByPosition?";
	// 获取所有说明内容
	public static final String getSetting = "http://img.zjxssnn.com/app/app_person.json?";
	// 获取抽奖列表
	public static final String getIndianaList = apiHead
			+ "LuckyGameService/GetGameList?";
	// 获取现金抽奖列表
	public static final String getCashList = apiHead
			+ "CarGameService/GetGameList?";
	// 获取抽奖详情
	public static final String getIndianaDetail = apiHead
			+ "LuckyGameService/GetGameInfo?";
	// 获取现金抽奖详情
	public static final String getCashDetail = apiHead
			+ "CarGameService/GetGameInfo?";
	// 获取一个抽奖号
	public static final String getCj = apiHead + "LuckyGameService?";
	// 我的抽奖游戏列表
	public static final String getMyIndiana = apiHead + "LuckyGameService?";
	// 我的现金抽奖游戏列表
	public static final String getMyCash = apiHead + "CarGameService?";
	// 抽奖游戏参加情况列表
	public static final String getJoinList = apiHead
			+ "LuckyGameService/GetJoinList?";
	// 抽奖现金抽奖参加情况列表
	public static final String getCashJoinList = apiHead
			+ "CarGameService/GetJoinList?";
	// 创建抽奖订单
	public static final String createIndianaOrder = apiHead
			+ "LuckyGameOrder/GetCreateOrder?";
	// 创建现金抽奖订单
	public static final String createCashYfOrder = apiHead
			+ "CarGameOrder/GetCreateOrder?";
	// 获取抽奖订单
	public static final String getIndianaOrder = apiHead
			+ "LuckyGameOrder/GetUserOrderList?";
	// 获取现金抽奖订单
	public static final String getCashOrder = apiHead
			+ "CarGameOrder/GetUserOrderList?";
	// 抽奖图片地址
	public static final String indianaUrl = "http://img.zjxssnn.com/banner/index-lucky.png";
	// 最后50个时间之和
	public static final String getLastSum = apiHead
			+ "LuckyGameService/GetLastDateTimeSum?";
	// 最后现金抽奖50个时间之和
	public static final String getCashLastSum = apiHead
			+ "CarGameService/GetLastDateTimeSum?";
	// 余额支付
	public static final String cashJh = apiHead + "user/JhByCurrency?";
	// 创建现金抽奖订单
	public static final String createCashOrder = apiHead
			+ "CarGameService/getCreateGameNumOrder?";
	// 获取用户现金抽奖中购买奖号记录
	public static final String getCashAllOrderList = apiHead
			+ "CarGameService/getGameNumOrderList?";
	// 余额支付现金抽奖运费
	public static final String payCashYf = apiHead
			+ "CarGameOrder/GetVirtualPay?";
	// 余额支付夺宝运费
	public static final String payIndianaYf = apiHead
			+ "LuckyGameOrder/GetVirtualPay?";
	// 余额支付竞拍运费
	public static final String payAuctionYf = apiHead
			+ "AuctionOrderService/GetVirtualPay?";
	// 可抢的红包列表
	public static final String getRedList = apiHead + "HB/GetICanReciveList?";
	// 发红包
	public static final String issueRed = apiHead + "HB/GetSendHb?";
	// 抢红包
	public static final String grabRed = apiHead + "HB/GetReciveHb?";
	// 某个红包的领取记录
	public static final String getGrabList = apiHead + "HB/GetReciveHbList?";
	// 我领取到的红包记录
	public static final String getMyRedList = apiHead + "HB/GetMyReciveList?";
	// 抢到的红包总数和金额
	public static final String getTotalRed = apiHead + "HB/GetMyReciveCount?";
	//我发出去的红包列表
	public static final String getSendRed = apiHead + "HB/GetSendHBList?";
	//我发出去的红包总数
	public static final String getSendTotal = apiHead + "HB/GetMySendCount?";

	//该红包详情
	public static final String getRedInfo = apiHead + "HB/GetHbInfo?";
	//注册是否需要推荐码
	public static final String getNeedJhm = apiHead + "Version/GetNeedTJM?";
	//我的余额
	public static final String getMyCurrency = apiHead + "useraccount/GetUserBalance?";
	//获取货币明细
	public static final String getGoldMx = apiHead + "useraccount/GetUserAccountChangeDetail?";
	//微信注册
	public static final String wxRegister = apiHead + "user/BindWxByTJM?";
	//微信登录
	public static final String wxLog = apiHead + "user/IsBindWeiXin?";
	//获取非余额界面数据
	public static final String getGoldShmony = apiHead + "useraccount/GetUserAccountWithIncomeAndCost?";
	//获取明显总额
	public static final String getAllDetail = apiHead + "useraccount/GetUserAccountFromDetail?";
	//获取我的下级
	public static final String getMyUsersubordinate = apiHead + "useraccount/GetUserSubPath?";
	//获取旗下游客奖励
	public static final String getMyTouristReward = apiHead + "useraccount/GetUserSubActiveNoArriveMoney?";
	//我的推广二维码
	public static final String getMyCode = "http://api.zjxssnn.com/WX/GetMyQrCode?";
	//商城猜你喜欢
	public static final String getMallLikes = apiHead+"Gift2/YouLike?";
	//新商品列表获取
	public static final String getShopList = apiHead+"Gift2/ProductsByShop?";
	//获取分类图标
	public static final String getIconList = apiHead+"GiftGroup/GetChildrenByID?";
	//获取余额
	public static final String getBalance = apiHead+ "useraccount/NormalMoneyWithdrawals?";
	//温馨提示
	public static final String wxts = "http://mp.weixin.qq.com/s?__biz=MzIwMDQxNzU2OA==&mid=502297075&idx=1&sn=baff3f8aec3abfd4d1d3ce66205820fb&scene=23&srcid=06213eK80W2nwZz86jLiTXVT#rd";
	//奖励规则
	public static final String jlgz = "http://mp.weixin.qq.com/s?__biz=MzIwMDQxNzU2OA==&mid=502297071&idx=1&sn=605c4f2ef47e32848791fa9bfc4d15f7&scene=23&srcid=0614VZsxYFC0tBDwzOij20bj#wechat_redirect";
	//获取推荐成功人数和收益
	public static final String getTjRy = apiHead+"useraccount/GetUserSubActiveNumAndMoney?";
	//获取推荐成功人数和收益
	public static final String guanyuJb = "http://mp.weixin.qq.com/s?__biz=MzI0ODA5MTk5Mw==&mid=502023139&idx=1&sn=91ed709ca942883350f2718a13cd140b&scene=23&srcid=0714ix4G0CUwIFybVfTAObdy#rd";
	//商城精选
	public static final String getMallJx = apiHead+"Gift2/ProductTopN?";
	//获取游客
	public static final String getUserClear = apiHead+"useraccount/GetUserClearRemainDays?";
	//新商城订单
	public static final String getNewMallOrder = apiHead+"StoreOrder/GetOrderList?";
	//创建订单
	public static final String createNewMallOrder = apiHead+"StoreOrder/InsertOrder?";
	//购物车费用计算
	public static final String getAllPrice = apiHead+"StoreOrder/CaculatePrice?";
	//金币兑换
	public static final String Exchange = apiHead +"useraccount/Exchange?";
	//取消订单
	public static final String cancelOrder = apiHead +"StoreOrder/UserCancelOrder?";

	// 解析json对象数据
	public static HashMap<String, Object> getJsonObject(String json) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<?> it = jsonObject.keys();
			String a;
			while (it.hasNext()) {
				a = it.next().toString();
				map.put(a, jsonObject.get(a).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap<String, HashMap<String,Object>> getJsonObjectToMap(String json) {
		HashMap<String, HashMap<String,Object>> maps = new HashMap<>();
//		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<?> it = jsonObject.keys();
			String a;
			while (it.hasNext()) {
				a = it.next().toString();
//				map.put(a, jsonObject.get(a).toString());
				maps.put(a, getJsonObject(jsonObject.get(a).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maps;
	}
	public static HashMap<String, Object> getJson2Object(String json)
			throws JSONException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonObject = new JSONObject(json);
		Iterator<?> it = jsonObject.keys();
		String a;
		while (it.hasNext()) {

			a = it.next().toString();
			map.put(a, jsonObject.get(a).toString());
		}
		return map;
	}

	public static ArrayList<HashMap<String, Object>> getJsonArrayByData(
			String json) throws JSONException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		JSONObject jsonObject = new JSONObject(json);
		Iterator<?> it = jsonObject.keys();
		String a;
		while (it.hasNext()) {
			a = it.next().toString();
			map.put(a, jsonObject.get(a).toString());
		}
		try {
			list = JSON.parseObject(map.get("Data").toString(),
					new TypeReference<ArrayList<HashMap<String, Object>>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static HashMap<String, Object> getJsonObjectByData(String json) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<?> it = jsonObject.keys();
			String a;
			while (it.hasNext()) {
				a = it.next().toString();
				map.put(a, jsonObject.get(a).toString());
			}
			map2 = getJsonObject(map.get("Data").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map2;
	}

	// 解析json数组数据
	public static ArrayList<HashMap<String, Object>> getJsonArray(
			String jsonString) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			list = JSON.parseObject(jsonString,
					new TypeReference<ArrayList<HashMap<String, Object>>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}



	// 清楚缓存
	public static void clearUserInfo() {
		Constants.onLine = 0;
		Constants.mUserName = "";
		Constants.mId = "0";
		Constants.mPayPassword = "";
		Constants.mShMoney = "";
		Constants.mPassWord = "";
		Constants.mIsjh = "";
		Constants.openid = "";
	}

	// 计算两点之间距离 单位为km
	public static double getDistance(double longt1, double lat1, double longt2,
			double lat2) {
		double x, y, distance;
		x = (longt2 - longt1) * PI * r
				* Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
		y = (lat2 - lat1) * PI * r / 180;
		distance = Math.hypot(x, y);
		return distance / 1000;
	}

	// 请求接口排序
	public static String sortsStr(String res) {
		String result = "";
		res = res + partner;
		String[] str = res.split("&");
		Arrays.sort(str);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			if (i < str.length - 1) {
				sb.append(str[i]).append("&");
			} else {
				sb.append(str[i]);
			}
		}
		sb.append(key);
		result = sb.toString();
		Log.i("sb", sb + "=======sb=============");
		return md5(result);
	}
//	public static String sortsStr2(String res) {
//		String result = "";
//		res = res + "app_android";
//		String[] str = res.split("&");
//		Arrays.sort(str);
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < str.length; i++) {
//			if (i < str.length - 1) {
//				sb.append(str[i]).append("&");
//			} else {
//				sb.append(str[i]);
//			}
//		}
//		sb.append("mxyh89g50q5iz7nbvp018vwn4svdkqvi");
//		result = sb.toString();
//		Log.i("sb", sb + "=======sb=============");
//		return md5(result);
//	}
	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
	// 请求接口排序 参数为空
	public static String sortsStrNull(String res) {
		String result = "";
		res = res + "partner=app_android_new";
		String[] str = res.split("&");
		Arrays.sort(str);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			if (i < str.length - 1) {
				sb.append(str[i]).append("&");
			} else {
				sb.append(str[i]);
			}
		}
		sb.append(key);
		result = sb.toString();
		Log.i("sb", sb + "=======sb=============");
		return md5(result);
	}

	// md5加密
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isNull(Object object) {
		if (object == null) {
			return true;
		} else {
			return false;
		}
	}

	// 手机号验证
	public static boolean checkPhoneNum(String phoneNum) {
		Pattern pattern = Pattern.compile("^1\\d{10}$");
		Matcher matcher = pattern.matcher(phoneNum);

		if (matcher.matches()) {
			return true;
		}
		return false;
	}
	public static Bitmap createCornerBitmap(Bitmap oldBitmap, int num, Context context) {
		// 新建画布
		int width = oldBitmap.getWidth();
		int height = oldBitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		// --->先画原来的图片
		Paint bitmapPaint = new Paint();
		// 防止抖动
		bitmapPaint.setDither(true);
		// 对Bitmap进行滤波处理
		bitmapPaint.setFilterBitmap(true);
		Rect src = new Rect(0, 0, oldBitmap.getWidth(), oldBitmap.getHeight());
		Rect dst = new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight());
		canvas.drawBitmap(oldBitmap, src, dst, bitmapPaint);
		// 画圆 设置成数字的背景
		Paint paintCircle = new Paint(); // 设置一个笔刷大小是3的红色的画笔
		paintCircle.setColor(Color.RED);
		paintCircle.setStrokeJoin(Paint.Join.ROUND);
		paintCircle.setStrokeCap(Paint.Cap.ROUND);
		paintCircle.setStrokeWidth(3);
		canvas.drawCircle(
				width / 2 + DensityUtils.dp2px(context, 135),
				height / 3 - DensityUtils.dp2px(context, 115),
				DensityUtils.dp2px(context, 15), paintCircle);
		// --->再画新加的数字
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(Color.WHITE);
		countPaint.setTextSize(DensityUtils.dp2px(context, 120));
		countPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(num + "",
				width / 2 + DensityUtils.dp2px(context, 30),
				height / 3 - DensityUtils.dp2px(context, 8),
				countPaint);
		Log.i("购物车图标","====================");
		return newBitmap;
	}
	public static Bitmap createCarsBitmap(Bitmap oldBitmap, int num,Context context) {
		// 新建画布
		int width = oldBitmap.getWidth();
		int height = oldBitmap.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		// --->先画原来的图片
		Paint bitmapPaint = new Paint();
		// 防止抖动
		bitmapPaint.setDither(true);
		bitmapPaint.setAntiAlias(true);
		// 对Bitmap进行滤波处理
		bitmapPaint.setFilterBitmap(true);
		Rect src = new Rect(0, 0, oldBitmap.getWidth(), oldBitmap.getHeight());
		Rect dst = new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight());
		canvas.drawBitmap(oldBitmap, src, dst, bitmapPaint);
		// 画圆 设置成数字的背景
		Paint paintCircle = new Paint(); // 设置一个笔刷大小是3的红色的画笔
		paintCircle.setColor(Color.RED);
		paintCircle.setStrokeJoin(Paint.Join.ROUND);
		paintCircle.setStrokeCap(Paint.Cap.ROUND);
		paintCircle.setStrokeWidth(3);
		paintCircle.setAntiAlias(true);
		canvas.drawCircle(
				width / 2 + DensityUtils.dp2px(context, 10),
				height / 3,
				DensityUtils.dp2px(context, 10), paintCircle);
		// --->再画新加的数字
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(Color.WHITE);
		countPaint.setAntiAlias(true);
		countPaint.setTextSize(DensityUtils.dp2px(context, 13));
		countPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(num + "",
				width / 2 + DensityUtils.dp2px(context,6),
				height / 3+ DensityUtils.dp2px(context,4) ,
				countPaint);
		Log.i("购物车图标","====================");
		return newBitmap;
	}
}
