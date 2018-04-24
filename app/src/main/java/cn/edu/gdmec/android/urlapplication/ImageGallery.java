package cn.edu.gdmec.android.urlapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageGallery extends AppCompatActivity {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mGalleryLinearLayout;
    private ProgressDialog dialog ;
    private View itemView;
    private ImageView imgView;

    private String[] m_imageURLs = new String[] {
            "http://www.w3school.com.cn/i/eg_mouse.jpg",
            "http://b393.photo.store.qq.com/psb?/313793d8-4fcf-48c9-8593-e47af98f7953/UIAZ8K2IrtjgqW2NrC2m9TKjsUj9O47VBmQmHnyDpb8!/b/dAkcQupoBgAA&bo=SQHcAAAAAAABALM!&rf=viewer_4",
            "http://b396.photo.store.qq.com/psb?/313793d8-4fcf-48c9-8593-e47af98f7953/hW8hD10HQogTjH22yW*kN2tNkcfjschgSWIntRkn09I!/b/dK4WEuxTDQAA&bo=XgHaAAAAAAABAKI!&rf=viewer_4" };




@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_image_gallery);
        initView();
        new DownTask().execute(m_imageURLs);
    }

    private void initView() {
    //添加弹出的对话框
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在下载图片，请稍后。。。");
        mContext=this;
        mGalleryLinearLayout=(LinearLayout) findViewById(R.layout.activity_image_gallery);
        mLayoutInflater=LayoutInflater.from(mContext);

    }

    /**异步任务执行网络下载图片
     * 定义一个类继承 AsyncTask　<Params, Progress, Result>
     * Params: 指定的是我们传递给异步任务执行时的参数的类型，网络图片路径
       Progress: 指定的是我们的异步任务在执行的时候将执行的进度返回给UI线程的参数的类型
       Result: 指定的是异步任务执行完后返回给UI线程的结果的类型
     */
    public class DownTask extends AsyncTask<String,Void,Bitmap[]> {

        @Override
        //在界面上现实提示框
        //这个方法是在执行异步任务之前的时候执行，
        // 并且是在UI Thread当中执行的，
        // 通常我们在这个方法里做一些UI控件的初始化的操作，
        // 例如弹出ProgressDialog
        protected void onPreExecute() {
           dialog.show();
        }

        @Override
        protected Bitmap[] doInBackground(String... strings) { //三个点，代表可变参数
            //完成对网络数据的提取
            //decodeStream 从输入流加载
//            a.开启异步线程去获取网络图片
//            b.网络返回InputStream
//            c.解析：Bitmap bm = BitmapFactory.decodeStream(stream),这是一个耗时操作，要在子线程中执行
            Bitmap[] bms=new Bitmap[strings.length];
            for (int i=0;i<strings.length;i++){
                try {
                    URL url=new URL(strings[i]);
                    URLConnection con;
                    con=url.openConnection();
                    InputStream is=con.getInputStream();
                    Bitmap bm= BitmapFactory.decodeStream(is);
                    is.close();
                    bms[i]=bm;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return bms;
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {//主要是更新ui
            super.onPostExecute(bitmaps);
            for (int i=0;i<bitmaps.length;i++){
                itemView=mLayoutInflater.inflate(R.layout.item,null);
                imgView=(ImageView) itemView.findViewById(R.id.imageView);
                imgView.setImageBitmap(bitmaps[i]);
                mGalleryLinearLayout.addView(itemView);
            }
            dialog.dismiss();
        }
    }
}
