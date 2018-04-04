package com.baisi.spedometer.view.chatview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.baisi.spedometer.R;
import com.baisi.spedometer.utiles.DensityUtil;

/**
 * @author wj
 * @date 2018/1/20
 * api用法：{@link{setStepNum()设置目标步数和当前步数，刻度线的个数必须为360的因数}}
 */
public class CircleScaleView extends View {

    private Context mContext;
    private float mViewWidth;
    private float mViewHeight;
    private float mCircleRadius;
    private int mScaleLineLength;
    private int mLineNum = 36;
    private int mGoalStep = 16000;
    private int mCurrentStep = 14628;
    private int mRedLineNum;
    private int mLastRedLineNum;
    private String mGoalText;
    private String mSteps;

    private Paint mPaint;
    private TextPaint mTextPaint;
    private int bottomColorWhite;
    private int upColorRed;
    private int goalColor;

    private PathEffect mEffect;
    private float mDashWidth;

    public CircleScaleView(Context context) {
        this(context, null);
    }

    public CircleScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mGoalText = context.getResources().getString(R.string.goal_with_value);
        mSteps = context.getResources().getString(R.string.steps);
        bottomColorWhite = ContextCompat.getColor(context, R.color.bottomColorWhite);
        upColorRed = ContextCompat.getColor(context, R.color.new_main_pause);
        goalColor = ContextCompat.getColor(context, R.color.goalColor);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dip2px(mContext, 2));
        mDashWidth = dip2px(mContext, 2);
        mEffect = new DashPathEffect(new float[]{mDashWidth, mDashWidth * 3, mDashWidth, mDashWidth * 3}, 1);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        mCircleRadius = (float) (mViewWidth * 0.35);
        mScaleLineLength = (int) (mViewWidth * 0.06);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw bottom circle
        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        mPaint.setColor(bottomColorWhite);
        mPaint.setPathEffect(mEffect);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStrokeWidth(dip2px(mContext,  2.5f));
        canvas.drawCircle(0, 0, mCircleRadius, mPaint);
        // draw text
        mTextPaint.setTextSize(mCircleRadius / 6);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mTextPaint.setColor(goalColor);
        String mStepGoalText =  String.format(mGoalText, mGoalStep);
        canvas.drawText(mStepGoalText, 0, -mCircleRadius / 2, mTextPaint);
        mTextPaint.setTextSize((float) (mCircleRadius / 2.3));
        mTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        mTextPaint.setColor(bottomColorWhite);
        canvas.drawText(mCurrentStep + "", 0, dip2px(mContext,  10), mTextPaint);
        mTextPaint.setTextSize(mCircleRadius / 5);
        canvas.drawText(mSteps, dip2px(mContext,  10), mCircleRadius / 2, mTextPaint);
        // draw bitmap
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_stat_step),
                dip2px(mContext,  5) - mCircleRadius / 2,
                mCircleRadius / 2 - dip2px(mContext,  20), mPaint);
        // draw scale line
        mPaint.setPathEffect(null);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(dip2px(mContext,  4));
        mPaint.setColor(bottomColorWhite);
        for (int i = 0; i < mLineNum; i++) {
            canvas.drawLine(0, -(mCircleRadius + mScaleLineLength + dip2px(mContext,  10)),
                    0, -(mCircleRadius + dip2px(mContext,  10)), mPaint);
            canvas.rotate(360 / mLineNum);
        }
        // draw red line
        mPaint.setColor(upColorRed);
        for (int i = 0; i < mRedLineNum; i++) {
            canvas.drawLine(0, -(mCircleRadius + mScaleLineLength + dip2px(mContext,  10)),
                    0, -(mCircleRadius + dip2px(mContext,  10)), mPaint);
            canvas.rotate(360 / mLineNum);
        }
    }

    public void setStepNum(int goalStep, int currentStep) {
        mGoalStep = goalStep;
        mCurrentStep = currentStep;
        final int redLineNum = mLineNum * currentStep / goalStep;
        ValueAnimator animator = ValueAnimator.ofInt(mLastRedLineNum, redLineNum);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if ((int) (animation.getAnimatedValue()) != mRedLineNum ||
                        (int) (animation.getAnimatedValue()) == redLineNum) {
                    mRedLineNum = (int) animation.getAnimatedValue();
                    invalidate();
                }
            }
        });
        animator.setDuration(Math.abs((redLineNum - mLastRedLineNum) * 100));
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        mLastRedLineNum = redLineNum;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
