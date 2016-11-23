# SmileNews
# 快速完成一个新闻APP

本Demo主要使用的技术：


- **看标题就知道了**
- **Material Design**
- **聚合数据**

-------------------

## 效果
直接点吧，先看下效果
![这里写图片描述](http://upload-images.jianshu.io/upload_images/1322432-3d6d75014ec7baec.gif?imageMogr2/auto-orient/strip)


## Demo架构
老司机们一看就知道界面是由ViewPager+Fragment组成，还是比较简单的。新闻详情页面主要是采用了design包下的CoordinatorLayout作为父布局，因为要做出那个下拉折叠效果嘛。然后点击新闻列表时会有一个转场动画，不知道细心的朋友们有木有看出来，上拉刷新是采用的官方的SwipeRefreshLayout
一切都追求原滋原味。
整个Demo的网络请求是通过RxJava+Retrofit来实现的，为什么用这对基友组合呢？
三个字 “太爽了”
OK，后面会有相关的介绍。


##代码分析

**封装BaseActivity**　
虽然整个Demo就两个Activity，那我们还是封装一下，因为我喜欢追求代码简洁（与后面可能会有些出入）额额，，
先上波代码吧

```
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
        initStatusBar();
    }

    protected abstract void initContentView(Bundle savedInstanceState);
    /**
     * 初始化沉浸式状态栏
     */
    private void initStatusBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }
    
    @SuppressWarnings("unchecked")
    public final <E extends View> E findView(int id){
        try {
            return (E) findViewById(id);
        }catch (ClassCastException e){
            throw  e;
        }
    }
}
```

其实也没什么亮点，就是封装下共同的方法，一个是沉浸式状态栏，一个是我为了偷懒，不想在findviewById的时候加个强制类型转换。当然也可以通过框架注入，和databinding来解决这个，但这次的重点不是他们。

**创建实体类**
	对了，这些数据都是从聚合数据获取下来的，具体细节就不多说了，就是申请个key，填写一些参数，然后它会返回一串json数据。我们就通过这些json数据去生成对应的实体类，用一个良心之作的工具 GsonFormat，具体操作可以自行Google或者百度。
　使用这个工具一是为了偷懒，二是为了配合gson来对json解析。

**创建配置文件**
项目中可能会遇到一些很多地方都会用到的常量，比如说聚合数据的key等等，我们可以创建一个接口
将这些数据写到这个接口里面，这样的话，哪里要用就直接继承这个接口就OK了。

```
public interface Config {
     String[] ARRYTITLES ={"头条","社会","科技","国内","国际","娱乐","时尚","军事","体育","财经"};
     String KEY_POSTION="key_postion";
     String[] ARRYTYPE={"top","shehui","keji","guonei","guoji","yule","shishang","junshi","tiyu","caijing"};
     String KEY_IMG_URL="imgurl";
     String KEY_CONTENT_URL="contenturl";
     String KEY_TYPE="type";
     String KEY_JUHE="0489bcea378ce792facda791d0f1e188";

}
```
因为请求不同的类型的新闻，参数不一样，所以弄个数组，把参数存入进去，记得与类型对应。

**主Activity编写**
这里因为创建项目的时候手贱了下，点了那个有侧滑的activity，所以一些生成了很多没什么卵用的代码（至少这个项目里面没什么用）

```
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,Config {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private List<String> mTitles=new ArrayList<>();

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findView(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTabLayout=findView(R.id.tab_layout);
        mViewPager=findView(R.id.viewpager);

        DrawerLayout drawer = findView(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findView(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initTitle();
        mAdapter=new ViewPagerAdapter(getSupportFragmentManager(),mTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    private void initTitle(){
        for (int i=0;i<ARRYTITLES.length;i++){
            mTitles.add(ARRYTITLES[i]);
        }

    }
 }
```
这里就是进行初始化一些view，将fragment添加进去，viewpager+tablayout基友组合。




**创建Fragment的适配器**
先说说适配器吧，因为这里要用的fragment比较多，所以继承FragmentStatePagerAdapter，Why？
###内存优化
因为一个Fragment占的内存还是比较大，一旦fragment数量比较多了，后果你懂的。当页面不可见时， 对应的Fragment实例可能会被销毁，但是Fragment的状态会被保存，所以一些提高了我们的app性能。

```
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> mTitles;


    public ViewPagerAdapter(FragmentManager fm, List<String> mTitles) {
        super(fm);
        this.mTitles = mTitles;

    }

    @Override
    public Fragment getItem(int position) {

        return ContentFragment.instance(position);
    }

    @Override
    public int getCount(){
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
```
还是比较简单的，可能最后一个方法或许有些陌生，用过TabLayout的朋友应该懂，就是设置Tab的标题，因为我们的ViewPager是要与TabLayout进行关联的。
 
**编写网络工具类**
好了重头戏来了，也是本项目唯一的特色，RxJava+Retrofit。
记得别忘了引入这些框架

```
	compile 'io.reactivex:rxjava:1.0.14'
    compile 'io.reactivex:rxandroid:1.0.1'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
    compile 'com.google.code.gson:gson:2.6.2'
    compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0'
```
这里我不做过多关于RxJava和Retrofit的描述，因为相关资料网上一堆堆。RxJava说到底就是异步，这是它整个流程非常简洁明了，而且方便线程切换。Retrofit是讲OKHttp更好的封装下，简化我们的网络请求。
首先编写Retrofit接口

```
public interface NewService {
    String BASE_URL="http://v.juhe.cn/";

    @GET("toutiao/index?")
    Observable<News> getNews(@QueryMap Map<String,String> map);
}
```
好像News 少了个s

接下来编写我们的请求工具类

```
 private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private NewService newService;

    private RetrofitUtil(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder()
                //配置Gson
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        retrofit=new Retrofit.Builder()
                .baseUrl(NewService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        newService=retrofit.create(NewService.class);

    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final RetrofitUtil INSTANCE = new RetrofitUtil();
    }

    public static RetrofitUtil getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void getNews(Subscriber<News> newsSubscriber,int type){
        newService.getNews(getParams(type))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsSubscriber);

    }

    private Map<String,String> getParams(int type){
        Map<String,String> map=new HashMap<>();
        map.put("type",ARRYTYPE[type]);
        map.put("key",KEY_JUHE);
        return map;
    }
}
```

我们在外面只用调用getNews就行了，传一个Subscriber和类型就行了。逻辑都不是很复杂吧。里面的精髓就是线程切换 .subscribeOn(Schedulers.io()) ，RxJava给我吗提供了五个选择，这里因为我们是请求网络，所以就用io的，最后切换到主线程 .observeOn(AndroidSchedulers.mainThread())

**编写Fragment**

既然网络工具类写好了，那么就写个fragment来把这些数据展示出来吧！

```
public class ContentFragment extends Fragment implements Config {

    private int mType;
    private List<News.ResultBean.DataBean> mData;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mShowNews;
    private NewsAdapter mAdapter;
    private int mSpacingInPixels;//
    private int mCount=0;


    public static Fragment instance(int postion){
        ContentFragment fragment=new ContentFragment();
        Bundle bundle = new Bundle() ;
        bundle.putInt(KEY_POSTION,postion);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_content,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments() ;
        Log.e("dandy","pos "+bundle.getInt(KEY_POSTION));
        initViews(view);
        mType=bundle.getInt(KEY_POSTION);

        getData();
    }

    private void initViews(View view) {
        mRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mShowNews= (RecyclerView) view.findViewById(R.id.news_recyclerview);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.tab_select_text_color,R.color.refresh_color,R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        mSpacingInPixels= getResources().getDimensionPixelSize(R.dimen.item_space);
        mShowNews.setHasFixedSize(true);
    }

    private void getData(){
        mRefreshLayout.setRefreshing(true);
        Subscriber<News> subscriber=new Subscriber<News>() {
            @Override
            public void onCompleted() {

            }
            //出现异常回调
            @Override
            public void onError(Throwable e) {
                Log.e("smile","获取失败");
            }
            //获取数据成功后回调
            @Override
            public void onNext(News news) {
                Log.e("smile","获取出来的"+news.getResult().getData().size());
                //mData=news.getResult().getData();
                setData(news);
            }
        };
        RetrofitUtil.getInstance().getNews(subscriber,mType);
    }

    private void setData(News data){

        mData=data.getResult().getData();
        mRefreshLayout.setRefreshing(false);
        mAdapter=new NewsAdapter(getContext(),data);
        mShowNews.setLayoutManager(new LinearLayoutManager(getContext()));
        //避免重复添加间距
        if (mCount==0){
            mShowNews.addItemDecoration(new SpacesItemDecoration(mSpacingInPixels));
        }

        mShowNews.setAdapter(mAdapter);
       // mAdapter.setOnScrollListener(mShowNews);
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                startDetailActivity(view,mData.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }) ;
        mCount++;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startDetailActivity(View view, News.ResultBean.DataBean bean){
        Intent intent=new Intent(getActivity(), NewsDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString(KEY_IMG_URL,bean.getThumbnail_pic_s());
        bundle.putString(KEY_CONTENT_URL,bean.getUrl());
        bundle.putString(KEY_TYPE,ARRYTITLES[mType]);
        intent.putExtras(bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),view.findViewById(R.id.item_news_img),"photos");
        getContext().startActivity( intent, options.toBundle());
    }
}
```

其实仔细一看也不是很复杂，就是调用我们开始写的getNews而已，请求成功后会回调onNext方法。对了这里有个转场动画，就是最后一个方法，这里的动画效果是共享元素，所以指定你要共享的元素就行，然后设置下它的   android:transitionName="photos"。


**编写RecyclerView适配器**
再见ListView，你好RecyclerView
RecyclerView的优点就不多说了，就是自由，任性
适配器，我就不贴代码了，累，而且没什么特色。

**编写详情页面**
这里主要都是用了Design里面的一些控件

```
<?xml version="1.0" encoding="utf-8"?>
<android.support.design.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">
    <android.support.design.widget.AppBarLayout
        android:id="@+id/app_bar_layout"
        android:layout_width="match_parent"
        android:layout_height="250dp">
        <android.support.design.widget.CollapsingToolbarLayout
            android:id="@+id/toolbar_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:minHeight="?attr/actionBarSize"
            android:fitsSystemWindows="true"
            app:contentScrim="?attr/colorPrimary"
            app:statusBarScrim="?attr/colorAccent"

            app:collapsedTitleGravity="left"
            app:expandedTitleGravity="center_horizontal|bottom"
            app:layout_scrollFlags="scroll|exitUntilCollapsed">
            <ImageView
                android:id="@+id/news_img"
                android:src="@mipmap/ic_launcher"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:scaleType="centerCrop"
                app:layout_collapseMode="parallax"
                android:transitionName="photos"
                app:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
                app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
                />
            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                app:layout_collapseMode="pin"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:navigationIcon="?attr/homeAsUpIndicator"
                app:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
                app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
                style="@style/ToolbarTheme"
                android:titleTextColor="@color/white"

               />
        </android.support.design.widget.CollapsingToolbarLayout>
    </android.support.design.widget.AppBarLayout>

    <com.dandy.smilenews.ui.MyNestedScrollView
        android:id="@+id/news_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">
        <WebView
            android:id="@+id/news_web"
            android:layout_width="match_parent"
            android:layout_height="match_parent"></WebView>
    </com.dandy.smilenews.ui.MyNestedScrollView>
</android.support.design.widget.CoordinatorLayout>
```
折叠式效果就是通过编写xml就能实现了，还是简单的描述下那些属性的含义
CollapsingToolbarLayout 

```
//折叠后的背景色  -> setContentScrim(Drawable)
  app:contentScrim="?attr/colorPrimary"   
  // 必须设置透明状态栏才有效  -> setStatusBarScrim(Drawable)     
  app:statusBarScrim="?attr/colorAccent"    
  // 标题  
  app:title="title"
  // 折叠后的标题位置
  app:collapsedTitleGravity="right"
  // 打开时的标题位置
  app:expandedTitleGravity="center_horizontal|bottom"
```
折叠效果

```
app:layout_collapseMode      
  有两个可选:
       parallax ——  视差模式,就是上面的图片的变化效果
       pin     —— 固定模式，在折叠的时候最后固定在顶端

  // 视差效果
  app:layout_collapseParallaxMultiplier   
  范围[0.0,1.0]，值越大视差越大
```
下面的MyNestedScrollView是我自定义的一个view，继承的NestedScrollView，主要是为了解决时间冲突导致滑动卡顿，就是不让拦截子view的触摸事件。

```
class MyNestedScrollView extends NestedScrollView {
    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;
    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }

}
```

到这里差不多也要完工了，就可以看到开头的效果了，貌似第一次写这么长的博客，，，
最后附上源码  [传送门](https://github.com/Smile52/SmileNews)
喜欢就给个星星吧！
