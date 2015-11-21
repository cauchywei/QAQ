package org.sssta.qaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.sssta.qaq.edit.crop.CropImageView;
import org.sssta.qaq.edit.operate.OperateUtils;
import org.sssta.qaq.edit.utils.FileUtils;
import org.sssta.qaq.utils.FaceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FaceDetectorActivity extends AppCompatActivity {


    private Button mConfirmButton;
    private CropImageView mCropImage;

    private LinearLayout mContentLayout;

    private String photoPath;

    OperateUtils operateUtils;
    private File mCurrentPhotoFile;
    private FaceDetector faceDetector;
    private FaceDetector.Face[] faces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_dectector);

        findViews();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        photoPath = getIntent().getExtras().getString("path");


        Bitmap hh = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crop_button);

        mCropImage.setCropOverlayCornerBitmap(hh);


        operateUtils = new OperateUtils(this);



        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Bitmap face = mCropImage.getCroppedImage();
                final ProgressDialog progressDialog = ProgressDialog.show(FaceDetectorActivity.this, "合成", "正在合成");
                progressDialog.show();

                new AsyncTask(){

                    @Override
                    protected Object doInBackground(Object[] params) {
                        Bitmap greyFace = Filter.discolor(face);
                        return Filter.disWhite(FaceUtils.cropFaceEdge(Filter.changeBitmapContrastBrightness(greyFace, 1.8f, -30)));
                    }


                    @Override
                    protected void onPostExecute(Object o) {
                        BadGlobalCode.tmpFace = ((Bitmap) o);
                        progressDialog.dismiss();
                        startActivity(new Intent(FaceDetectorActivity.this, TemplateActivity.class));
                    }
                }.execute();
            }
        });

        mCropImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mCropImage.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                detectFace();
            }
        },300);
        
    }

    private void findViews() {

        mConfirmButton = (Button) findViewById(R.id.button_confirm);
        mContentLayout = (LinearLayout) findViewById(R.id.layout_content);

        mCropImage = (CropImageView) findViewById(R.id.cropImageView);
    }


    private void detectFace() {

        final Bitmap resizeBmp = operateUtils.compressionFiller(photoPath,
                mContentLayout);
        mCropImage.setImageBitmap(resizeBmp);

        final ProgressDialog progressDialog = ProgressDialog.show(this, "识别", "正在识别人脸");
        progressDialog.show();

        new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] params) {

                faceDetector = new FaceDetector(resizeBmp.getWidth(), resizeBmp.getHeight(), 1);
                faces = new FaceDetector.Face[1];

                int nFace = faceDetector.findFaces(resizeBmp, faces);
                Rect faceRect = null;
                if (nFace != 0) {

                    FaceDetector.Face face = faces[0];
                    int faceSize = (int) (face.eyesDistance() * 1.8);
//                    faceSize += face.eyesDistance() * 0.3;

                    PointF centerFace = new PointF();
                    face.getMidPoint(centerFace);

                    int tInitX = (int) (centerFace.x - faceSize / 2);
                    int tInitY = (int) (centerFace.y - faceSize / 2.3);

                    tInitX = Math.max(0, tInitX);
                    tInitY = Math.max(0, tInitY);

                    int tEndX = tInitX + faceSize;
                    int tEndY = (int) (tInitY + faceSize + faceSize / 7);
                    tEndX = Math.min(tEndX, resizeBmp.getWidth());
                    tEndY = Math.min(tEndY, resizeBmp.getHeight());


                    int initX = resizeBmp.getWidth();
                    int initY = resizeBmp.getHeight();
                    int endX = 0;
                    int endY = 0;

                    initX = Math.min(initX, tInitX);
                    initY = Math.min(initY, tInitY);
                    endX = Math.max(endX, tEndX);
                    endY = Math.max(endY, tEndY);

                    faceRect = new Rect((int)(initX-faceSize*0.1),initY,(int)(endX-faceSize*0.1),endY);



                }

                return faceRect;
            }

            @Override
            protected void onPostExecute(Object o) {

                progressDialog.dismiss();

                if (o == null) {
                    Toast.makeText(FaceDetectorActivity.this, "没有检测到人脸，请拖动选框选择脸部区域", Toast.LENGTH_SHORT).show();
                } else {
                    Rect rect = (Rect) o;
                    mCropImage.setCropFrame(rect.left,rect.top,rect.right,rect.bottom);
                }
            }
        }.execute();


    }

    public String saveBitmap(Bitmap bitmap, String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(Constants.DIR_PATH);
            if (!dir.exists())
                dir.mkdir();
            File file = new File(Constants.DIR_PATH + name + ".jpg");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                    out.flush();
                    out.close();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
