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
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.sssta.qaq.edit.crop.CropImageView;
import org.sssta.qaq.edit.operate.OperateUtils;
import org.sssta.qaq.edit.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FaceDetectorActivity extends AppCompatActivity {


    private static final int PHOTO_FROM_CAMERA = 2333;
    private static final int PHOTO_FROM_GALLERY = 3332;

    private Button mCameraButton;
    private Button mGalleryButton;
    private Button mCropButton;
    private Button mFilterTestButton;

    private CropImageView mCropImage;

    private LinearLayout mContentLayout;

    private String photoPath = null;
    private String tempPhotoPath;

    private int screenWidth;

    OperateUtils operateUtils;
    private File mCurrentPhotoFile;
    private FaceDetector faceDetector;
    private FaceDetector.Face[] faces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;


        Bitmap hh = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crop_button);

        mCropImage.setCropOverlayCornerBitmap(hh);


        operateUtils = new OperateUtils(this);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhotoFormCamera();
            }
        });

        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhotoFromGallery();
            }
        });

        mCropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCropImage.getCroppedImage() == null) {
                    Toast.makeText(FaceDetectorActivity.this, "23333", Toast.LENGTH_SHORT).show();

                } else {
                    BadGlobalCode.tmpFace = mCropImage.getCroppedImage();
                }
            }
        });

        mFilterTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FaceDetectorActivity.this,ImageFilterTestActivity.class));
            }
        });

    }

    private void findViews() {
        mCameraButton = (Button) findViewById(R.id.button_from_camera);
        mGalleryButton = (Button) findViewById(R.id.button_from_gallery);
        mCropButton = (Button) findViewById(R.id.button_crop);
        mFilterTestButton = (Button) findViewById(R.id.button_filter_test);

        mContentLayout = (LinearLayout) findViewById(R.id.layout_content);

        mCropImage = (CropImageView) findViewById(R.id.cropImageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void loadPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_FROM_GALLERY);
    }

    private void loadPhotoFormCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        tempPhotoPath = FileUtils.DCIMCamera_PATH + FileUtils.getNewFileName()
                + ".jpg";

        mCurrentPhotoFile = new File(tempPhotoPath);

        if (!mCurrentPhotoFile.exists()) {
            try {
                mCurrentPhotoFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(mCurrentPhotoFile));
        startActivityForResult(intent, PHOTO_FROM_CAMERA);
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
                    int tEndY = (int) (tInitY + faceSize + faceSize / 8);
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

                if (o == null) {
                    Toast.makeText(FaceDetectorActivity.this, "没有检测到人脸，请拖动选框选择脸部区域", Toast.LENGTH_SHORT).show();
                } else {
                    Rect rect = (Rect) o;
                    mCropImage.setCropFrame(rect.left,rect.top,rect.right,rect.bottom);
                }
                progressDialog.dismiss();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTO_FROM_CAMERA:

                photoPath = tempPhotoPath;
                detectFace();

                break;

            case PHOTO_FROM_GALLERY:

                Uri originalUri = data.getData();

                String[] filePathColumn = {MediaStore.MediaColumns.DATA};
                Cursor cursor = FaceDetectorActivity.this.getContentResolver().query(
                        originalUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                photoPath = cursor.getString(columnIndex);

                detectFace();

                break;
            default:
                break;
        }

    }

}
