package com.example.myapplication.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.myapplication.R;

/**
 * create by lindanrong
 * 实现功能 类似在聊天记录后面跟随日期 根据前面的TextView 然后测量大小将自动计算后面的控件位置进行显示。
 *
 * 这里需要注意的是，目前第二个控件是比第一个控件小且底部对齐。
 * 如果后面的字体比前面的大，那么请自己修改一下逻辑。（自己求底部距离并且将）
 *
 */
public class TwoFlowCustomView extends FrameLayout {

    private int mInterval = 0;//两个布局中间的间隔
    private static final String TAG = "TwoFlowCustomView";
    public TwoFlowCustomView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public TwoFlowCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    /**
     * 初始化部分东西
     */
    public void initView(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TwoFlowCustomView);
            mInterval = typedArray.getInteger(R.styleable.TwoFlowCustomView_interval, 0);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        TextView textView = (TextView) getChildAt(0);
        int countSize = textView.getLineCount();
        if (countSize > 0) {
            //获取最后一行的宽度
            float aLastLineWidth = textView.getLayout().getLineWidth(countSize - 1);
            //获取第二个view的宽度
            View twoView = getChildAt(1);
            int twoViewWith = twoView.getMeasuredWidth();
            int twoViewHeight = twoView.getMeasuredHeight();
            //获取本TwoFlowCustomView的宽高
            int height = getMeasuredHeight();
            int width = getMeasuredWidth();

            //计算一下这个本布局的大小 然后再计算一下如果布局超过
            // 第一个控件最后一行的宽度，第二个控件的宽度 大布局中的左右padding 两个控件的间隔
            if (aLastLineWidth + twoViewWith + getPaddingLeft() + getPaddingRight() + mInterval > this.getMeasuredWidth()) {
                //这种情况要增加本ViewGroup的高度 宽度的不用了 xml中指定为match_parent
                int newHeight = height + twoViewHeight;
                setMeasuredDimension(width, newHeight);
            } else {
                //跟在最后一行后面展示控件 这种情况不需要再增加布局的高度了
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        TextView textView = (TextView) getChildAt(0);
        int countSize = textView.getLineCount();
        if (countSize > 0) {
            //获取最后一行的宽度
            float aLastLineWidth = textView.getLayout().getLineWidth(countSize - 1);
//            //textView的高度
            int textViewHeight =  textView.getLayout().getHeight();
            //获取第二个view的宽高
            View twoView = getChildAt(1);
            int twoViewWith = twoView.getWidth();
            int twoViewHeight = twoView.getHeight();
            //计算一下这个本布局的大小 然后再计算一下如果布局超过
            // 第一个控件最后一行的宽度，第二个控件的宽度 大布局中的左右padding 两个控件的间隔
            if (aLastLineWidth + twoViewWith + getPaddingLeft() + getPaddingRight() + mInterval > this.getWidth()) {
                //另起一行来写后面的展示控件
                twoView.setLeftTopRightBottom(0, textViewHeight,twoViewWith, textViewHeight+twoViewHeight);
            } else {
                //跟在最后一行后面展示控件
                twoView.setLeftTopRightBottom((int) aLastLineWidth + mInterval, textViewHeight-twoViewHeight,(int) aLastLineWidth + mInterval+twoViewWith, textViewHeight);
            }
        }
    }
}
