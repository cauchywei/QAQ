package org.sssta.qaq;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zzt.library.BooheeScrollView;
import com.zzt.library.BuildLayerLinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by mac on 15/11/21.
 */
public class TemplateActivity extends FragmentActivity{
    private static final String TAG = TemplateActivity.class.getSimpleName();

    private BooheeScrollView mBooheeScrollView;
    private BuildLayerLinearLayout mBuildLayerLinearLayout;
    private ImageView preViewImageView;
    //private StickerView stickerView;
    private List<Integer> imageViews = TemplateID.templateIDList;

    private static Bitmap aboveBitmap,finalBitmap;
    private int currIndex;

    private View mShareView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);


        preTreatmentBitmap();
        mBooheeScrollView = (BooheeScrollView) findViewById(R.id.scrollGallery_horizontal);
        mBuildLayerLinearLayout = (BuildLayerLinearLayout) findViewById(R.id.scrollGallery_linear);
        preViewImageView = (ImageView) findViewById(R.id.scroll_gallery_iv);
        mShareView = findViewById(R.id.imageView_share);
        //stickerView = (StickerView)findViewById(R.id.sticker_view);
        //operateView = (OperateView)findViewById(R.id.operateView);

        mShareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_STREAM, saveImageToGallery(TemplateActivity.this, finalBitmap));
                startActivity(Intent.createChooser(i, "Share"));
            }
        });

        initScrollView();
    }

    private void preTreatmentBitmap() {
        aboveBitmap = BadGlobalCode.tmpFace;
        aboveBitmap = Filter.disWhite(aboveBitmap);
    }

    private void initScrollView() {
        initChildView();
        setPreview();

        preViewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TemplateActivity.this,EditActivity.class);
                i.putExtra("index",currIndex);
                startActivity(i);
            }
        });
    }

    public static Bitmap getAboveBitmap() {
        return aboveBitmap;
    }

    private void setPreview() {
        mBooheeScrollView.setAnimType(BooheeScrollView.REBOUND_ANIM);
        mBooheeScrollView.setScrollChangeListener(new BooheeScrollView.OnScrollChangeListener() {
            @Override
            public void OnScrollChange(int centerViewIndex) {
                if (centerViewIndex > 0 && centerViewIndex <= imageViews.size()) {

                    Bitmap modelBitmap = BitmapFactory.decodeResource(getResources(),
                            imageViews.get(centerViewIndex - 1));

                    Bitmap scaleAboveBitmap = scaleAboveBitmap(centerViewIndex - 1, modelBitmap);

                    Bitmap rotatedBitmap = Filter.bitmapRotate(TemplateID.templateSpinAngleList.
                            get(centerViewIndex - 1).floatValue(),scaleAboveBitmap);

                    Bitmap newBitmap = Filter.mergeBitmap(rotatedBitmap, modelBitmap,
                            getStartX(centerViewIndex - 1, scaleAboveBitmap.getWidth(),
                                    modelBitmap.getWidth()),
                            getStartY(centerViewIndex - 1, scaleAboveBitmap.getHeight(),
                                    modelBitmap.getHeight()));
                    finalBitmap = newBitmap;

                    currIndex = centerViewIndex - 1;

//                    RelativeLayout.LayoutParams layoutParams =
//                            (RelativeLayout.LayoutParams)stickerView.getLayoutParams();
//                    layoutParams.width = scaleAboveBitmap.getWidth();
//                    layoutParams.height = scaleAboveBitmap.getHeight();
//                    stickerView.setLayoutParams(layoutParams);
//                    operateView = new OperateView(TemplateActivity.this,scaleAboveBitmap);
//                    //if (previewLayout.getChildCount() == 0) {
//                    previewLayout.removeAllViews();
//                        previewLayout.addView(operateView);
//                        Bitmap rotateBmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_sticker_control);
//                        Bitmap deleteBmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_sticker_delete);
//                        operateView.addItem(new ImageObject(scaleAboveBitmap,rotateBmp,deleteBmp));
//                        operateView.setX(getStartX(centerViewIndex - 1, scaleAboveBitmap.getWidth(),
//                                modelBitmap.getWidth()));
//                        operateView.setY(getStartY(centerViewIndex - 1, scaleAboveBitmap.getHeight(),
//                                modelBitmap.getHeight()));
//
////                    }

                    preViewImageView.setImageBitmap(newBitmap);
                }
            }
        });
    }

    private void initChildView() {
        ImageView emptyStartImageView = new ImageView(this);
        emptyStartImageView.setLayoutParams(new LinearLayout.LayoutParams(400, 600));
        ImageView emptyLastImageView = new ImageView(this);
        emptyLastImageView.setLayoutParams(new LinearLayout.LayoutParams(400, 600));

        View[] views = new View[imageViews.size()+2];

        views[0] = emptyStartImageView;
        views[imageViews.size()+1] = emptyLastImageView;
        mBuildLayerLinearLayout.addView(emptyStartImageView);

        for (int i = 0;i<imageViews.size();i++) {
            View imageView = getNewImageView(imageViews.get(i));
            mBuildLayerLinearLayout.addView(imageView);
            views[i+1] = imageView;
        }
        mBuildLayerLinearLayout.addView(emptyLastImageView);

        mBooheeScrollView.setChildViews(views);
    }

    private int getStartX(int index, int aboveWidth,int bmpWidth) {
        double centerX = TemplateID.templateCenterXList.get(index) * bmpWidth;
//        double width = TemplateID.templateFWidthList.get(index) * bmpWidth;
        return (int)(centerX - aboveWidth/2);
    }

    private int getStartY(int index,int aboveHeight,int bmpHeight) {
        double centerY = TemplateID.templateCenterYList.get(index) * bmpHeight;
//        double height = TemplateID.templateFHeightList.get(index) * bmpHeight;
        return (int)(centerY - aboveHeight/2);
    }

    private Bitmap scaleAboveBitmap(int index,Bitmap modelBitmap) {
        double faceHeight = TemplateID.templateFHeightList.get(index) * modelBitmap.getHeight();
        double faceWidth = TemplateID.templateFWidthList.get(index) * modelBitmap.getWidth();
        double aboveHeight = aboveBitmap.getHeight();
        double aboveWidth = aboveBitmap.getWidth();

        if (aboveHeight/aboveWidth > 1) {
            return Filter.scaleBitmap(aboveBitmap,(float)(faceHeight/aboveHeight));
        } else {
            return Filter.scaleBitmap(aboveBitmap,(float)(faceWidth/aboveWidth));
        }
    }


    private View getNewImageView(int id) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.face_card, null, false);
        ((ImageView) inflate.findViewById(R.id.imageView_template)).setImageResource(id);
        return inflate;
    }

    public static Uri saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "/QAQ");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "QAQ_"+System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        Uri photoUri = null;
        try {
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            Log.d("aaa", "path --->" + path);
            photoUri = Uri.parse(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        //Uri photoUri = Uri.parse("file://"+file.getAbsolutePath());
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, photoUri));
        return photoUri;
    }
}
