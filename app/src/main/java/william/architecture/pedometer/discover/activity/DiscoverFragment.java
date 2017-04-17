package william.architecture.pedometer.discover.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import william.architecture.pedometer.R;

/**
 * Created by Administrator on 2017/4/17.
 * description:发现fragment
 */

public class DiscoverFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.discover_fragment,container,false);
    }
}
