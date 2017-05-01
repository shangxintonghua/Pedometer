package william.architecture.pedometer.food;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import william.architecture.pedometer.ArticleDetail;
import william.architecture.pedometer.CommonAdapter;
import william.architecture.pedometer.CommonViewHolder;
import william.architecture.pedometer.R;
import william.architecture.pedometer.food.model.Article;

/**
 * Created by Administrator on 2017/4/17.
 * description:食谱fragment
 */

public class FoodFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener {
    private ListView mFoodList;
    private SwipeRefreshLayout mSwipeLayout;//加载组件

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

        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setColorSchemeColors(getResources().getColor(R.color.blue), getResources().getColor(R.color.green),
                getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.red));
        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setOnRefreshListener(this);

    }

    /**
     * 查询bmob数据
     */
    private void initData(){
        BmobQuery<Article> query=new BmobQuery<Article>();
        //查询cat=1的数据
        query.addWhereEqualTo("cat","1");
        //设置返回数据条数
        query.setLimit(20);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e==null){
                    mSwipeLayout.setRefreshing(false);
                    initList(list);
                }else {
                    //错误提示信息
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });





    }

    /**
     * 监听下拉
     */
    @Override
    public void onRefresh() {
        initData();
    }

    /**
     * 初始化列表
     * @param articles
     */
    private void initList(final List<Article> articles){
        //设置适配器
        mFoodList.setAdapter(new CommonAdapter<Article>(getContext(),articles,R.layout.item_food) {
            @Override
            protected void convertView(View item, Article food) {
                //文章标题
                TextView title=CommonViewHolder.get(item,R.id.tv_title);
                title.setText(food.getTitle());
                //发布时间
                TextView time=CommonViewHolder.get(item,R.id.tv_time);
                time.setText(food.getUpdatedAt());

                //作者（来源）
                TextView author=CommonViewHolder.get(item,R.id.tv_author);
                author.setText(food.getAuthor());

                //显示图片
                ImageView conver=CommonViewHolder.get(item,R.id.iv_image);
                Glide.with(getContext()).load(food.getPic()).placeholder(R.drawable.downloading ).
                        error(R.drawable.downerror).into(conver);




            }
        });
        //设置item点击事件
        mFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ArticleDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", articles.get(position).getObjectId());
                bundle.putString("title",articles.get(position).getTitle());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
