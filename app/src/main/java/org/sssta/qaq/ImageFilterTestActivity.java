package org.sssta.qaq;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by Cauchywei on 15/11/20.
 */
public class ImageFilterTestActivity extends Activity implements View.OnClickListener {
    public static final String TAG = ImageFilterTestActivity.class.getSimpleName();
    private static final int RGB_MASK = 0x00FFFFFF;

    private ImageView mTestImageView;
    private Button mSrcButton, mGreyButton, mConvertButton, mReverseButton, mDevideButton;
    private Bitmap mSrcBitmap, mGreyBitmap, mConvertBitmap, mReserveBitmap, mDevideBitmap;
    private DiscreteSeekBar mContrastSeekBar, mBrightnessSeekBar;

    float mCurrContrast, mCurrBrightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter_test);
        findViews();
        setContrastSeekBarSlide();
        mCurrBrightness = 0;
        mCurrContrast = 50;

        //Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show();
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        mGreyBitmap = greyScale(mSrcBitmap);
        mConvertBitmap = mGreyBitmap;
        mReserveBitmap = mGreyBitmap;

        mTestImageView.setImageBitmap(mSrcBitmap);
    }

    private void findViews() {
        mTestImageView = (ImageView)findViewById(R.id.test_imageview);
        mSrcButton = (Button)findViewById(R.id.src_button);
        mGreyButton = (Button)findViewById(R.id.grey_button);
        mConvertButton = (Button)findViewById(R.id.convert_button);
        mReverseButton = (Button)findViewById(R.id.reserve_button);
        mDevideButton = (Button)findViewById(R.id.divide_button);
        mContrastSeekBar = (DiscreteSeekBar)findViewById(R.id.contrast_seek_bar);
        mBrightnessSeekBar = (DiscreteSeekBar)findViewById(R.id.brightness_seek_bar);

        mSrcButton.setOnClickListener(this);
        mGreyButton.setOnClickListener(this);
        mConvertButton.setOnClickListener(this);
        mReverseButton.setOnClickListener(this);
        mContrastSeekBar.setIndicatorPopupEnabled(true);
        mDevideButton.setOnClickListener(this);
    }

    private void setContrastSeekBarSlide() {
        mContrastSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int progress, boolean b) {
                Log.d(TAG, "mCurrBrightness---->" + mCurrBrightness);
                //double myProgress;
//                if (progress <= 50) {
//                    myProgress = 0.18 * progress / 50;
//                } else {
//                    myProgress = ((double)(progress - 50)/50)*9 ;
//                }
                mConvertBitmap = changeBitmapContrastBrightness(mGreyBitmap, (float) progress / 10.0f, mCurrBrightness);
                mCurrContrast = (float) (progress / 10.0f);
                mTestImageView.setImageBitmap(mConvertBitmap);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {
                mReserveBitmap = mConvertBitmap;
            }
        });

        mBrightnessSeekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int progress, boolean b) {
                Log.d(TAG, "mCurrContrast---->" + mCurrContrast);
                mConvertBitmap = changeBitmapContrastBrightness(mGreyBitmap, mCurrContrast, progress);
                mCurrBrightness = progress;
                mTestImageView.setImageBitmap(mConvertBitmap);
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {
                mReserveBitmap = mConvertBitmap;
            }
        });
    }

    private Bitmap greyScale(Bitmap originBitmap) {
        int width,height;
        width = originBitmap.getWidth();
        height = originBitmap.getHeight();

        mGreyBitmap = Bitmap.createBitmap(discolor(originBitmap),width,height, Bitmap.Config.RGB_565);
        return mGreyBitmap;
    }

    public static int[] discolor(Bitmap bitmap) {

        int picHeight = bitmap.getHeight();
        int picWidth = bitmap.getWidth();

        int[] pixels = new int[picWidth * picHeight];
        bitmap.getPixels(pixels, 0, picWidth, 0, 0, picWidth, picHeight);

        for (int i = 0; i < picHeight; ++i) {
            for (int j = 0; j < picWidth; ++j) {
                int index = i * picWidth + j;
                int color = pixels[index];
                int r = (color & 0x00ff0000) >> 16;
                int g = (color & 0x0000ff00) >> 8;
                int b = (color & 0x000000ff);
                int grey = (int) (r * 0.3 + g * 0.59 + b * 0.11);
                pixels[index] = grey << 16 | grey << 8 | grey | 0xff000000;
            }
        }

        return pixels;

    }

    private Bitmap createInvertedBitmap(Bitmap src) {
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[] {
                        -1,  0,  0,  0, 255,
                        0, -1,  0,  0, 255,
                        0,  0, -1,  0, 255,
                        0,  0,  0,  1,   0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(
                colorMatrix_Inverted);

        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();

        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(src, 0, 0, paint);

        return bitmap;
    }

    public Bitmap changeAlpha(int alpha, Bitmap bmp) {
        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Paint paint = new Paint();
        paint.setAlpha(alpha);
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }

    public static Bitmap getBlendDivide(Bitmap backBitmap,Bitmap aboveBitmap) {
        Bitmap ret = Bitmap.createBitmap(backBitmap.getWidth(), backBitmap.getHeight(),
                backBitmap.getConfig());

        // Get info about Bitmap
        int width = backBitmap.getWidth();
        int height = backBitmap.getHeight();
        int pixels = width * height;

        // Get backBitmap pixels
        int[] backPixel = new int[pixels];
        backBitmap.getPixels(backPixel, 0, width, 0, 0, width, height);

        // Get aboveBitmap pixels
        int[] abovePixel = new int[pixels];
        aboveBitmap.getPixels(abovePixel, 0, width, 0, 0, width, height);

        int[] newBmpPixel = new int[pixels];
        for (int i = 0;i < backPixel.length;i++) {
            int newRed = (int)checkDivideByZero(Color.red(backPixel[i]),Color.red(abovePixel[i]));
            int newGreen = (int)checkDivideByZero(Color.green(backPixel[i]),Color.green(abovePixel[i])) ;
            int newBlue = (int)checkDivideByZero(Color.blue(backPixel[i]),Color.blue(abovePixel[i]));
            int alpha = Color.alpha(backPixel[i]);
            newBmpPixel[i] = Color.argb(alpha,newRed,newGreen,newBlue);
            //newBmpPixel[i] =(int)( (backPixel[i] * 1.0f / (abovePixel[i] * 1.0f))*255);
//            Log.d(TAG,"-----"+i+"-----");
//            Log.d(TAG,"newP--->"+newBmpPixel[i]);
//            Log.d(TAG,"backP--->"+backPixel[i]);
//            Log.d(TAG,"aboveP--->"+abovePixel[i]);
        }

        ret.setPixels(newBmpPixel, 0, width,0,0,width,height);

        return ret;
    }

    private static float checkDivideByZero(int backColor,int aboveColor) {

        if (aboveColor == 0) {
//            if (backColor == 0) {
//                return 0;
//            } else {
//                return 1;
//            }
            return 255;
        }
        if (aboveColor == 1) {
            return backColor;
        }

        //Log.d(TAG,"color--->"+ (backColor*1.0f) / (aboveColor*1.0f));
        float newColor = ((backColor*1.0f) / (aboveColor*1.0f)) * 255;
        if (newColor > 255) {
            return 255;
        }
        return newColor;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grey_button:
                //Toast.makeText(ImageFilterTestActivity.this,"grey",Toast.LENGTH_SHORT).show();
                mTestImageView.setImageBitmap(mGreyBitmap);
                break;
            case R.id.convert_button:
                mTestImageView.setImageBitmap(mConvertBitmap);
                break;
            case R.id.src_button:
                //Toast.makeText(ImageFilterTestActivity.this,"src",Toast.LENGTH_SHORT).show();
                mTestImageView.setImageBitmap(mSrcBitmap);
                break;
            case R.id.reserve_button:
                //makeReserveBitmap();
                if (mReserveBitmap == null) {
                    mReserveBitmap = mGreyBitmap;
                    Toast.makeText(ImageFilterTestActivity.this, "reverseBitmap is null", Toast.LENGTH_SHORT
                    ).show();
                }

                mReserveBitmap = createInvertedBitmap(mConvertBitmap);
                mTestImageView.setImageBitmap(mReserveBitmap);
                break;
            case R.id.divide_button:
                mDevideBitmap = getBlendDivide(mConvertBitmap, mReserveBitmap);
                mTestImageView.setImageBitmap(mDevideBitmap);
            default:
                break;
        }
    }
}
