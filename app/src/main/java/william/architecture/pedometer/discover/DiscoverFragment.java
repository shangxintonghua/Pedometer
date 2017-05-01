package william.architecture.pedometer.discover;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import william.architecture.pedometer.ArticleDetail;
import william.architecture.pedometer.CommonAdapter;
import william.architecture.pedometer.CommonViewHolder;
import william.architecture.pedometer.R;
import william.architecture.pedometer.food.model.Article;


/**
 * Created by Administrator on 2017/4/23.
 */

public class DiscoverFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{
    private ListView mArtilces;
    private SwipeRefreshLayout mSwipeLayout;//加载组件

    /**
     * 定义script的正则表达式
     */
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    /**
     * 定义style的正则表达式
     */
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REGEX_HTML = "<[^>]+>";
    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";

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

        mSwipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.id_discover_sp);
        mSwipeLayout.setColorSchemeColors(getResources().getColor(R.color.blue), getResources().getColor(R.color.green),
                getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.red));
        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setOnRefreshListener(this);

    }

    /**
     * 初始化数据
     */
    private void initData(){
        BmobQuery<Article> query=new BmobQuery<Article>();
        //查询cat=1的数据
        query.addWhereEqualTo("cat","0");
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
     * 初始化列表
     * @param articles
     */
    private void initList(final List<Article> articles){
        //设置适配器
        mArtilces.setAdapter(new CommonAdapter<Article>(getContext(),articles,R.layout.item_discover) {
            @Override
            protected void convertView(View item, Article food) {
                //文章标题
                TextView title= CommonViewHolder.get(item,R.id.tv_discover_title);
                title.setText(food.getTitle());

                //显示图片
                ImageView conver=CommonViewHolder.get(item,R.id.iv_discover);
                Glide.with(getContext()).load(food.getPic()).placeholder(R.drawable.downloading ).
                        error(R.drawable.downerror).into(conver);




            }
        });
        //设置item点击事件
        mArtilces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    /**
     * 下拉监听
     */
    @Override
    public void onRefresh() {
        initData();

    }


    /**
     * 过滤标签
     * @param htmlStr
     * @return
     */
    public  String delHTMLTag(String htmlStr) {
        // 过滤script标签
        Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        // 过滤style标签
        Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        // 过滤html标签
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        // 过滤空格回车标签
        Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("");
        return htmlStr.trim(); // 返回文本字符串
    }
}
