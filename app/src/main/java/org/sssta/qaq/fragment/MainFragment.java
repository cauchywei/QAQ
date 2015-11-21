package org.sssta.qaq.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.sssta.qaq.FaceDetectorActivity;
import org.sssta.qaq.R;
import org.sssta.qaq.edit.operate.OperateUtils;
import org.sssta.qaq.edit.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by cauchywei on 15/11/22.
 */
public class MainFragment extends BasePageFragment {

    private static final int PHOTO_FROM_CAMERA = 2333;
    private static final int PHOTO_FROM_GALLERY = 3332;

    private Button mCameraButton;
    private Button mGalleryButton;

    private String photoPath = null;
    private String tempPhotoPath;

    private File mCurrentPhotoFile;


    @Override
    public String getTitle() {
        return "制作";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        mCameraButton = (Button) rootView.findViewById(R.id.button_from_camera);
        mGalleryButton = (Button) rootView.findViewById(R.id.button_from_gallery);


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

        return rootView;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTO_FROM_CAMERA:

                photoPath = tempPhotoPath;
                break;

            case PHOTO_FROM_GALLERY:

                Uri originalUri = data.getData();

                String[] filePathColumn = {MediaStore.MediaColumns.DATA};
                Cursor cursor = getContext().getContentResolver().query(
                        originalUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                photoPath = cursor.getString(columnIndex);

                break;
            default:
                break;
        }

        if (PHOTO_FROM_CAMERA == requestCode || PHOTO_FROM_GALLERY == requestCode) {
            Intent intent = new Intent(getContext(), FaceDetectorActivity.class);
            intent.putExtra("path",photoPath);
            startActivity(intent);
        }

    }
}
