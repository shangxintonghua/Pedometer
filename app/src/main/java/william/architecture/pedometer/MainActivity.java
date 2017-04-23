package william.architecture.pedometer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import william.architecture.pedometer.discover.DiscoverFragment;
import william.architecture.pedometer.food.FoodFragment;
import william.architecture.pedometer.home.HomeFragment;
import william.architecture.pedometer.mine.MineFragment;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    //底部菜单5个LinearLayout
    private LinearLayout ll_home;
    private LinearLayout ll_food;
    private LinearLayout ll_run;
    private LinearLayout ll_discover;
    private LinearLayout ll_mine;


    //底部菜单4个title
    private TextView tv_home;
    private TextView tv_food;
    private TextView tv_discover;
    private TextView tv_mine;

    //4个image
    private ImageView iv_home;
    private ImageView iv_food;
    private ImageView iv_discover;
    private ImageView iv_mine;


    //4个fragment

    private Fragment home_fragment;
    private Fragment food_fragment;
    private Fragment discover_fragment;
    private Fragment mine_fragment;


    //标题
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //该方法要在setContentView前使用，否则 会报错..无效
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //上面设置消除title没有效果，在style设置也没有效果，只好调用activity中的settitle设置为空
        setTitle("");
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化控件
        initView();
        //初始化监听事件
        initEvent();
        //初始化和设置当前fragment
        initFragment(0);




    }

    /**
     * description:初始化控件
     */
    private void initView(){
        tv_title=(TextView)findViewById(R.id.tv_title);

        ll_home=(LinearLayout)findViewById(R.id.ll_home);
        ll_food=(LinearLayout)findViewById(R.id.ll_food);
        ll_run=(LinearLayout)findViewById(R.id.ll_run);
        ll_discover=(LinearLayout)findViewById(R.id.ll_discover);
        ll_mine=(LinearLayout)findViewById(R.id.ll_mine);


        tv_home=(TextView)findViewById(R.id.tv_home);
        tv_food=(TextView)findViewById(R.id.tv_food);
        tv_discover=(TextView)findViewById(R.id.tv_discover);
        tv_mine=(TextView)findViewById(R.id.tv_mine);


        iv_home=(ImageView)findViewById(R.id.iv_home);
        iv_food=(ImageView)findViewById(R.id.iv_food);
        iv_discover=(ImageView)findViewById(R.id.iv_discover);
        iv_mine=(ImageView)findViewById(R.id.iv_mine);

    }

    /**
     * description:初始化事件
     */
    private void initEvent(){
         ll_home.setOnClickListener(this);
         ll_food.setOnClickListener(this);
         ll_run.setOnClickListener(this);
         ll_discover.setOnClickListener(this);
         ll_mine.setOnClickListener(this);
    }

    /**
     * description:初始化fragment
     */
    private  void initFragment(int index){
      FragmentManager fragmentManager=getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        //隐藏所有fragment
        hideFragment(transaction);
        switch (index){
            case 0:
                if (home_fragment==null){
                    home_fragment=new HomeFragment();
                    transaction.add(R.id.fl_content,home_fragment);
                }else {
                    transaction.show(home_fragment);
                }
                break;
            case 1:
                if (food_fragment==null){
                    food_fragment=new FoodFragment();
                    transaction.add(R.id.fl_content,food_fragment);

                }else {
                    transaction.show(food_fragment);
                }
                break;
            case 2:
                if (discover_fragment==null){
                    discover_fragment=new DiscoverFragment();
                    transaction.add(R.id.fl_content,discover_fragment);
                }else {
                    transaction.show(discover_fragment);
                }
                break;
            case 3:
                if (mine_fragment==null){
                    mine_fragment=new MineFragment();
                    transaction.add(R.id.fl_content,mine_fragment);
                }else {
                    transaction.show(mine_fragment);
                }
                break;
            default:
                break;
        }
        //提交事务
        transaction.commit();


    }

    /**
     * description:隐藏fragment
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction){
        if (home_fragment!=null){
            transaction.hide(home_fragment);
        }
        if (food_fragment!=null){
            transaction.hide(food_fragment);
        }
        if (discover_fragment!=null){
            transaction.hide(discover_fragment);
        }
        if (mine_fragment!=null){
            transaction.hide(mine_fragment);
        }
    }


    @Override
    public void onClick(View view) {
        restartBotton();
        switch (view.getId()){
            case R.id.ll_home:
                iv_home.setImageResource(R.drawable.home_active);
                tv_home.setTextColor(getResources().getColor(R.color.module_active));
                initFragment(0);
                setCoumtTitle("首页");
                break;
            case R.id.ll_food:
                iv_food.setImageResource(R.drawable.food_active);
                tv_food.setTextColor(getResources().getColor(R.color.module_active));
                initFragment(1);
                setCoumtTitle("推荐食物");
                break;
            case R.id.ll_discover:
                iv_discover.setImageResource(R.drawable.discover_active);
                tv_discover.setTextColor(getResources().getColor(R.color.module_active));
                initFragment(2);
                setCoumtTitle("发现");
                break;
            case R.id.ll_mine:
                iv_mine.setImageResource(R.drawable.mine_active);
                tv_mine.setTextColor(getResources().getColor(R.color.module_active));
                initFragment(3);
                setCoumtTitle("个人中心");
                break;
            case R.id.ll_run:
                break;
            default:
                break;
        }

    }

    //重置按钮
    private void restartBotton(){


        iv_home.setImageResource(R.drawable.home_unactive);
        iv_food.setImageResource(R.drawable.food_unactive);
        iv_discover.setImageResource(R.drawable.discover_unactive);
        iv_mine.setImageResource(R.drawable.mine_unactive);


        tv_home.setTextColor(getResources().getColor(R.color.module_unactive));
        tv_food.setTextColor(getResources().getColor(R.color.module_unactive));
        tv_discover.setTextColor(getResources().getColor(R.color.module_unactive));
        tv_mine.setTextColor(getResources().getColor(R.color.module_unactive));
    }
    private void setCoumtTitle(String title){
        tv_title.setText(title);
    }
}
