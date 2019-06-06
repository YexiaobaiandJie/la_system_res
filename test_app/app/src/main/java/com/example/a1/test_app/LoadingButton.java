package com.example.a1.test_app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;

public class LoadingButton extends android.support.v7.widget.AppCompatButton {
    private Context context;

    // 开始Loading时的回调
    private OnStartListener startListener;

    // 结束Loading时的回调
    private OnFinishListener finishListener;

    // 开始和结束Loading时的回调
    private OnLoadingListener listener;

    // Loading动画旋转周期
    private int rotateDuration = 1000;

    // 按钮缩成Loading动画的时间
    private int reduceDuration = 350;

    // Loading旋转动画控制器
    private Interpolator rotateInterpolator;

    // 按钮缩成Loading动画的控制器
    private Interpolator reduceInterpolator;

    private int width;
    private int height;

    private String text;

    // 是否在Loading中
    private boolean isLoading = false;

    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (width == 0) width = getMeasuredWidth();
        if (height == 0) height = getMeasuredHeight();
    }

    /**
     * 播放按钮缩成Loading的动画
     */
    private void showStartLoadAnimation() {
        ValueAnimator animator = new ValueAnimator().ofInt(width, height);
        animator.setDuration(reduceDuration);
        if (reduceInterpolator != null) animator.setInterpolator(reduceInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().width = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_main_color_selector));
                setEnabled(false);
                text = getText().toString();
                setText("");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showLoadingAnimation();
            }
        });
        animator.start();
    }

    /**
     * 播放Loading动画
     */
    private void showLoadingAnimation() {
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(rotateDuration);
        animation.setInterpolator(rotateInterpolator != null ? rotateInterpolator : new LinearInterpolator());
        animation.setRepeatCount(-1);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_loading));
        if (startListener != null) {
            startListener.onStart();
        } else if (listener != null) {
            listener.onStart();
        }
        startAnimation(animation);
        isLoading = true;
    }

    /**
     * 播放Loading拉伸成按钮的动画
     */
    private void showFinishLoadAnimation() {
        ValueAnimator animator = new ValueAnimator().ofInt(height, width);
        animator.setDuration(reduceDuration);
        if (reduceInterpolator != null) animator.setInterpolator(reduceInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().width = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_main_color_selector));
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setText(text);
                setEnabled(true);
                if (finishListener != null) {
                    finishListener.onFinish();
                } else if (listener != null) {
                    listener.onFinish();
                }
            }
        });
        animator.start();
        isLoading = false;
    }

    /**
     * 开始Loading
     */
    public void startLoading() {
        if (!isLoading) {
            clearAnimation();
            showStartLoadAnimation();
        }
    }

    /**
     * 开始Loading
     *
     * @param listener Loading开始时的回调
     */
    public void startLoading(OnStartListener listener) {
        if (!isLoading) {
            this.startListener = listener;
            clearAnimation();
            showStartLoadAnimation();
        }
    }

    /**
     * 结束Loading
     */
    public void finishLoading() {
        if (isLoading) {
            clearAnimation();
            showFinishLoadAnimation();
        }
    }

    /**
     * 结束Loading
     *
     * @param listener Loading结束时的回调
     */
    public void finishLoading(OnFinishListener listener) {
        if (isLoading) {
            this.finishListener = listener;
            clearAnimation();
            showFinishLoadAnimation();
        }
    }

    /**
     * 设置Loading开始和结束时的回调接口
     *
     * @param listener
     */
    public void setOnLoadingListener(OnLoadingListener listener) {
        this.listener = listener;
    }

    /**
     * 设置按钮缩成Loading动画的时间
     *
     * @param reduceDuration 时间，单位毫秒
     */
    public void setReduceDuration(int reduceDuration) {
        this.reduceDuration = reduceDuration;
    }

    /**
     * 设置Loading动画旋转周期
     *
     * @param rotateDuration 旋转周期，单位毫秒
     */
    public void setRotateDuration(int rotateDuration) {
        this.rotateDuration = rotateDuration;
    }

    /**
     * 获取是否正在Loading
     *
     * @return
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * 设置Loading旋转动画控制器
     *
     * @param rotateInterpolator
     */
    public void setRotateInterpolator(Interpolator rotateInterpolator) {
        this.rotateInterpolator = rotateInterpolator;
    }

    /**
     * 按钮缩成Loading动画的控制器
     *
     * @param reduceInterpolator
     */
    public void setReduceInterpolator(Interpolator reduceInterpolator) {
        this.reduceInterpolator = reduceInterpolator;
    }

    /**
     * Loading开始时的回调接口
     */
    public interface OnStartListener {
        void onStart();
    }

    /**
     * Loading结束时的回调接口
     */
    public interface OnFinishListener {
        void onFinish();
    }

    /**
     * Loading开始和结束时的回调接口
     */
    public interface OnLoadingListener {
        void onStart();

        void onFinish();
    }
}
