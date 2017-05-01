package william.architecture.pedometer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import william.architecture.pedometer.food.model.Article;

/**
 * 文章详情页面
 */
public class ArticleDetail extends Activity implements View.OnClickListener{

    private ImageView mReturn;
    private TextView mTitle;
    private WebView mArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        Bundle bundle = this.getIntent().getExtras();
        String id=bundle.getString("id");
        String title=bundle.getString("title");

        initView(title);
        initData(id);


    }

    /**
     * 初始化控件
     */
    private void initView(String title){
        mReturn=(ImageView)findViewById(R.id.iv_return);
        mTitle=(TextView)findViewById(R.id.tv_title);
        mArticle=(WebView)findViewById(R.id.mv_article);


        mArticle.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应屏幕，内容将自动缩放

        //截取字符串
        int length=title.length();
        if (length>20){
            title=title.substring(0,18);
        }
        mTitle.setText(title);

        mReturn.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void  initData(String id){

        BmobQuery<Article> query = new BmobQuery<Article>();
        query.getObject(id, new QueryListener<Article>() {

            @Override
            public void done(Article article, BmobException e) {
                if(e==null){
                    mArticle.loadDataWithBaseURL(null,article.getContent(),"text/html", "utf-8",null);

                }else{
                    //错误提示信息
                    Toast.makeText(ArticleDetail.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        });
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
