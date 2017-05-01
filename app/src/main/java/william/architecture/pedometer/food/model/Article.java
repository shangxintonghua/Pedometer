package william.architecture.pedometer.food.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/4/23.
 * 推荐食品相关类
 */

public class Article extends BmobObject{

    private String cat;//文章类别
    private String content;//文章内容
    private String articleuniqid;//文章发布时间
    private String title;//文章标题
    private String author;//作者或者来源
    private String pic;//图片地址

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticleuniqid() {
        return articleuniqid;
    }

    public void setArticleuniqid(String articleuniqid) {
        this.articleuniqid = articleuniqid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
