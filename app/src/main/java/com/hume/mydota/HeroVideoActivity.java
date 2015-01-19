package com.hume.mydota;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

/**视频教学
 * Created by tcp on 2015/1/9.
 */
public class HeroVideoActivity extends Activity {
    private WebView wv = null;
    private FrameLayout mFullscreenContainer;//全屏时的视图
    private FrameLayout mContentView;
    private View mCustomView = null;
    private Boolean islandport = true;//true表示此时是竖屏，false表示此时横屏。
    String vid;//视频id
    String clientid = "a618bc2a77395c33";//账号id
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dota_video_fullscreen);
        vid = this.getIntent().getStringExtra("vid");//获取视频id
        initViews();
        initWebView();
    }

    private void initViews() {
        mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
        mContentView = (FrameLayout) findViewById(R.id.main_content);
        wv = (WebView) findViewById(R.id.webview_player);

    }
    private void initWebView() {
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        settings.setLoadWithOverviewMode(true);
        wv.setBackgroundColor(Color.DKGRAY); // 设置背景色
        wv.loadDataWithBaseURL(null,setWebView(clientid,vid),"text/html","UTF-8", null);//加载网页数据
        wv.setWebChromeClient(new MyWebChromeClient());
        wv.setWebViewClient(new MyWebViewClient());
//        Log.v("string", setWebView(clientid, vid));
    }
    /*JS代码处理*/
    class MyWebChromeClient extends WebChromeClient {
        private CustomViewCallback mCustomViewCallback;
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            /*如果存在一个视图，立即终止并新建一个*/
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomViewCallback = callback;//回调函数值
            wv.setVisibility(View.GONE);//隐藏webview
            mFullscreenContainer.addView(view);//将全屏布局放到当前视图中
            mCustomView = view;
            mFullscreenContainer.setVisibility(View.VISIBLE);//全屏布局显示
            mFullscreenContainer.bringToFront();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏
            setFullScreen();//设置全屏
        }

        public void onHideCustomView() {
            if (mCustomView == null) {
                return;
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mCustomView.setVisibility(View.GONE);
            mFullscreenContainer.removeView(mCustomView);
            mCustomView = null;
            mFullscreenContainer.setVisibility(View.GONE);
            mCustomViewCallback.onCustomViewHidden();
            quitFullScreen();//退出全屏
            wv.setVisibility(View.VISIBLE);
        }

    }

    private void quitFullScreen() {
        // 声明当前屏幕状态的参数并获取
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getParent().getWindow().setAttributes(attrs);
        getParent().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void setFullScreen() {
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        getParent().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 全屏下的状态码：1098974464
        // 窗口下的状态吗：1098973440
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

    }
    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack())
        {
            // 返回键退回
            wv.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onPause() {// 继承自Activity
        super.onPause();
        wv.onPause();
    }
    @Override
    public void onResume() {// 继承自Activity
        super.onResume();
        wv.onResume();
    }

    public String setWebView(String clientid, String vid) {
        return "<div id=\"youkuplayer\" style=\"width:100%;height:100%\"></div>\n" +
                "\t<script type=\"text/javascript\" src=\"http://player.youku.com/jsapi\">"+
                "player = new YKU.Player('youkuplayer',{styleid: '0',"+
                "client_id: "+"'"+clientid+"',"+
                "vid: "+"'"+vid+"',"+
                "embsig: 'VERSION_TIMESTAMP_SIGNATURE'"+
                "});\n" +
                "\t\t</script>";
    }

    /**
     * 当横竖屏切换时会调用该方法
     * @tcp
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            islandport = false;
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            islandport = true;

        }

    }
}

