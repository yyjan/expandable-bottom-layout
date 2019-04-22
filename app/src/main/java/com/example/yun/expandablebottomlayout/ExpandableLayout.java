package com.example.yun.expandablebottomlayout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ExpandableLayout extends RelativeLayout {

    private static final int MORPH_ANIMATION_DURATION = 200;
    private static final int COlOR_ANIMATION_DURATION = 400;

    private float cornerRadius;
    private float marginLeft;
    private float marginRight;
    private float marginBottom;
    private int duration;
    private int bgColor;

    private GradientDrawable defaultBackground;

    public ExpandableLayoutType layoutType = ExpandableLayoutType.DEFAULT;

    public ExpandableLayout(Context context) {
        super(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources resources = getResources();

        // set default value
        int defBgColor = resources.getColor(R.color.colorAccent);
        float defCornerRadius = resources.getDimension(R.dimen.buy_button_corner_radius);
        float defMarginBottom = resources.getDimension(R.dimen.buy_button_margin_bottom);
        float defMarginLeft = resources.getDimension(R.dimen.buy_button_margin_left);
        float defMarginRight = resources.getDimension(R.dimen.buy_button_margin_right);

        // set attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandBottomButton);
        bgColor = typedArray.getColor(R.styleable.ExpandBottomButton_background_color, defBgColor);
        cornerRadius = typedArray.getDimension(R.styleable.ExpandBottomButton_corner_radius, defCornerRadius);
        marginLeft = typedArray.getDimension(R.styleable.ExpandBottomButton_margin_left, defMarginLeft);
        marginRight = typedArray.getDimension(R.styleable.ExpandBottomButton_margin_right, defMarginRight);
        marginBottom = typedArray.getDimension(R.styleable.ExpandBottomButton_margin_bottom, defMarginBottom);
        duration = MORPH_ANIMATION_DURATION;

        // set background drawable
        defaultBackground = createBackgroundDrawable(bgColor, cornerRadius);
        setBackground(defaultBackground);
    }


    private GradientDrawable createBackgroundDrawable(int color, float cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        return drawable;
    }

    public synchronized void morph(ExpandableLayoutType type) {

        // check progress
        if (isProgress()) return;
        setLayoutType(ExpandableLayoutType.PROGRESS);

        if (type == ExpandableLayoutType.EXPAND) {

            // expand animation
            ExpandableLayoutAnimation.Params animationParams = ExpandableLayoutAnimation.Params.create(this)
                    .cornerRadius(cornerRadius, 0)
                    .margin((int) marginBottom, 0)
                    .width(getWidth(), Utility.getScreenWidth(getContext()))
                    .duration(duration)
                    .listener(new ExpandableLayoutAnimation.Listener() {
                        @Override
                        public void onAnimationEnd() {
                            setLayoutType(ExpandableLayoutType.EXPAND);
                        }
                    });
            ExpandableLayoutAnimation animation = new ExpandableLayoutAnimation(animationParams);
            animation.startMorphAnimation();
        } else if (type == ExpandableLayoutType.DEFAULT) {

            // default animation
            ExpandableLayoutAnimation.Params animationParams = ExpandableLayoutAnimation.Params.create(this)
                    .cornerRadius(0, cornerRadius)
                    .margin(0, (int) marginBottom)
                    .width(getWidth(), getButtonWithMarginWidth())
                    .duration(duration)
                    .listener(new ExpandableLayoutAnimation.Listener() {
                        @Override
                        public void onAnimationEnd() {
                            setLayoutType(ExpandableLayoutType.DEFAULT);
                        }
                    });
            ExpandableLayoutAnimation animation = new ExpandableLayoutAnimation(animationParams);
            animation.startMorphAnimation();
        }
    }

    public void changeColor(int color) {

        // check color
        if (bgColor == color) return;

        // animation
        ExpandableLayoutAnimation.Params animationParams = ExpandableLayoutAnimation.Params.create(this)
                .color(bgColor, color)
                .duration(COlOR_ANIMATION_DURATION);
        ExpandableLayoutAnimation animation = new ExpandableLayoutAnimation(animationParams);
        animation.startColorAnimation();
    }

    public int getButtonWithMarginWidth() {
        return (int) (Utility.getScreenWidth(getContext()) - (marginLeft + marginRight));
    }

    public GradientDrawable getBackgroundDrawable() {
        return defaultBackground;
    }

    private boolean isProgress() {
        return layoutType.equals(ExpandableLayoutType.PROGRESS);
    }

    private void setLayoutType(ExpandableLayoutType type) {
        layoutType = type;
    }

}
