package org.sssta.qaq;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by mac on 15/11/21.
 */
public class Filter {

    public static Bitmap disWhite(Bitmap srcBmp) {
//        Bitmap workingBitmap = Bitmap.createBitmap(srcBmp);
//        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas c = new Canvas(mutableBitmap);
//        Paint p = new Paint();
//        p.setAlpha(0);
//        p.setXfermode(new AvoidXfermode(Color.TRANSPARENT, 0, AvoidXfermode.Mode.TARGET));
//        c.drawPaint(p);
//        return mutableBitmap;

        int width = srcBmp.getWidth();
        int height = srcBmp.getHeight();
        Bitmap ret = Bitmap.createBitmap(width,height,srcBmp.getConfig());

        int []pixels = new int[width * height];
        srcBmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0;i < pixels.length; i++) {
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            int blue = Color.blue(pixels[i]);
            int alpha;
            if (red > 150 && green > 150 && blue > 150) {
                alpha = 0;
                pixels[i] = Color.argb(alpha,red,blue,green);
            }
        }

        ret.setPixels(pixels,0,width,0,0,width,height);
        return ret;
    }

    public static Bitmap mergeBitmap(Bitmap aboveBitmap,Bitmap backBitmap,int startX,int startY) {
        int abWidth = aboveBitmap.getWidth();
        int abHeight = aboveBitmap.getHeight();
        int bgWidth = backBitmap.getWidth();
        int bgHeight = backBitmap.getHeight();

        Bitmap newBmp = Bitmap
                .createBitmap(bgWidth, bgHeight, backBitmap.getConfig());
        Canvas canvas = new Canvas(newBmp);
        canvas.drawBitmap(backBitmap, 0, 0, null);
        canvas.drawBitmap(aboveBitmap, startX,
                startY, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return newBmp;
    }

    /**
     * 图片旋转
     */
    public static Bitmap bitmapRotate(float degrees,Bitmap baseBitmap) {
        // 创建一个和原图一样大小的图片
        Bitmap afterBitmap = Bitmap.createBitmap(baseBitmap.getWidth(),
                baseBitmap.getHeight(), baseBitmap.getConfig());
        Canvas canvas = new Canvas(afterBitmap);
        Paint paint = new Paint();
        Matrix matrix = new Matrix();
        // 根据原图的中心位置旋转
        matrix.setRotate(degrees, baseBitmap.getWidth() / 2,
                baseBitmap.getHeight() / 2);
//        matrix.setRotate(degrees, midX,
//                midY);
        canvas.drawBitmap(baseBitmap, matrix, paint);
        return afterBitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bm,float ratio) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        ratio = ratio * 1.2415926f;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    /**
     * 去色
     * @param bitmap
     * @return
     */
    public static Bitmap discolor(Bitmap bitmap) {

        Bitmap ret = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
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

        ret.setPixels(pixels,0,picWidth,0,0,picWidth,picHeight);

        return ret;

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
        }

        ret.setPixels(newBmpPixel, 0, width, 0, 0, width, height);

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
}
