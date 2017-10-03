package com.chandlersystem.chandler.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.chandlersystem.chandler.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageHelper {
    public static final int REQUEST_IMAGE_FROM_LIBRARY = 1;
    public static final int REQUEST_TAKE_PHOTO = 2;

    private Context context;

    private File photoCapturedFile;

    private Disposable subscription;

    private ProgressDialog progressDialog;

    private OnImageCompress callback;

    private Uri fileUri;

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public interface OnImageCompress {
        void onCompressSuccessfully(MultipartBody.Part part);

        void onCompressFailed(Throwable throwable);
    }

    public ImageHelper(Context context, OnImageCompress callback) {
        this.context = context;
        this.callback = callback;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.content_loading));
    }

    public void openLibrary() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((AppCompatActivity) context).startActivityForResult(i, REQUEST_IMAGE_FROM_LIBRARY);
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = System.currentTimeMillis() + "";
        String fileName = "JPEG_" + timeStamp;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            photoCapturedFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES),
                    fileName + ".jpg");
        } else {
            String dir = Environment.getExternalStorageDirectory() + File.separator + "myDirectory";
            //create folder
            File folder = new File(dir); //folder name
            if (!folder.exists()) {
                folder.mkdirs();
            }

            //create file
            photoCapturedFile = new File(dir, fileName + ".jpg");
        }

        Uri temUri = Uri.fromFile(photoCapturedFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, temUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    public void onActivityResult(int requestCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (photoCapturedFile != null) {
                    createAvatar(REQUEST_TAKE_PHOTO, photoCapturedFile.getAbsolutePath());
                }
                break;
            case REQUEST_IMAGE_FROM_LIBRARY:
                if (data != null) {
                    if (getImagePathFromURI(data) != null) {
                        String imagePath = getImagePathFromURI(data);
                        createAvatar(REQUEST_IMAGE_FROM_LIBRARY, imagePath);
                    }
                }
                break;
        }
    }

    private String getImagePathFromURI(Intent data) {
        Uri selectedImage = data.getData();
        fileUri = selectedImage;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);

        if (cursor == null || cursor.getCount() < 1) {
            //Toast.makeText(EditorActivity.this, getResources().getString(R.string.image_erro), Toast.LENGTH_SHORT).show();
            return null; // no cursor or no record. DO YOUR ERROR HANDLING
        }

        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        if (columnIndex < 0) {
            //Toast.makeText(EditorActivity.this, getResources().getString(R.string.image_erro), Toast.LENGTH_SHORT).show();// no column index
            return null; // DO YOUR ERROR HANDLING
        }


        String imagePath = cursor.getString(columnIndex);
        cursor.close(); // close cursor
        return imagePath;
    }

    private void createAvatar(int requestTakePhoto, String imagePath) {
        subscription = Observable.just(imagePath)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }).flatMap(imagePath1 -> {
                    try {
                        return Observable.just(new File(compressImage(imagePath1)));
                    } catch (ArithmeticException e) {
                        e.printStackTrace();
                        return Observable.empty();
                    }
                })
                .filter(upstreamFile -> upstreamFile != null)
                .map(imageFile -> getRequestBody(requestTakePhoto, imageFile))
                .subscribe(callback::onCompressSuccessfully, throwable -> {
                    progressDialog.dismiss();
                    callback.onCompressFailed(throwable);
                });
    }

    private String compressImage(String imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 400.0f;
        float maxWidth = 600.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth,
                                      int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private MultipartBody.Part getRequestBody(int type, File imageFile) {
        RequestBody request;
        if (type == REQUEST_IMAGE_FROM_LIBRARY) {
            request = RequestBody.create(
                    MediaType.parse(context
                            .getContentResolver()
                            .getType(fileUri)), imageFile);
        } else {
            request = RequestBody.create(
                    MediaType.parse("image/jpg"),
                    imageFile);
        }

        return MultipartBody.Part.createFormData("avatar", imageFile.getName(), request);
    }

    private String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "SevenReward/SevenRewardPicture");
        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + "IMG_" + System.currentTimeMillis() + ".png");
    }

    public void onDestroy() {
        subscription.dispose();
    }
}
