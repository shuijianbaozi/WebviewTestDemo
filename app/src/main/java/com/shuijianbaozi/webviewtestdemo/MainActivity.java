package com.shuijianbaozi.webviewtestdemo;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 本用例旨在测试webview控件
 * 参考自:https://zhuanlan.zhihu.com/p/22247021
 */
public class MainActivity extends AppCompatActivity {
    
    private final String URL = "https://www.baidu.com";//https://www.baidu.com

    private WebView mWebView;
    private ProgressBar mProgressbar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAppBar();//初始化Toolbar
        initWebView();//初始化WebView
        initWebSettings();//初始化WebSettings
        initWebViewClient();//初始化WebViewClient
        initWebChromeClient();//初始化WebChromeClient

//        webView = (WebView) findViewById(R.id.wv_main);
//        initWebview(URL);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initAppBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("载入中..");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(mToolbar);//添加toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.wv_main);
        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
        mWebView.loadUrl(URL);
    }

    private void initWebSettings() {
        WebSettings settings = mWebView.getSettings();
        //支持获取手势焦点
        mWebView.requestFocusFromTouch();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //支持插件
        settings.setPluginState(WebSettings.PluginState.ON);
        //设置适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持缩放
        settings.setSupportZoom(false);
        //隐藏原生的缩放控件
        settings.setDisplayZoomControls(false);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.supportMultipleWindows();
        settings.setSupportMultipleWindows(true);
        //设置缓存模式
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(mWebView.getContext().getCacheDir().getAbsolutePath());

        //设置可访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持自动加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        settings.setNeedInitialFocus(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
    }

    //丞相
    private void initWebViewClient() {
        mWebView.setWebViewClient(new WebViewClient() {

            //页面开始加载时
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressbar.setVisibility(View.VISIBLE);
            }


            //页面完成加载时
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressbar.setVisibility(View.GONE);
            }

            //是否在WebView内加载新页面
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }

            //网络错误时回调的方法
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                /**
                 * 在这里写网络错误时的逻辑,比如显示一个错误页面
                 *
                 * 这里我偷个懒不写了
                 * */
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
    }

    //秘书
    private void initWebChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            private Bitmap mDefaultVideoPoster;//默认的视频展示图

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setToolbarTitle(title);
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                if (mDefaultVideoPoster == null) {
                    mDefaultVideoPoster = BitmapFactory.decodeResource(
                            getResources(), R.drawable.video_default
                    );
                    return mDefaultVideoPoster;
                }
                return super.getDefaultVideoPoster();
            }
        });
    }

    /**
     * 设置Toolbar标题
     *
     * @param title
     */
    private void setToolbarTitle(final String title) {
        Log.d("setToolbarTitle", " WebDetailActivity " + title);
        if (mToolbar != null) {
            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    mToolbar.setTitle(TextUtils.isEmpty(title) ? getString(R.string.loading) : title);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 有点占地方,暂时gone
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.page_up:
                Toast.makeText(getApplicationContext(), "页面向上", Toast.LENGTH_SHORT).show();
                mWebView.pageUp(true);
                break;
            case R.id.page_down:
                Toast.makeText(getApplicationContext(), "页面向下", Toast.LENGTH_SHORT).show();
                mWebView.pageDown(true);
                break;
            case R.id.refresh:
                Toast.makeText(getApplicationContext(), "刷新~", Toast.LENGTH_SHORT).show();
                mWebView.reload();
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    //监听物理返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //如果按下的是回退键且历史记录里确实还有页面
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "退出", Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event);
    }


    //后续处理 销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView == null) {
            return;
        }
        mWebView.stopLoading();
        ((ViewGroup) mWebView.getParent()).removeView(mWebView);
        mWebView.removeAllViews();
//        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.destroy();
    }

}
