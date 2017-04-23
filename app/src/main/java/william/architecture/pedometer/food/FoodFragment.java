package william.architecture.pedometer.food;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import william.architecture.pedometer.CommonAdapter;
import william.architecture.pedometer.R;
import william.architecture.pedometer.food.model.Food;

/**
 * Created by Administrator on 2017/4/17.
 * description:食谱fragment
 */

public class FoodFragment extends Fragment {
    private ListView mFoodList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assignViews();
        initData();
    }

    /**
     * 初始化控件
     */
    private void assignViews(){
        mFoodList=(ListView)getActivity().findViewById(R.id.lv_food);

    }

    /**
     * 初始化listview采用万能适配器
     */
    private void initData(){
        List<Food> foods=new ArrayList<>();
        for(int i=0;i<10;i++){
            foods.add(new Food());
        }
        mFoodList.setAdapter(new CommonAdapter<Food>(getContext(),foods,R.layout.item_food) {
            @Override
            protected void convertView(View item, Food food) {

            }
        });

    }
}
