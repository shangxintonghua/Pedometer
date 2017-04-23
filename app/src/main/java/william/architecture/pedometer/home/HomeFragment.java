package william.architecture.pedometer.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import william.architecture.pedometer.R;
import william.architecture.pedometer.constant.Constant;
import william.architecture.pedometer.model.StepData;
import william.architecture.pedometer.service.StepService;
import william.architecture.pedometer.until.DbUtils;
import william.architecture.pedometer.until.SharedPreferencesUtils;
import william.architecture.pedometer.until.StepEnableUtils;
import william.architecture.pedometer.view.StepArcView;

/**
 * Created by Administrator on 2017/4/17.
 * description:首页fragment
 */

public class HomeFragment extends Fragment implements Handler.Callback{

    private SharedPreferencesUtils sp;
    private StepArcView stepArcView;
    private TextView mCalorie;//卡路里
    private TextView mStepcount;//目标步数
    private TextView mKilometer;//行走公里数
    private Handler delayHandler;


    private static String CURRENTDATE = "";//当前时间
    private int  CURRENT_SETP;//当前步数




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.home_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assignViews();//添加控件
        initTodayData();//取得今天步数
        Log.d("step",String.valueOf(CURRENT_SETP));
        initexpression();//计算卡路里和公里数
        initData();//初始化数据

    }

    /**
     * 初始化控件
     */
    private void assignViews() {
        stepArcView = (StepArcView)getActivity().findViewById(R.id.step_arc_view);
        mCalorie=(TextView)getActivity().findViewById(R.id.tv_calorie);
        mStepcount=(TextView)getActivity().findViewById(R.id.tv_stepcount);
        mKilometer=(TextView)getActivity().findViewById(R.id.tv_Kilometer);

    }


    /**
     * 初始化软环中的步数
     */

    private void initData() {
        sp = new SharedPreferencesUtils(getContext());
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        stepArcView.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        if (StepEnableUtils.isSupportStepCountSensor(getContext())) {

            delayHandler = new Handler(this);
            setupService();
        } else {

        }
    }


    private boolean isBind = false;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Messenger messenger;


    /**
     * 从service服务中拿到步数
     *
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                stepArcView.setCurrentCount(Integer.parseInt(planWalk_QTY), msg.getData().getInt("step"));

                break;
        }
        return false;
    }

    /**
     * 格局步数计算公式
     *int做除法，小数点不会保留
     */
    private void initexpression(){

        float kilometer=(float)CURRENT_SETP/1333;   //步数转换公里
        float calorie=(float)(60*kilometer*1.03); //步数计算卡路里,体重暂定 为60kg
        java.text.DecimalFormat  decimalFormat=new java.text.DecimalFormat("0.00");

        mKilometer.setText(decimalFormat.format(kilometer));
        mCalorie.setText(decimalFormat.format(calorie));

    }

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(getContext(), StepService.class);
        isBind = getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        getContext().startService(intent);


    }


    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            getContext().unbindService(conn);
        }
    }


    /**
     * 初始化当天的步数
     */
    private void initTodayData() {
        CURRENTDATE = getTodayDate();
        DbUtils.createDb(getContext(), "jingzhi");
        DbUtils.getLiteOrm().setDebugged(false);
        //获取当天的数据，用于展示
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
        if (list.size() == 0 || list.isEmpty()) {
            CURRENT_SETP = 0;
        } else if (list.size() == 1) {
            Log.v("xf", "StepData=" + list.get(0).toString());
            CURRENT_SETP = Integer.parseInt(list.get(0).getStep());
        } else {
            Log.v("xf", "出错了！");
        }
//        updateNotification("今日步数：" + StepDcretor.CURRENT_SETP + " 步");
    }

    /**
     * 获取今天时间
     * @return
     */
    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }




}
