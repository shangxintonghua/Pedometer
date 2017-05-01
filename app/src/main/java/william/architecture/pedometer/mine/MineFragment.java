package william.architecture.pedometer.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import william.architecture.pedometer.R;

/**
 * Created by Administrator on 2017/4/17.
 * description:个人fragment
 */

public class MineFragment extends Fragment implements View.OnClickListener {

    private LinearLayout ll_report;
    private LinearLayout ll_bluetood;
    private LinearLayout ll_question;
    private LinearLayout ll_agerment;
    private LinearLayout ll_share;
    private LinearLayout ll_setting;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.mine_fragment,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assignViews();

    }

    /**
     * 初始化控件
     */
    private void assignViews(){
        ll_report=(LinearLayout)getActivity().findViewById(R.id.ll_report);
        ll_bluetood=(LinearLayout)getActivity().findViewById(R.id.ll_bluetood);
        ll_question=(LinearLayout)getActivity().findViewById(R.id.ll_question);
        ll_agerment=(LinearLayout)getActivity().findViewById(R.id.ll_agerment);
        ll_share=(LinearLayout)getActivity().findViewById(R.id.ll_share);
        ll_setting=(LinearLayout)getActivity().findViewById(R.id.ll_setting);

        //bind the clicklistener
        ll_report.setOnClickListener(this);
        ll_bluetood.setOnClickListener(this);
        ll_question.setOnClickListener(this);
        ll_agerment.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_setting.setOnClickListener(this);

    }

    /**
     * 搜索蓝牙设备
     */
    private void search(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_report:
                break;
            case R.id.ll_bluetood:
                //搜索蓝牙设备
                search();
                break;
            case R.id.ll_question:
                break;
            case R.id.ll_agerment:
                break;
            case R.id.ll_share:
                break;
            case R.id.ll_setting:
                break;
        }
    }
}
