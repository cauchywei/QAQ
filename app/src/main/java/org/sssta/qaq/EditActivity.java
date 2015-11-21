package org.sssta.qaq;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.sssta.qaq.widget.StickerPropertyModel;
import org.sssta.qaq.widget.StickerView;

/**
 * Created by mac on 15/11/21.
 */
public class EditActivity extends Activity {
    private ImageView previewImageView;
    private DiscreteSeekBar contrastSeekBar,brightnessSeekBar;
    private int currIndex;
    private Bitmap aboveBitmap,modleBitmap,finalBitmap;
    private RelativeLayout preLayout;
    private StickerView mCurrentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        contrastSeekBar = (DiscreteSeekBar)findViewById(R.id.edit_contrast_seek_bar);
        brightnessSeekBar = (DiscreteSeekBar)findViewById(R.id.edit_brightness_seek_bar);
        previewImageView = (ImageView) findViewById(R.id.edit_imageview);
        preLayout = (RelativeLayout) findViewById(R.id.edit_prelayout);
        //stickerView = (StickerView) findViewById(R.id.edit_stickerView);
        currIndex = getIntent().getExtras().getInt("index");

        init();
    }

    private void init(){
        modleBitmap = BitmapFactory.decodeResource(getResources(),
                TemplateID.templateIDList.get(currIndex));

        previewImageView.setImageBitmap(modleBitmap);
        aboveBitmap = TemplateActivity.getAboveBitmap();
        addStickerView();


        //previewImageView.setImageBitmap(TemplateActivity.getAboveBitmap());
    }


    //添加表情
    private void addStickerView() {
        final StickerView stickerView = new StickerView(this);
        stickerView.setBitmap(aboveBitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                preLayout.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                Bitmap aboveBmp = stickerView.getmBitmap();
                StickerPropertyModel model = new StickerPropertyModel();
                stickerView.calculate(model);
                aboveBmp = Filter.bitmapRotate(Float.parseFloat(""+-model.getDegree()*180/Math.PI)
                        ,aboveBmp);
                finalBitmap = Filter.mergeBitmap(aboveBmp, modleBitmap,
                        getStartX(currIndex, aboveBmp.getWidth(),
                                modleBitmap.getWidth()),
                        getStartY(currIndex - 1, aboveBmp.getHeight(),
                                modleBitmap.getHeight()));


                //Log.d("degree",model.getDegree()+"");
                previewImageView.setImageBitmap(finalBitmap);
                stickerView.setVisibility(View.INVISIBLE);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        //lp.height = aboveBitmap.getHeight();
        //lp.width = aboveBitmap.getWidth();
        //stickerView.setTranslationX(aboveBitmap.);
//        StickerPropertyModel model = new StickerPropertyModel();
//        model.setDegree(75);
//        model.setScaling(stickerView.getHeight()/aboveBitmap.getHeight());
//        stickerView.calculate(model);
        preLayout.addView(stickerView, lp);

        setCurrentEdit(stickerView);
    }

    /**
     * 设置当前处于编辑模式的贴纸
     */
    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }

        mCurrentView = stickerView;
        stickerView.setInEdit(true);
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


}
