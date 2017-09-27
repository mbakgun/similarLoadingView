package similar.io.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class SimilarLoadingView extends View {


    private static final int MAX_PROGRESS_VALUE = 1450;
    private static final int PROGRESS_TIME = 2000;
    private static final int MAX_ALPHA = 70;

    private Paint paint = new Paint();
    private double hexHeight;
    private double hexWidth;
    private double hexPadding = 0;
    private float actualProgress = 0;
    private int maxAlpha = MAX_ALPHA;
    @IdRes
    private int loadingDrawable;
    private int animationTime = PROGRESS_TIME;
    private int color;
    private int cornerRadius;

    private AnimatorSet indeterminateAnimator;

    public SimilarLoadingView(Context context) {
        super(context);
    }

    public SimilarLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimilarLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttributes(attrs, defStyle);
        initPaint();
    }

    private void initAttributes(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.SimilarLoadingView,
                defStyle, 0);
        animationTime = a.getInteger(R.styleable.SimilarLoadingView_similar_animDuration, PROGRESS_TIME);
        maxAlpha = a.getInteger(R.styleable.SimilarLoadingView_similar_maxAlpha, MAX_ALPHA);
        color = a.getColor(R.styleable.SimilarLoadingView_similar_color, Color.BLACK);
        cornerRadius = a.getInteger(R.styleable.SimilarLoadingView_similar_cornerRadius, 0);
        loadingDrawable = a.getResourceId(R.styleable.SimilarLoadingView_similar_loadingDrawable, -1);
        a.recycle();
    }

    private void initPaint() {
        paint.setAlpha(0);
        paint.setPathEffect(new CornerPathEffect(cornerRadius));
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        int currentVisibility = getVisibility();
        super.setVisibility(visibility);
        if (visibility != currentVisibility) {
            if (visibility == View.VISIBLE) {
                resetAnimator();
            } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
                stopAnimation();
            }
        }
    }

    private void startAnimation() {
        resetAnimator();
    }

    private void stopAnimation() {
        actualProgress = 0;
        if (indeterminateAnimator != null) {
            indeterminateAnimator.cancel();
            indeterminateAnimator = null;
        }
    }

    private void resetAnimator() {
        if (indeterminateAnimator != null && indeterminateAnimator.isRunning()) {
            indeterminateAnimator.cancel();
        }
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(0, MAX_PROGRESS_VALUE);
        progressAnimator.setDuration(animationTime);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                actualProgress = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        indeterminateAnimator = new AnimatorSet();
        indeterminateAnimator.play(progressAnimator);
        indeterminateAnimator.addListener(new AnimatorListenerAdapter() {
            boolean wasCancelled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                wasCancelled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!wasCancelled) {
                    resetAnimator();
                }
            }
        });
        indeterminateAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        double viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        double viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        double widthC = convertDpToPixel(200, getRootView().getContext()) / viewWidth;
        double heightC = convertDpToPixel(220, getRootView().getContext()) / viewHeight;
        hexWidth = (viewWidth / 4.125 * widthC);
        hexHeight = (viewHeight / 3.5 * heightC);
        hexPadding = viewHeight / 23;
        setMeasuredDimension((int) (viewWidth / 1.385 * widthC), (int) (viewHeight * heightC));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(getResources(), getHexagonImage(1)), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        //1
        Path hexPath = hiveRect(hexWidth / 2, hexPadding, hexWidth * 3 / 2, hexHeight + hexPadding);
        paint.setAlpha(getAlpha(1, actualProgress));
        canvas.drawPath(hexPath, paint);
        //2
        hexPath = hiveRect(hexWidth * 3 / 2, hexPadding, hexWidth * 5 / 2, hexHeight + hexPadding);
        paint.setAlpha(getAlpha(2, actualProgress));
        canvas.drawPath(hexPath, paint);
        //6
        hexPath = hiveRect(0, hexHeight * 3 / 4 + hexPadding, hexWidth,
                hexHeight * 7 / 4 + hexPadding);
        paint.setAlpha(getAlpha(6, actualProgress));
        canvas.drawPath(hexPath, paint);
        //7
        hexPath = hiveRect(hexWidth, hexHeight * 3 / 4 + hexPadding, hexWidth * 2,
                hexHeight * 7 / 4 + hexPadding);
        paint.setAlpha(getAlpha(7, actualProgress));
        canvas.drawPath(hexPath, paint);
        //3
        hexPath = hiveRect(hexWidth * 2, hexHeight * 3 / 4 + hexPadding, hexWidth * 3,
                hexHeight * 7 / 4 + hexPadding);
        paint.setAlpha(getAlpha(3, actualProgress));
        canvas.drawPath(hexPath, paint);
        //5
        hexPath = hiveRect(hexWidth / 2, hexHeight * 6 / 4 + hexPadding, hexWidth * 3 / 2,
                hexHeight * 10 / 4 + hexPadding);
        paint.setAlpha(getAlpha(5, actualProgress));
        canvas.drawPath(hexPath, paint);
        //4
        hexPath = hiveRect(hexWidth * 3 / 2, hexHeight * 6 / 4 + hexPadding, hexWidth * 5 / 2,
                hexHeight * 10 / 4 + hexPadding);
        paint.setAlpha(getAlpha(4, actualProgress));
        canvas.drawPath(hexPath, paint);
    }

    private int getHexagonImage(int position) {
        if (position <= 1) {
            return loadingDrawable;
        } else {
            return color;
        }
    }

    private int getAlpha(int num, float progress) {
        float alpha;
        if (progress > num * 100) {
            alpha = maxAlpha;
        } else {
            int min = (num - 1) * 100;
            alpha = (progress - min) > 0 ? progress - min : 0;
            alpha = alpha * maxAlpha / 100;
        }
        if (progress > 700) {
            float fadeProgress = progress - 700;
            if (fadeProgress > num * 100) {
                alpha = 0;
            } else {
                int min = (num - 1) * 100;
                alpha = (fadeProgress - min) > 0 ? fadeProgress - min : 0;
                alpha = maxAlpha - alpha * maxAlpha / 100;
            }
        }
        if (progress > 1400) {
            alpha = 0;
        }
        return (int) alpha;
    }

    private Path hiveRect(double left, double top, double right, double bottom) {
        Path path = new Path();
        double height = Math.abs(bottom - top);
        double width = Math.abs(right - left);
        double r = width > height ? height : width;
        r = r / 2;
        float x = (float) ((right - left) / 2 + left);
        float y = (float) top;
        int edge = (int) (r * Math.sqrt(3) / 2);
        path.moveTo(x, y);
        x = x + edge;
        y = (float) (y + r / 2);
        path.lineTo(x, y);
        y = (float) (y + r);
        path.lineTo(x, y);
        x = x - edge;
        y = (float) (y + r / 2);
        path.lineTo(x, y);
        x = x - edge;
        y = (float) (y - r / 2);
        path.lineTo(x, y);
        y = (float) (y - r);
        path.lineTo(x, y);
        path.close();
        return path;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
