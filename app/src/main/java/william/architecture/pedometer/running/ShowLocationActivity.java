package william.architecture.pedometer.running;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import william.architecture.pedometer.R;

/**
 * 跑步轨迹activity
 */
public class ShowLocationActivity extends Activity implements View.OnClickListener,AMapLocationListener{

    private MapView mMapView;//地图控件
    private AMap aMap;//地图实例
    private MyLocationStyle myLocationStyle;//定位蓝点样式

    private ImageView mReturn;
    private ImageView mShare;
    private Button mRun;
    private Button mCancle;
    private TextView mKilometer;//公里数


    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    //定位客户端
    private AMapLocationClient aMapLocationClient;
    //标记参数
    private MarkerOptions markerOption;
    //是否开始跑步
    private boolean isRun=false;


    //轨迹平滑客户端
    private LBSTraceClient mTraceClient;
    //收集原始数据
    private List<TraceLocation> traceLocations=new ArrayList<>();

    //绘制的点
    private Polyline polyline;

    //计时器
    private Chronometer chronometer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.mv_run);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        initView();

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //地图一些设置
        aMap.getUiSettings().setZoomControlsEnabled(false);
        //显示当前位置,定位模式实现，默认定位小蓝点太小了
        startLocation();






    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        aMapLocationClient.stopLocation();//停止定位
        mMapView.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();



    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }



    private void startLocation(){
        aMapLocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        aMapLocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        //mLocationOption.setInterval(2000);
         mLocationOption.setOnceLocation(true);//只定位一次
        //设置定位参数
        aMapLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        aMapLocationClient.startLocation();

    }

    /**
     * 初始化控件
     */
    private void initView(){
        mReturn=(ImageView)findViewById(R.id.iv_return);
        mShare=(ImageView)findViewById(R.id.iv_share);
        mRun=(Button)findViewById(R.id.bt_run);
        mCancle=(Button)findViewById(R.id.bt_cancle);
        mKilometer=(TextView)findViewById(R.id.tv_kilemter);
        chronometer=(Chronometer)findViewById(R.id.ch_time);

        //设置计时器显示时间

        chronometer.setFormat("HH:mm:ss");


        mReturn.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mRun.setOnClickListener(this);
        mCancle.setOnClickListener(this);

    }

    /**
     * 开始跑步
     */
    private void run(){
        chronometer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60);
        chronometer.setFormat("0"+String.valueOf(hour)+":%s");
        chronometer.start();
        isRun=!isRun;
        if (aMapLocationClient==null){
            aMapLocationClient = new AMapLocationClient(this);
        }
        if (mLocationOption==null){
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
        }

        //设置定位监听
        aMapLocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(false);//只定位一次
        //设置定位参数
        aMapLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        aMapLocationClient.startLocation();
    }


    /**
     * 跑步勾画轨迹
     */
    private void runRecord(AMapLocation amapLocation){
        mTraceClient = LBSTraceClient.getInstance(getApplicationContext());
        //构建list
        TraceLocation traceLocation=new TraceLocation();
        traceLocation.setLatitude(amapLocation.getLatitude());
        traceLocation.setLongitude(amapLocation.getLongitude());
        traceLocation.setSpeed(amapLocation.getSpeed());
        traceLocation.setTime(amapLocation.getTime());
        traceLocation.setBearing(amapLocation.getBearing());

        traceLocations.add(traceLocation);
        //进行轨迹纠偏
        mTraceClient.queryProcessedTrace(traceLocations.size(), traceLocations, LBSTraceClient.TYPE_AMAP, new TraceListener() {
            @Override
            public void onRequestFailed(int i, String s) {
                //暂时错误信息
                Log.e("tag","failed");
                Toast.makeText(ShowLocationActivity.this,s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTraceProcessing(int i, int i1, List<LatLng> list) {
                //过程中绘制点
                Log.e("tag","processing");


            }

            @Override
            public void onFinished(int i, List<LatLng> list, int distance, int i2) {
                //完成后处理，绘制终点
                Log.e("tag","finished");
                int end =list.size()-1;
                //跳转中心点
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(list.get(end).latitude, list.get(end).longitude), 17));
                polyline = aMap.addPolyline(new PolylineOptions().
                        addAll(list).width(10).color(Color.argb(255, 1, 1, 1)));
                mKilometer.setText(String.valueOf(distance));


            }
        });


    }

    /**
     * 显示当前位置
     */
    private void location(AMapLocation amapLocation){
        //  通过定位后获得的经纬度 为地图添加中心点 和地图比例  数字越小地图显示越多
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 17));
        Marker marker;
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.	HUE_ROSE))
                .position(new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude()))
                .draggable(true)
                .title("起点")
                .snippet(amapLocation.getDistrict())
                .draggable(true);
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
        aMapLocationClient.stopLocation();//停止定位
    }

    /**
     * 结束跑步
     */
    private void cancle(){
        isRun=false;
        //绘制终点
        Marker marker;

        int index=traceLocations.size()-1;//终点索引
        if (index>0){
            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.	HUE_ROSE))
                    .position(new LatLng(traceLocations.get(index).getLatitude(),traceLocations.get(index).getLongitude()))
                    .draggable(true)
                    .title("终点")
                    .snippet("完成跑步")
                    .draggable(true);
            marker = aMap.addMarker(markerOption);
            marker.showInfoWindow();
        }
        //清除记录点
        traceLocations.clear();
        aMapLocationClient.stopLocation();
        chronometer.stop();
    }

    /**
     * 分享
     */
    private void share(){

        screenShot();

    }

    /**
     * 截屏 只会截取地图部分
     */
    private void screenShot(){
        /**
         * 对地图进行截屏
         */
        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {

            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int status) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                if(null == bitmap){
                    return;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(
                            Environment.getExternalStorageDirectory() + "/test_"
                                    + sdf.format(new Date()) + ".png");
                    boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    try {
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    StringBuffer buffer = new StringBuffer();
                    if (b)
                        buffer.append("截屏成功 ");
                    else {
                        buffer.append("截屏失败 ");
                    }
                    if (status != 0)
                        buffer.append("地图渲染完成，截屏无网格");
                    else {
                        buffer.append( "地图未渲染完成，截屏有网格");
                    }
                    Toast.makeText(getApplicationContext(), buffer.toString(),Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

    }



    //定位回调
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间

                if (isRun){
                    runRecord(amapLocation);
                }else {
                    location(amapLocation);
                }





            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_return:
                chronometer.setBase(SystemClock.elapsedRealtime());//复位计时器，停止计时
                finish();
                break;
            case R.id.iv_share:
                share();
                break;

            case R.id.bt_run:
                run();
                break;
            case R.id.bt_cancle:
                cancle();
                break;
            default:
                break;
        }
    }
}


