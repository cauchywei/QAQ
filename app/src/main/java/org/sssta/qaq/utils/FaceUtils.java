package org.sssta.qaq.utils;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by cauchywei on 15/11/21.
 */
public class FaceUtils {
    public static Bitmap cropFaceEdge(Bitmap faceBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(faceBitmap.getWidth(),faceBitmap.getHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paint.setARGB(0, 255, 255, 255);
        canvas.drawRect(0, 0, faceBitmap.getWidth(), faceBitmap.getHeight(), paint);

        paint.setAlpha(255);

        Path path = new Path();
        float deltaWidth = faceBitmap.getWidth()/3;
        path.moveTo(deltaWidth, 0);
        path.lineTo(faceBitmap.getWidth() - deltaWidth, 0);
        path.lineTo(faceBitmap.getWidth(),faceBitmap.getHeight()/9);

        path.lineTo(faceBitmap.getWidth(), (float) (faceBitmap.getHeight()/1.7));

        path.lineTo((float) (faceBitmap.getWidth()*5.0/6), (float) (faceBitmap.getHeight()/1.2));

        path.lineTo((float) (faceBitmap.getWidth() -deltaWidth), faceBitmap.getHeight());

        path.lineTo(deltaWidth, faceBitmap.getHeight());

        path.lineTo((float) (faceBitmap.getWidth() / 6), (float) (faceBitmap.getHeight() / 1.2));

        path.lineTo(0, (float) (faceBitmap.getHeight() / 1.7));

        path.lineTo(0, faceBitmap.getHeight() / 9);
        path.close();

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawPath(path, paint);
//
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(faceBitmap, 0, 0, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(faceBitmap.getWidth() / 7);
        paint.setMaskFilter(new BlurMaskFilter(faceBitmap.getWidth() / 10, BlurMaskFilter.Blur.NORMAL));
        paint.setARGB(100, 255, 255, 255);

        canvas.drawPath(path, paint);

        return bitmap;
    }
}
