package org.sssta.qaq;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by cauchywei on 15/11/21.
 */
public class Utils {
    public static Bitmap cropFaceEge(Bitmap face) {
        Bitmap bitmap = Bitmap.createBitmap(face.getWidth(),face.getHeight(),Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paint.setAlpha(0);
        canvas.drawRect(0, 0, face.getWidth(), face.getHeight(), paint);

        paint.setAlpha(255);
        Path path = new Path();
        float deltaWidth = face.getWidth()/3;
        path.moveTo(deltaWidth, 0);
        path.lineTo(face.getWidth() - deltaWidth, 0);
        path.lineTo(face.getWidth(),face.getHeight()/9);
        path.lineTo(face.getWidth(), (float) (face.getHeight()/1.5));
        path.lineTo((float) (face.getWidth() / 1.5), face.getHeight());
        path.lineTo(deltaWidth, face.getHeight());
        path.lineTo(0, (float) (face.getHeight() / 1.5));
        path.lineTo(0, face.getHeight() / 9);
        path.close();

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawPath(path, paint);
//
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(face,0,0,paint);

        return bitmap;
    }
}
