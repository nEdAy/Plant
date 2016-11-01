package com.xpple.plant.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xpple.plant.CustomApplcation;
import com.xpple.plant.R;
import com.xpple.plant.bean.User;
import com.xpple.plant.view.ActivityBase;
import com.xpple.plant.view.HeaderLayout.onRightImageButtonClickListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * 实现定位，并使用MyLocationOverlay绘制定位位置， 同时展示使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class LocationSourceActivity extends ActivityBase {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	boolean isFirstLoc = true;// 是否首次定位

	BmobUser targetUser;
	User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);
		mCurrentMode = LocationMode.NORMAL;
		initTopBarForBoth("我的位置", R.drawable.base_action_bar_more_bg_selector,
				"模式  切换", new onRightImageButtonClickListener() {
					@Override
					public void onClick() {
						// TODO Auto-generated method stub
						switch (mCurrentMode) {
						case NORMAL:
							mCurrentMode = LocationMode.FOLLOWING;
							mBaiduMap
									.setMyLocationConfigeration(new MyLocationConfiguration(
											mCurrentMode, true, mCurrentMarker));
							break;
						case COMPASS:
							mCurrentMode = LocationMode.NORMAL;
							mBaiduMap
									.setMyLocationConfigeration(new MyLocationConfiguration(
											mCurrentMode, true, mCurrentMarker));
							break;
						case FOLLOWING:
							mCurrentMode = LocationMode.COMPASS;
							mBaiduMap
									.setMyLocationConfigeration(new MyLocationConfiguration(
											mCurrentMode, true, mCurrentMarker));
							break;
						}
					}
				});
		mHeaderLayout.getRightImageButton().setEnabled(false);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.cmapView);
		mBaiduMap = mMapView.getMap();
		// 设置缩放级别
		mBaiduMap.setMaxAndMinZoomLevel(18, 13);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000); // 设置定时定位的时间间隔

		targetUser = (BmobUser) getIntent().getSerializableExtra("user");
		if (targetUser != null) {

			BmobQuery<User> query = new BmobQuery<User>();
			query.addWhereEqualTo("username", targetUser.getUsername());
			query.findObjects(this, new FindListener<User>() {
				@Override
				public void onSuccess(List<User> arg0) {
					// TODO Auto-generated method stub
					user = arg0.get(0);
					double Latitude = user.getLocation().getLatitude();
					double Longitude = user.getLocation().getLongitude();
					friendlocation(Latitude, Longitude);
				}

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
				}
			});

		}
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	public void friendlocation(double Latitude, double Longitude) {
		// 定义Maker坐标点
		LatLng point = new LatLng(Latitude, Longitude);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option2 = new MarkerOptions().position(point).icon(
				bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option2);
		getwalkingroute(point);
	}

	RoutePlanSearch mSearch = RoutePlanSearch.newInstance();

	public void getwalkingroute(LatLng point) {
		OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
			@Override
			public void onGetWalkingRouteResult(WalkingRouteResult result) {
				// 获取步行线路规划结果
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(LocationSourceActivity.this,
							"抱歉，未找到可行步行线路结果", Toast.LENGTH_SHORT).show();
				}
				if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
					// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
					// result.getSuggestAddrInfo()
					return;
				}
				if (result.error == SearchResult.ERRORNO.NO_ERROR) {
					WalkingRouteOverlay overlay = new WalkingRouteOverlay(
							mBaiduMap);
					mBaiduMap.setOnMarkerClickListener(overlay);
					overlay.setData(result.getRouteLines().get(0));
					overlay.addToMap();
					overlay.zoomToSpan();
					int a = result.getRouteLines().get(0).getDistance();
					String str = String.valueOf(a);
					Toast.makeText(LocationSourceActivity.this,
							"步行总距离" + str + "米", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onGetTransitRouteResult(TransitRouteResult result) {
				// 获取公交换乘路径规划结果
			}

			@Override
			public void onGetDrivingRouteResult(DrivingRouteResult result) {
				// 获取驾车线路规划结果
			}
		};
		mSearch.setOnGetRoutePlanResultListener(listener);
		LatLng mylocation = new LatLng(
				CustomApplcation.lastPoint.getLatitude(),
				CustomApplcation.lastPoint.getLongitude());
		PlanNode stNode = PlanNode.withLocation(mylocation);
		PlanNode enNode = PlanNode.withLocation(point);
		mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(
				enNode));

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 释放检索实例
		mSearch.destroy();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}
