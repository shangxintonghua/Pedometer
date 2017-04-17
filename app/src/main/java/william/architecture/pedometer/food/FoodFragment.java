package william.architecture.pedometer.food;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import william.architecture.pedometer.R;

/**
 * Created by Administrator on 2017/4/17.
 * description:食谱fragment
 */

public class FoodFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_fragment,container,false);
    }
}
