package com.hume.mydota;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by tcp on 2015/1/19.
 */
public class VideoAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<String> list;
    private String clientid = "a618bc2a77395c33";//账号id

    public VideoAdapter(Context context,List list) {
        super();
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewvideo;
        if(convertView==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.dota_hero_video,null);
            viewvideo = new ViewHolder();
            viewvideo.webview = (WebView)view.findViewById(R.id.dota_web);
            view.setTag(viewvideo);
        }else{
            viewvideo = (ViewHolder)view.getTag();
        }
        if(viewvideo!=null){
            final String item_vid = (String) getItem(position);
            initWebView(viewvideo.webview,item_vid);
        }
        return view;
    }

    private void initWebView(WebView wv,String vid) {
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        settings.setLoadWithOverviewMode(true);
        wv.setBackgroundColor(Color.DKGRAY); // 设置背景色
        wv.setClickable(false);
        wv.setPressed(false);
        wv.setEnabled(false);
//        Log.v("string",setWebView(clientid,vid));
        wv.loadDataWithBaseURL(null,setWebView(clientid,vid),"text/html","UTF-8", null);
        wv.setWebChromeClient(new WebChromeClient());
        wv.setWebViewClient(new WebViewClient());
    }
    public String setWebView(String clientid, String vid) {
        return "<html>\n" +
                "\t<body>\n" +
                "\t<div id=\"youkuplayer\" style=\"width:100%;height:100%\"></div>\n" +
                "\t<script type=\"text/javascript\" src=\"http://player.youku.com/jsapi\">"+
                "player = new YKU.Player('youkuplayer',{styleid: '0',"+
                "client_id: "+"'"+clientid+"',"+
                "vid: "+"'"+vid+"',"+
                "embsig: 'VERSION_TIMESTAMP_SIGNATURE'"+
                "});\n" +
                "\t\t</script>\n" +
                "\t</body>\n" +
                "</html>";
    }
    private final class ViewHolder {
        public WebView webview;
    }
}
