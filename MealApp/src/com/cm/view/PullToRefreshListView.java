package com.cm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.cm.mealapp.activity.R;
import com.cm.utils.BaseUtil;

public class PullToRefreshListView extends ListView implements OnScrollListener {

	private final static int PULL_To_REFRESH = 0;
	private final static int RELEASE_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DEFAULT = 3;
	private int curState;
	private Scroller mScroller;
	private final static int SCROLL_DURATION = 400;
	private int viewHeight = 0;
	private LayoutInflater inflater;
	private LinearLayout headView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private LinearLayout footerView;
	private ProgressBar listview_foot_progress;
	private TextView listview_foot_more;
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private boolean isRecored;
	private int headContentWidth;
	private int headContentHeight;
	private int headContentOriginalTopPadding;
	private int startY;
	private int firstItemIndex;
	private int currentScrollState;
	private boolean isBack;
	private OnRefreshListener refreshListener;
	private OnLoadMoreListener loadMoreListener;
	private boolean isAutoLoadOnBottom = true;
	private Mode mCurrentMode = Mode.BOTH;
	private boolean hasMore = true;
	private boolean isLoading = false;

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);

	}

	private void init(Context context) {
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(100);
		animation.setFillAfter(true);
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(100);
		reverseAnimation.setFillAfter(true);
		findview(context);
		mScroller = new Scroller(context);
		headContentOriginalTopPadding = headView.getPaddingTop();

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight,
				headView.getPaddingRight(), headView.getPaddingBottom());

		headView.invalidate();
		addHeaderView(headView);
		setOnScrollListener(this);
	}

	private void findview(Context context) {
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(
				R.layout.pull_to_refresh_header, null);
		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(50);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);
		footerView = (LinearLayout) inflater.inflate(
				R.layout.pull_to_refresh_footer, null);
		footerView.setDrawingCacheBackgroundColor(0);
		footerView.setVisibility(View.GONE);
		listview_foot_progress = (ProgressBar) footerView
				.findViewById(R.id.listview_foot_progress);
		listview_foot_more = (TextView) footerView
				.findViewById(R.id.listview_foot_more);
		listview_foot_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isLoading && hasMore) {
					onLoadMore();
				}
			}
		});
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset() && curState != DEFAULT) {
			headView.setPadding(headView.getPaddingLeft(),
					mScroller.getCurrY(), headView.getPaddingRight(),
					headView.getPaddingBottom());
			postInvalidate();
		}

		super.computeScroll();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisiableItem,
			int visibleItemCount, int totalItemCount) {
		firstItemIndex = firstVisiableItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		currentScrollState = scrollState;
		if (currentScrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& this.getLastVisiblePosition() == this.getCount() - 1) {
			if (!isLoading && isPermitLoadMore() && hasMore) {
				if (footerView.getVisibility() == View.GONE) {
					addFooterView(footerView);
					footerView.setVisibility(View.VISIBLE);
				}
				if (isAutoLoadOnBottom) {
					onLoadMore();
				} else {
					listview_foot_progress.setVisibility(View.GONE);
					listview_foot_more
							.setText(R.string.pull_to_refresh_click_label);// 点击加载更多
				}

			}
		}

	}

	public void setAutoLoadOnBottom(boolean isAutoLoadOnBottom) {
		this.isAutoLoadOnBottom = isAutoLoadOnBottom;
	}

	public Mode getCurrentMode() {
		return mCurrentMode;
	}

	public void setMode(Mode mCurrentMode) {
		this.mCurrentMode = mCurrentMode;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
		if (!hasMore) {
			if (footerView.getVisibility() == View.GONE) {
				addFooterView(footerView);
				footerView.setVisibility(View.VISIBLE);
			}
			listview_foot_more
					.setText(R.string.pull_to_refresh_not_moredata_label);
			listview_foot_progress.setVisibility(View.GONE);
		} else {
			listview_foot_more.setText(R.string.pull_to_refresh_more_label);
		}
	}

	public boolean isPermitLoadMore() {
		return mCurrentMode == Mode.BOTH || mCurrentMode == Mode.PULL_FROM_END;
	}

	public boolean isPermitPullRefresh() {
		return mCurrentMode == Mode.BOTH
				|| mCurrentMode == Mode.PULL_FROM_START;
	}

	public boolean IsRefreshing() {
		return curState == REFRESHING;
	}

	public boolean IsLoading() {
		return isLoading;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!isPermitPullRefresh()) {
				return super.onTouchEvent(event);
			}
			if (firstItemIndex == 0 && !isRecored) {
				startY = (int) event.getY();
				isRecored = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (!isPermitPullRefresh()) {
				return super.onTouchEvent(event);
			}
			if (curState != REFRESHING) {
				if (curState == PULL_To_REFRESH) {
					curState = DEFAULT;
					resetHeaderView();
				} else if (curState == RELEASE_To_REFRESH) {
					curState = REFRESHING;
					resetHeaderView();
					onRefresh();
				}
			}

			isRecored = false;
			isBack = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isPermitPullRefresh()) {
				return super.onTouchEvent(event);
			}
			int tempY = (int) event.getY();
			viewHeight = tempY - startY;
			if (!isRecored && firstItemIndex == 0) {
				isRecored = true;
				startY = tempY;
			}
			if (curState != REFRESHING && isRecored) {
				if (curState == RELEASE_To_REFRESH) {
					if ((tempY - startY < headContentHeight + 20)
							&& (tempY - startY) > 0) {
						curState = PULL_To_REFRESH;
						resetHeaderView();
					} else if (tempY - startY <= 0) {
						curState = DEFAULT;
						resetHeaderView();
					}
				} else if (curState == PULL_To_REFRESH) {
					if (tempY - startY >= headContentHeight + 20
							&& currentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
						curState = RELEASE_To_REFRESH;
						isBack = true;
						resetHeaderView();
					} else if (tempY - startY <= 0) {
						curState = DEFAULT;
						resetHeaderView();
					}
				} else if (curState == DEFAULT) {
					if (tempY - startY > 0) {
						curState = PULL_To_REFRESH;
						resetHeaderView();
					}
				}
				if (curState == PULL_To_REFRESH) {
					int topPadding = ((-1 * headContentHeight + (tempY - startY)));
					headView.setPadding(headView.getPaddingLeft(), topPadding,
							headView.getPaddingRight(),
							headView.getPaddingBottom());
					headView.invalidate();
				}
				if (curState == RELEASE_To_REFRESH) {
					int topPadding = ((tempY - startY - headContentHeight));
					headView.setPadding(headView.getPaddingLeft(), topPadding,
							headView.getPaddingRight(),
							headView.getPaddingBottom());
					headView.invalidate();
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private void resetHeaderView() {
		BaseUtil.LogII("resetHeaderView curState=" + curState);
		switch (curState) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextview.setText(R.string.pull_to_refresh_release_label);
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
			}
			tipsTextview.setText(R.string.pull_to_refresh_pull_label);
			break;

		case REFRESHING:
			scrollToTop();
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText(R.string.pull_to_refresh_refreshing_label);
			lastUpdatedTextView.setVisibility(View.GONE);
			break;
		case DEFAULT:
			headView.setPadding(headView.getPaddingLeft(), -1
					* headContentHeight, headView.getPaddingRight(),
					headView.getPaddingBottom());
			headView.invalidate();
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();

			tipsTextview.setText(R.string.pull_to_refresh_pull_label);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void scrollToTop() {
		mScroller.startScroll(0, viewHeight, 0, -viewHeight, SCROLL_DURATION);
		PullToRefreshListView.this.invalidate();

	}

	private void resetFooterView() {
		if (!isLoading) {
			listview_foot_progress.setVisibility(View.GONE);
			if (!hasMore) {
				listview_foot_more
						.setText(R.string.pull_to_refresh_not_moredata_label);
			} else {
				if (isAutoLoadOnBottom) {
					listview_foot_more
							.setText(R.string.pull_to_refresh_more_label);
				} else {
					listview_foot_more
							.setText(R.string.pull_to_refresh_click_label);
				}
			}
		} else {
			if (footerView.getVisibility() == View.GONE) {
				addFooterView(footerView);
				footerView.setVisibility(View.VISIBLE);
			}
			listview_foot_progress.setVisibility(View.VISIBLE);
			listview_foot_more.setText(R.string.pull_to_refresh_loading_label);
		}
	}

	public void clickRefresh() {
		setSelection(0);
		curState = REFRESHING;
		resetHeaderView();
		onRefresh();
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}

	public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
	}

	public void onRefreshComplete(String update) {
		lastUpdatedTextView.setText(update);
		onRefreshComplete();
	}

	public void onRefreshComplete() {
		if (curState == REFRESHING) {
			curState = DEFAULT;
			resetHeaderView();
			hasMore = true;
			resetFooterView();
		}

	}

	public void onLoadMoreComplete() {
		if (isLoading) {
			isLoading = false;
			resetFooterView();
		}

	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	public void setIsLoading(boolean isLoading) {
		this.isLoading = isLoading;
		resetFooterView();

	}

	public void onLoadMore() {
		if (loadMoreListener != null) {
			isLoading = true;
			if (footerView.getVisibility() == View.GONE) {
				addFooterView(footerView);
				footerView.setVisibility(View.VISIBLE);
			}
			listview_foot_progress.setVisibility(View.VISIBLE);
			listview_foot_more.setText(R.string.pull_to_refresh_loading_label);
			loadMoreListener.onLoadMore();
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public enum Mode {
		DISABLED, PULL_FROM_START, PULL_FROM_END, BOTH;
	}

}
