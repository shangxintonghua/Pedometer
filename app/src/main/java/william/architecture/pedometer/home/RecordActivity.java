package william.architecture.pedometer.home;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

import william.architecture.pedometer.R;

public class RecordActivity extends FragmentActivity implements View.OnClickListener{
    private LineChart mLineChart;
    private ImageView mReturn;

    private Random random;//用于产生随机数

    private LineDataSet dataSet;//数据集
    private LineData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        mLineChart = (LineChart) findViewById(R.id.lc_chart);
        mReturn=(ImageView)findViewById(R.id.iv_return);
        mReturn.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        /**图表具体设置*/
        ArrayList<Entry> entries = new ArrayList<>();//显示条目
        ArrayList<String> xVals = new ArrayList<String>();//横坐标标签
        random=new Random();//随机数

        //添加数据
        for(int i=0;i<7;i++){
            float profit= random.nextFloat()*1000;
            //entries.add(BarEntry(float val,int positon);
            entries.add(new BarEntry(profit,i));
            xVals.add("第"+(i+1)+"日");
        }


        dataSet = new LineDataSet(entries, "一周跑步记录");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data = new LineData(xVals, dataSet);
        mLineChart.setData(data);
        //设置Y方向上动画animateY(int time);
        mLineChart.animateY(3000);
        //图表描述
        mLineChart.setDescription("七日跑步记录数(单位：步)");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_return:
                finish();
                break;
            default:
                break;
        }
    }
}
