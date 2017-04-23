package william.architecture.pedometer.discover;

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
import william.architecture.pedometer.discover.model.Article;

/**
 * Created by Administrator on 2017/4/23.
 */

public class DiscoverFragment extends Fragment {
    private ListView mArtilces;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discover_fragment,container,false);
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
        mArtilces=(ListView)getActivity().findViewById(R.id.lv_discover);

    }

    /**
     * 初始化listview采用万能适配器
     */
    private void initData(){
        List<Article> articles=new ArrayList<>();
        for(int i=0;i<10;i++){
            articles.add(new Article());
        }
        mArtilces.setAdapter(new CommonAdapter<Article>(getContext(),articles,R.layout.item_discover) {
            @Override
            protected void convertView(View item, Article food) {

            }
        });

    }
}
