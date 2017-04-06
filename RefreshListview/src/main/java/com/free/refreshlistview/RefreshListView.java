package com.free.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    //下拉
    private static final int RELEASE_TO_REFRESH = 0;

    //下拉返回
    private static final int PULL_TO_REFRESH = 1;

    //正在刷新
    private static final int REFRESHING = 2;

    private static final int DONE = 3;

    private static final int LOADING = 4;


    //实际的padding的距离与界面上偏移距离的比例
    private static final int RATIO = 3;

    private LayoutInflater inflater;

    //头部
    private LinearLayout headView;

    private TextView lvHeaderTipsTv;

    private TextView lvHeaderLastUpdatedTv;

    private ImageView lvHeaderArrowIv;

    private ProgressBar lvHeaderProgressBar;


    //头部下拉刷新的布局的高度
    private int headerContentHeight;

    //动画
    private RotateAnimation animation;

    private RotateAnimation reverseAnimation;

    //当前状态标记
    private int state;

    //是否正在刷新
    private boolean isRefreshable;


    private int startY;
    private boolean isBack;

    //保证startY的值在一个完整的touch实践中只记录一次
    private boolean isRecored;


    //回调接口
    private OnRefreshListener refreshListener;



    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setCacheColorHint(context.getResources().getColor(R.color.transparent));
        inflater = LayoutInflater.from(context);

        headView = (LinearLayout) inflater.inflate(R.layout.refresh_listview_header, null);

        lvHeaderTipsTv = (TextView) headView.findViewById(R.id.lvHeaderTipsTv);

        lvHeaderLastUpdatedTv = (TextView) headView.findViewById(R.id.lvHeaderLastUpdatedTv);

        lvHeaderArrowIv = (ImageView) headView.findViewById(R.id.lvHeaderArrowIv);

        lvHeaderArrowIv.setMinimumWidth(70);
        lvHeaderArrowIv.setMinimumHeight(50);

        lvHeaderProgressBar = (ProgressBar) headView.findViewById(R.id.lvHeaderProgressBar);

        measureView(headView);
        headerContentHeight = headView.getMeasuredHeight();

        //设置内边距，把头部隐藏
        headView.setPadding(0, -1 * headerContentHeight, 0, 0);

        //重新绘制
        headView.invalidate();

        //加入到ListView的顶部
        addHeaderView(headView);

        //设置滚动监听
        setOnScrollListener(this);

        //设置旋转动画事件
        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        //初始设置为刷新完毕状态
        state = DONE;
        isRefreshable = false;

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (0 == firstVisibleItem) {
            isRefreshable = true;
        } else {
            isRefreshable = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (isRefreshable) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (!isRecored) {
                    isRecored = true;
                    startY = (int) ev.getY();
                }
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                if (state != REFRESHING && state != LOADING) {
                    if (state == PULL_TO_REFRESH) {
                        state = DONE;
                        changeHeaderViewByState();

                    }

                    if (state == RELEASE_TO_REFRESH) {
                        state = REFRESHING;
                        changeHeaderViewByState();
                        onLvRefresh();
                    }
                }
                isRecored = false;
                isBack = false;

            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                int tempY = (int) ev.getY();
                if (!isRecored) {
                    isRecored = true;
                    startY = tempY;
                }

                if (state != REFRESHING && isRecored && state != LOADING) {

                    if (state == RELEASE_TO_REFRESH) {
                        setSelection(0);
                        if ((tempY - startY) / RATIO < headerContentHeight
                                && (tempY - startY) > 0) {
                            state = PULL_TO_REFRESH;
                            changeHeaderViewByState();

                        } else if (tempY - startY <= 0) {
                            state = DONE;
                            changeHeaderViewByState();
                        }
                    }

                }

                if (state == PULL_TO_REFRESH) { //当前是下拉刷新，
                    setSelection(0);

                    //下拉到松开刷新的状态
                    if ((tempY - startY) / RATIO >= headerContentHeight) {
                        state = RELEASE_TO_REFRESH;
                        isBack = true;
                        changeHeaderViewByState();

                    } else if (tempY - startY <= 0) {
                        state = DONE;
                        changeHeaderViewByState();
                    }
                }

                if (state == DONE) {
                    if (tempY - startY > 0) {
                        state = PULL_TO_REFRESH;
                        changeHeaderViewByState();
                    }
                }

                //更新headView的size
                if (state == PULL_TO_REFRESH) {
                    headView.setPadding(0, -1 * headerContentHeight + (tempY - startY) / RATIO, 0, 0);
                }

                if (state == RELEASE_TO_REFRESH) {
                    headView.setPadding(0, (tempY - startY) / RATIO - headerContentHeight, 0, 0);
                }
            }
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 状态改变时调用，更新界面
     */
    private void changeHeaderViewByState() {

        if (state == RELEASE_TO_REFRESH) {
            lvHeaderArrowIv.setVisibility(View.VISIBLE);
            lvHeaderProgressBar.setVisibility(View.GONE);
            lvHeaderTipsTv.setVisibility(View.VISIBLE);
            lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);

            lvHeaderArrowIv.clearAnimation();
            lvHeaderArrowIv.startAnimation(animation);

            lvHeaderTipsTv.setText("松开刷新");

        } else if (state == PULL_TO_REFRESH) {
            lvHeaderProgressBar.setVisibility(View.GONE);
            lvHeaderTipsTv.setVisibility(View.VISIBLE);
            lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
            lvHeaderArrowIv.clearAnimation();
            lvHeaderArrowIv.setVisibility(View.VISIBLE);
            // 是由RELEASE_To_REFRESH状态转变来的
            if (isBack) {
                isBack = false;
                lvHeaderArrowIv.clearAnimation();
                lvHeaderArrowIv.startAnimation(reverseAnimation);

                lvHeaderTipsTv.setText("下拉刷新");
            } else {
                lvHeaderTipsTv.setText("下拉刷新");
            }

        } else if (state == REFRESHING) {
            headView.setPadding(0, 0, 0, 0);

            lvHeaderProgressBar.setVisibility(View.VISIBLE);
            lvHeaderArrowIv.clearAnimation();
            lvHeaderArrowIv.setVisibility(View.GONE);
            lvHeaderTipsTv.setText("正在刷新...");
            lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);

        } else if (state == DONE) {
            headView.setPadding(0, -1 * headerContentHeight, 0, 0);

            lvHeaderProgressBar.setVisibility(View.GONE);
            lvHeaderArrowIv.clearAnimation();
            lvHeaderArrowIv.setImageResource(R.drawable.arrow);
            lvHeaderTipsTv.setText("下拉刷新");
            lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 估算view的大小
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, params.width);
        int lpHeight = params.height;

        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 设置回调监听
     * @param refreshListener
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }
    /**
     * 刷新回调接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void onRefreshComplete() {
        state = DONE;
        lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
        changeHeaderViewByState();
    }

    private void onLvRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
        super.setAdapter(adapter);
    }
 }