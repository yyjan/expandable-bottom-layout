package com.example.yun.expandablebottomlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;

class ExpandableLayoutAnimation {

    private Params params;

    ExpandableLayoutAnimation(@NonNull Params params) {
        this.params = params;
    }

    static class Params {
        private float fromCornerRadius;
        private float toCornerRadius;

        private int fromWidth;
        private int toWidth;

        private int fromMargin;
        private int toMargin;

        private int fromColor;
        private int toColor;

        private int duration;

        private ExpandableLayout button;

        private Listener animationListener;

        private Params(@NonNull ExpandableLayout button) {
            this.button = button;
        }

        static Params create(@NonNull ExpandableLayout button) {
            return new Params(button);
        }

        Params duration(int duration) {
            this.duration = duration;
            return this;
        }

        Params listener(@NonNull ExpandableLayoutAnimation.Listener animationListener) {
            this.animationListener = animationListener;
            return this;
        }

        Params color(int fromColor, int toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
            return this;
        }

        Params cornerRadius(float fromCornerRadius, float toCornerRadius) {
            this.fromCornerRadius = fromCornerRadius;
            this.toCornerRadius = toCornerRadius;
            return this;
        }

        Params width(int fromWidth, int toWidth) {
            this.fromWidth = fromWidth;
            this.toWidth = toWidth;
            return this;
        }

        Params margin(int fromMargin, int toMargin) {
            this.fromMargin = fromMargin;
            this.toMargin = toMargin;
            return this;
        }
    }

    public interface Listener {
        void onAnimationEnd();
    }

    /**
     * Corner Radius Change Animator
     *
     * @return animator
     */
    private Animator cornerAnimator() {
        Drawable background = params.button.getBackgroundDrawable();
        ObjectAnimator cornerAnimation =
                ObjectAnimator.ofFloat(background, "cornerRadius", params.fromCornerRadius, params.toCornerRadius);
        return cornerAnimation;
    }

    /**
     * Color Change Animator
     *
     * @return animator
     */
    private Animator colorAnimator() {
        Drawable background = params.button.getBackgroundDrawable();
        ObjectAnimator colorAnimation = ObjectAnimator.ofInt(background, "color", params.fromColor, params.toColor);
        colorAnimation.setEvaluator(new ArgbEvaluator());
        colorAnimation.setDuration(params.duration);

        return colorAnimation;
    }

    /**
     * Width Change Animator
     *
     * @return animator
     */
    private Animator widthAnimator() {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(params.fromWidth, params.toWidth);
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = params.button.getLayoutParams();
                layoutParams.width = val;
                params.button.setLayoutParams(layoutParams);
            }
        });
        return widthAnimation;
    }

    /**
     * Margins Change Animator
     *
     * @return animator
     */
    private Animator moveAnimation() {
        ValueAnimator moveAnimation = ValueAnimator.ofInt(params.fromMargin, params.toMargin);
        moveAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                try {
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) params.button.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, (Integer) valueAnimator.getAnimatedValue());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        return moveAnimation;
    }

    /**
     * Start Expand Animation
     */
    void startMorphAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(params.duration);
        animatorSet.playTogether(cornerAnimator(), widthAnimator(), moveAnimation());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (params.animationListener != null) {
                    params.animationListener.onAnimationEnd();
                }
            }
        });
        animatorSet.start();
    }

    /**
     * Start Color Animation
     */
    void startColorAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(params.duration);
        animatorSet.play(colorAnimator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (params.animationListener != null) {
                    params.animationListener.onAnimationEnd();
                }
            }
        });
        animatorSet.start();
    }

}
