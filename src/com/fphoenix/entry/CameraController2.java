//package com.fphoenix.entry;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.res.Configuration;
//import android.fphoenix.face.DoodleGame;
//import android.hardware.Camera;
//import android.os.Build;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Display;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.WindowManager;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.badlogic.gdx.Gdx;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * Created by alan on 18-4-25.
// */
//// camera controller to use surface texture
//public class CameraController2 implements CameraRequest {
//    private static final String TAG = CameraController2.class.getSimpleName();
//    private static boolean DEBUGGING = true;
//    private static final String LOG_TAG = TAG;
//    private static final String CAMERA_PARAM_ORIENTATION = "orientation";
//    private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
//    private static final String CAMERA_PARAM_PORTRAIT = "portrait";
//    protected Activity mActivity;
//    protected Camera mCamera;
//    protected List<Camera.Size> mPreviewSizeList;
//    protected List<Camera.Size> mPictureSizeList;
//    protected List<Integer> mPreviewFormatList;
//    protected Camera.Size mPreviewSize;
//    protected Camera.Size mPictureSize;
//    private int mCameraId;
//    private int mCenterPosX, mCenterPosY;
//
//    Camera.PreviewCallback mPreviewCallback = null;
//    CameraController.PreviewReadyCallback mPreviewReadyCallback = null;
//    CameraController.PreviewFailedCallback mPreviewFailedCallback = null;
//
//    public static enum LayoutMode {
//        FitToParent, // Scale to the size that no side is larger than the parent
//        NoBlank // Scale to the size that no side is smaller than the parent
//    };
//
//    public interface PreviewReadyCallback {
//        public void onPreviewReady();
//    }
//    public interface PreviewFailedCallback {
//        void onPreviewFailed(String errMessage);
//    }
//
//    /**
//     * State flag: true when surface's layout size is set and surfaceChanged()
//     * process has not been completed.
//     */
//    protected boolean mSurfaceConfiguring = false;
//
//    public CameraController2(Activity activity) {
//        this.mActivity = activity;
//    }
//
//    void size() {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        WindowManager wm = (WindowManager) mActivity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
//        wm.getDefaultDisplay().getMetrics(displayMetrics);
//        int screenWidth = displayMetrics.widthPixels;
//        int screenHeight = displayMetrics.heightPixels;
//    }
//
//    @Override
//    public void startCamera() {
//        Camera.CameraInfo info = new Camera.CameraInfo();
//        mCameraId = -1;
//        for (int i=0,n=Camera.getNumberOfCameras(); i<n; i++) {
//            Camera.getCameraInfo(i, info);
//            if (info.facing== Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                mCamera = Camera.open(i);
//                mCameraId = i;
//                break;
//            }
//        }
//        if (mCameraId==-1 || mCamera==null) {
//            // cann't open front camera
//            if (mPreviewFailedCallback != null)
//                mPreviewFailedCallback.onPreviewFailed("Cannot find available front camera");
//            Toast.makeText(mActivity, "Cannot open front camera...", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        WindowManager wm = (WindowManager) mActivity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
//        wm.getDefaultDisplay().getMetrics(displayMetrics);
//        final int screenWidth = displayMetrics.widthPixels;
//        final int screenHeight = displayMetrics.heightPixels;
//        setCenterPosition(screenWidth/2, screenHeight/2);
//        Camera.Parameters cameraParams = mCamera.getParameters();
//        mPreviewFormatList = cameraParams.getSupportedPreviewFormats();
//        mPreviewSizeList = cameraParams.getSupportedPreviewSizes();
//        mPictureSizeList = cameraParams.getSupportedPictureSizes();
//
//
//        System.out.printf("screen size = %d x %d\n", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                surfaceCreated();
//                System.out.printf("screen size = %d x %d\n", screenWidth, screenHeight);
//                doSurfaceChanged(screenWidth, screenHeight);
//            }
//        });
//    }
//
//    public void setup() {
//        Log.d("CCAM", "setup: before use camera");
//        DoodleGame.get().useCamera(this);
//        Log.d("CCAM", "setup: after use camera");
//    }
//
//    public void dispose() {
//        stop();
//    }
//
//    public Camera getCamera() {
//        return mCamera;
//    }
//    public void surfaceCreated() {
//        if (mCamera==null) return;
//        try {
//            Log.d(TAG, "surfaceCreated: set preview display");
//            mCamera.setPreviewDisplay(mHolder);
//        } catch (IOException e) {
//            e.printStackTrace();
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//
//    public void surfaceChanged(int width, int height) {
//        doSurfaceChanged(width, height);
//    }
//
//    private void doSurfaceChanged(int width, int height) {
//        if (mCamera==null) return;
//        mCamera.stopPreview();
//
//        Camera.Parameters cameraParams = mCamera.getParameters();
//        boolean portrait = isPortrait();
//
//        // The code in this if-statement is prevented from executed again when surfaceChanged is
//        // called again due to the change of the layout size in this if-statement.
//        if (!mSurfaceConfiguring) {
//            Camera.Size previewSize = determinePreviewSize(portrait, width, height);
//            Camera.Size pictureSize = determinePictureSize(previewSize);
//            if (DEBUGGING) { Log.v(LOG_TAG, "Desired Preview Size - w: " + width + ", h: " + height); }
//            mPreviewSize = previewSize;
//            mPictureSize = pictureSize;
//            mSurfaceConfiguring = adjustSurfaceLayoutSize(previewSize, portrait, width, height);
//            // Continue executing this method if this method is called recursively.
//            // Recursive call of surfaceChanged is very special case, which is a path from
//            // the catch clause at the end of this method.
//            // The later part of this method should be executed as well in the recursive
//            // invocation of this method, because the layout change made in this recursive
//            // call will not trigger another invocation of this method.
//            if (mSurfaceConfiguring && (mSurfaceChangedCallDepth <= 1)) {
//                return;
//            }
//        }
//
//        configureCameraParameters(cameraParams, portrait);
//        mSurfaceConfiguring = false;
//
//        try {
//            mCamera.startPreview();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w(LOG_TAG, "Failed to start preview: " + e.getMessage());
//
//            // Remove failed size
//            mPreviewSizeList.remove(mPreviewSize);
//            mPreviewSize = null;
//
//            // Reconfigure
//            if (mPreviewSizeList.size() > 0) { // prevent infinite loop
//                surfaceChanged(width, height);
//            } else {
//                Toast.makeText(mActivity, "Can't start preview", Toast.LENGTH_LONG).show();
//                Log.w(LOG_TAG, "Gave up starting preview");
//                if (mPreviewFailedCallback !=null)
//                    mPreviewFailedCallback.onPreviewFailed("failed to open front camera");
//            }
//            return;
//        }
//
//        if (null != mPreviewReadyCallback) {
//            mPreviewReadyCallback.onPreviewReady();
//        }
//    }
//
//    /**
//     * @param portrait
//     * @param reqWidth must be the value of the parameter passed in surfaceChanged
//     * @param reqHeight must be the value of the parameter passed in surfaceChanged
//     * @return Camera.Size object that is an element of the list returned from Camera.Parameters.getSupportedPreviewSizes.
//     */
//    protected Camera.Size determinePreviewSize(boolean portrait, int reqWidth, int reqHeight) {
//        // Meaning of width and height is switched for preview when portrait,
//        // while it is the same as user's view for surface and metrics.
//        // That is, width must always be larger than height for setPreviewSize.
//        int reqPreviewWidth; // requested width in terms of camera hardware
//        int reqPreviewHeight; // requested height in terms of camera hardware
//        if (portrait) {
//            reqPreviewWidth = reqHeight;
//            reqPreviewHeight = reqWidth;
//        } else {
//            reqPreviewWidth = reqWidth;
//            reqPreviewHeight = reqHeight;
//        }
//
//        if (DEBUGGING) {
//            Log.v(LOG_TAG, "Listing all supported preview sizes");
//            for (Camera.Size size : mPreviewSizeList) {
//                Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
//            }
//            Log.v(LOG_TAG, "Listing all supported picture sizes");
//            for (Camera.Size size : mPictureSizeList) {
//                Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
//            }
//        }
//
//        // Adjust surface size with the closest aspect-ratio
//        float reqRatio = ((float) reqPreviewWidth) / reqPreviewHeight;
//        float curRatio, deltaRatio;
//        float deltaRatioMin = Float.MAX_VALUE;
//        Camera.Size retSize = null;
//        for (Camera.Size size : mPreviewSizeList) {
//            curRatio = ((float) size.width) / size.height;
//            deltaRatio = Math.abs(reqRatio - curRatio);
//            if (deltaRatio < deltaRatioMin) {
//                deltaRatioMin = deltaRatio;
//                retSize = size;
//            }
//        }
//
//        return retSize;
//    }
//
//    protected Camera.Size determinePictureSize(Camera.Size previewSize) {
//        Camera.Size retSize = null;
//        for (Camera.Size size : mPictureSizeList) {
//            if (size.equals(previewSize)) {
//                return size;
//            }
//        }
//
//        if (DEBUGGING) { Log.v(LOG_TAG, "Same picture size not found."); }
//
//        // if the preview size is not supported as a picture size
//        float reqRatio = ((float) previewSize.width) / previewSize.height;
//        float curRatio, deltaRatio;
//        float deltaRatioMin = Float.MAX_VALUE;
//        for (Camera.Size size : mPictureSizeList) {
//            curRatio = ((float) size.width) / size.height;
//            deltaRatio = Math.abs(reqRatio - curRatio);
//            if (deltaRatio < deltaRatioMin) {
//                deltaRatioMin = deltaRatio;
//                retSize = size;
//            }
//        }
//
//        return retSize;
//    }
//
//    protected boolean adjustSurfaceLayoutSize(Camera.Size previewSize, boolean portrait,
//                                              int availableWidth, int availableHeight) {
//        float tmpLayoutHeight, tmpLayoutWidth;
//        if (portrait) {
//            tmpLayoutHeight = previewSize.width;
//            tmpLayoutWidth = previewSize.height;
//        } else {
//            tmpLayoutHeight = previewSize.height;
//            tmpLayoutWidth = previewSize.width;
//        }
//
//        float factH, factW, fact;
//        factH = availableHeight / tmpLayoutHeight;
//        factW = availableWidth / tmpLayoutWidth;
//        if (mLayoutMode == CameraController.LayoutMode.FitToParent) {
//            // Select smaller factor, because the surface cannot be set to the size larger than display metrics.
//            if (factH < factW) {
//                fact = factH;
//            } else {
//                fact = factW;
//            }
//        } else {
//            if (factH < factW) {
//                fact = factW;
//            } else {
//                fact = factH;
//            }
//        }
//
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)surfaceView.getLayoutParams();
//
//        int layoutHeight = (int) (tmpLayoutHeight * fact);
//        int layoutWidth = (int) (tmpLayoutWidth * fact);
//        if (DEBUGGING) {
//            Log.v(LOG_TAG, "Preview Layout Size - w: " + layoutWidth + ", h: " + layoutHeight);
//            Log.v(LOG_TAG, "Scale factor: " + fact);
//        }
//
//        boolean layoutChanged;
//        if ((layoutWidth != surfaceView.getWidth()) || (layoutHeight != surfaceView.getHeight())) {
//            layoutParams.height = layoutHeight;
//            layoutParams.width = layoutWidth;
//            if (mCenterPosX >= 0) {
//                layoutParams.topMargin = mCenterPosY - (layoutHeight / 2);
//                layoutParams.leftMargin = mCenterPosX - (layoutWidth / 2);
//            }
//            surfaceView.setLayoutParams(layoutParams); // this will trigger another surfaceChanged invocation.
//            layoutChanged = true;
//        } else {
//            layoutChanged = false;
//        }
//
//        return layoutChanged;
//    }
//
//    /**
//     * @param x X coordinate of center position on the screen. Set to negative value to unset.
//     * @param y Y coordinate of center position on the screen.
//     */
//    public void setCenterPosition(int x, int y) {
//        mCenterPosX = x;
//        mCenterPosY = y;
//    }
//
//    protected void configureCameraParameters(Camera.Parameters cameraParams, boolean portrait) {
//        cameraParams.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//        cameraParams.setPictureSize(mPictureSize.width, mPictureSize.height);
//        if (DEBUGGING) {
//            Log.v(LOG_TAG, "Preview Actual Size - w: " + mPreviewSize.width + ", h: " + mPreviewSize.height);
//            Log.v(LOG_TAG, "Picture Actual Size - w: " + mPictureSize.width + ", h: " + mPictureSize.height);
//        }
//
//        mCamera.setParameters(cameraParams);
//    }
//
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        stop();
//    }
//
//    public void stop() {
//        if (null == mCamera) {
//            return;
//        }
//        mCamera.stopPreview();
//        mCamera.release();
//        mCamera = null;
//    }
//
//    public boolean isPortrait() {
//        return (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
//    }
//
//    public Camera.Size getPreviewSize() {
//        return mPreviewSize;
//    }
//
//    public void setOnPreviewReady(CameraController.PreviewReadyCallback cb) {
//        mPreviewReadyCallback = cb;
//    }
//}
