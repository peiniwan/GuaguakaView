package coordemo.ly.com.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 一开始将最下面写在自定义控件里了，扩展性不好
 */
public class GuaGuaKa extends View {

    /**
     * 绘制线条的画笔
     */
    private Paint mOutterPaint = new Paint();
    /**
     * 遮层画笔
     */
    private Paint mMaskPaint = new Paint();
    /**
     * 最下面画笔（原来代码里的，没用到）
     */
    private Paint mBackPint = new Paint();
    /**
     * mCanvas绘制内容在其上
     */
    private Bitmap mBitmap;
    /**
     * 最小面
     */
    private Bitmap scBitmap;
    /**
     * 记录用户绘制的Path
     */
    private Path mPath = new Path();
    /**
     * 内存中创建的Canvas
     */
    private Canvas mCanvas;

    // private Bitmap mBackBitmap;
    private boolean isComplete;
    private Rect mTextBound = new Rect();
    private String mText = "￥500,0000";

    private int mLastX;
    private int mLastY;
    private Context context;
    private int width;
    private int height;
    private RectF rectF;
    private int measuredWidth;

    public GuaGuaKa(Context context) {
        this(context, null);
        this.context = context;
    }

    public GuaGuaKa(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;

    }

    public GuaGuaKa(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        mPath = new Path();
        // mBackBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.t2);
        View view = View.inflate(context, R.layout.scratch_view_after, null);
        TextView tv_look = (TextView) view.findViewById(R.id.tv_look);
        LinearLayout ll_llok = (LinearLayout) view.findViewById(R.id.ll_llok);
        ll_llok.setFocusable(true);
        ll_llok.setClickable(true);

        ll_llok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                looklinstener.onClick();
                Log.d("looklinstener", "looklinstener");
            }
        });
//        tv_look.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                looklinstener.onClick();
//                Log.d("looklinstener","looklinstener");
//            }
//        });
        scBitmap = getViewBitmap(view);
        setUpOutPaint();
        setUpBackPaint();


    }

    public void setLooklinstener(Looklinstener looklinstener) {
        this.looklinstener = looklinstener;
    }

    private Looklinstener looklinstener;

    public interface Looklinstener {
        void onClick();
    }

    /**
     * 初始化canvas的绘制用的画笔
     */
    private void setUpBackPaint() {
        mBackPint.setStyle(Style.FILL);
        mBackPint.setTextScaleX(2f);
        mBackPint.setColor(Color.DKGRAY);
        mBackPint.setTextSize(32);
        mBackPint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawBitmap(mBackBitmap, 0, 0, null);
        // 绘制奖项
        canvas.drawBitmap(scBitmap, (measuredWidth - width) / 2, 0, null);
//        canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2,
//                getHeight() / 2 + mTextBound.height() / 2, mBackPint);
        if (!isComplete) {
            drawPath();
            canvas.drawBitmap(mBitmap, (measuredWidth - width) / 2, 0, null);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        width = scBitmap.getWidth();
        height = scBitmap.getHeight();
        // 初始化bitmap
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);
        rectF = new RectF((measuredWidth - width) / 2, 0, (measuredWidth - width) / 2 + width, height);
        // 绘制遮盖层
        // mCanvas.drawColor(Color.parseColor("#c0c0c0"));
//        mOutterPaint.setStyle(Style.FILL);
        mMaskPaint.setColor(Color.parseColor("#00000000"));
        mMaskPaint.setStyle(Style.FILL);
        mCanvas.drawRoundRect(rectF, 0, 0, mMaskPaint);
        mCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.award_1), null, new RectF(0, 0, width, height), null);
    }

    /**
     * 设置画笔的一些参数
     */
    private void setUpOutPaint() {
        // 设置画笔
//         mOutterPaint.setAlpha(0);
        mOutterPaint.setColor(Color.parseColor("#c0c0c0"));

        mOutterPaint.setAntiAlias(true);
        mOutterPaint.setDither(true);
        mOutterPaint.setStyle(Style.STROKE);
        mOutterPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
        // 设置画笔宽度
        mOutterPaint.setStrokeWidth(50);
    }

    /**
     * 绘制线条
     */
    private void drawPath() {
        mOutterPaint.setStyle(Style.STROKE);
        mOutterPaint
                .setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOutterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:

                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if (dx > 3 || dy > 3)
                    mPath.lineTo(x, y);

                mLastX = x;
                mLastY = y;
                new Thread(mRunnable).start();
                break;
            case MotionEvent.ACTION_UP:
                new Thread(mRunnable).start();
                break;
        }

        invalidate();
        return true;
    }

    /**
     * 统计擦除区域任务
     */
    private Runnable mRunnable = new Runnable() {
        private int[] mPixels;

        @Override
        public void run() {
//
//            int w = getWidth();
//            int h = getHeight();

            int w = width;
            int h = height;

            float wipeArea = 0;
            float totalArea = w * h;

            Bitmap bitmap = mBitmap;

            mPixels = new int[w * h];

            /**
             * 拿到所有的像素信息
             */
            bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

            /**
             * 遍历统计擦除的区域
             */
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int index = i + j * w;
                    if (mPixels[index] == 0) {
                        wipeArea++;
                    }
                }
            }

            /**
             * 根据所占百分比，进行一些操作
             */
            if (wipeArea > 0 && totalArea > 0) {
                int percent = (int) (wipeArea * 100 / totalArea);
//                Log.e("TAG", percent + "");

                if (percent > 50) {
//                    Log.e("TAG", "清除区域达到50%，下面自动清除");
                    isComplete = true;
                    postInvalidate();
                }
            }
        }

    };

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

}
