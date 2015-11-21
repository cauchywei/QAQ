package org.sssta.qaq;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zzt.library.BooheeScrollView;
import com.zzt.library.BuildLayerLinearLayout;

import java.util.List;

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

    private static Bitmap aboveBitmap;
    private int currIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        preTreatmentBitmap();
        mBooheeScrollView = (BooheeScrollView) findViewById(R.id.scrollGallery_horizontal);
        mBuildLayerLinearLayout = (BuildLayerLinearLayout) findViewById(R.id.scrollGallery_linear);
        preViewImageView = (ImageView) findViewById(R.id.scroll_gallery_iv);
        //stickerView = (StickerView)findViewById(R.id.sticker_view);
        //operateView = (OperateView)findViewById(R.id.operateView);

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
            ImageView imageView = getNewImageView(imageViews.get(i));
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


    private ImageView getNewImageView(int id) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(400, 600));
        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),id));
        return imageView;
    }
}
