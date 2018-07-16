//package com.fphoenix.entry;
//
//import android.fphoenix.face.ActivityCallback;
//import android.fphoenix.face.DoodleGame;
//import android.fphoenix.face.MainActivity;
//import android.graphics.ImageFormat;
//import android.graphics.PixelFormat;
//import android.hardware.Camera;
//import android.opengl.GLSurfaceView;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.RelativeLayout;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Graphics;
//import com.badlogic.gdx.backends.android.AndroidGraphics;
//import com.badlogic.gdx.graphics.Pixmap;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.scenes.scene2d.Action;
//import com.fphoenix.common.BaseGame;
//import com.fphoenix.common.BaseScreen;
//import com.fphoenix.common.Config;
//import com.fphoenix.common.InputUtils;
//import com.fphoenix.common.Utils;
//import com.fphoenix.common.ui.button.MyBaseButton;
//import com.fphoenix.common.ui.button.MyScaleButton;
//import com.fphoenix.face.show.R;
//import com.fphoenix.gdx.Background;
//import com.fphoenix.gdx.CameraDataQueue;
//import com.fphoenix.gdx.ClassifierWorker;
//import com.fphoenix.gdx.FaceGroup;
//import com.fphoenix.gdx.MultiTexture;
//import com.fphoenix.gdx.MyTexture;
//import com.fphoenix.gdx.TextureDataAdapter;
//import com.fphoenix.gdx.TfPipe;
//
///**
// * Created by wuhao on 11/20/17.
// */
//@Deprecated
//public class GdxGameScreen extends BaseScreen implements ActivityCallback, Runnable {
//    public GdxGameScreen(BaseGame game) {
//        super(game);
//        initAndroid();
//        addButton();
//        System.out.println("camera object = "+cameraObject);
//    }
//    private void initAndroid() {
//        activity = (MainActivity) Gdx.app;
//    }
//
//    public MainActivity getActivity() {
//        return activity;
//    }
//    void startPreview() {
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                initCameraSurfaceView();
//                activity.registerCallback(GdxGameScreen.this);
//            }
//        });
//    }
//    public void onCameraConfigured(final int width, final int height, final int format, final Camera camera) {
//        stage.getRoot().addAction(new Action() {
//            @Override
//            public boolean act(float v) {
//                onCameraConfigured__(width, height, format, camera);
//                return true;
//            }
//        });
//    }
//    // must call this in GL thread.
//    // from here now, the camera is initialized successful
//    // it's time to start init our work.
//    void onCameraConfigured__(int width, int height, int format, Camera camera) {
//        System.out.println("background = "+background);
//        System.out.printf("preview size = %d x %d\n", width, height);
//
//        if (background==null) {
//            background = new Background(width, height);
//            stage.addActor(background);
//        }
//        background.setMultiTexture(create_or_update_multitexture(width, height));
//        background.setCameraDataQueue(cameraDataQueue);
//
////        stage.getRoot().addActorBefore(btn, background);
//
//
//        imagePipe = new TfPipe().init(((MyTexture) background.getFrameBufferHead().getColorBufferTexture()));
//        background.setImagePipe(imagePipe);
//
//        // worker
//        if (classifierWorker != null) classifierWorker.setQuit();
////        classifierWorker = new ClassifierWorker().init(DoodleGame.get().getModel(model_tag), imagePipe);
//        classifierWorker = new ClassifierWorker().init(DoodleGame.get().getModel2(), imagePipe);
//        classifierWorker.start();
//
//        /////////////////
//        background.input_buffer = DoodleGame.get().getModel2().input_buffer;
//        // add emoji layer
//
//        if (faceGroup==null) {
//            Camera.Size preview_size = camera.getParameters().getPreviewSize();
//            FaceGroup fg = new FaceGroup();
//            fg.getConvertor().setup(preview_size.width, preview_size.height, Config.width, Config.height);
//            stage.addActor(fg);
//            background.setFaceGroup(fg);
//            faceGroup = fg;
//        }
//    }
//    FaceGroup faceGroup = null;
//    void addButton() {
//        TextureAtlas ta = Utils.load_get(Assets.main_atlas);
//        btn = new MyScaleButton(ta.findRegion("modeFree")) {
//            @Override
//            public void onClick() {
//                clickBtn();
//            }
//        }.setScaleFactor(1.1f);
//        stage.addActor(btn);
//        btn.setPosition(240, 200);
//        System.out.println("add Button ##############################################################################33");
//    }
//    void clickBtn() {
//        System.out.println("click btn ...");
//    }
//
//    class PreviewCallback implements Camera.PreviewCallback {
//        @Override
//        public void onPreviewFrame(byte[] bytes, Camera camera) {
//            cameraCallback(bytes, camera);
//            if (!hasPrint) {
//                hasPrint = true;
//                System.out.println("preview callback id = " + Thread.currentThread().getId());
//            }
//        }
//        boolean hasPrint  = false;
//    }
//    void cameraCallback(byte []bytes, Camera camera) {
//        byte []buffer = cameraDataQueue.push(bytes, true);
//        camera.addCallbackBuffer(buffer);
//    }
//    MultiTexture create_or_update_multitexture(int width, int height) {
//        if (this.multiTexture==null) {
//            this.multiTexture = createMultiTexture(width, height);
//        }else {
//            if (this.width!=width || this.height!=height) {
//                this.multiTexture.dispose();
//                this.width = width;
//                this.height = height;
//                this.multiTexture = createMultiTexture(width, height);
//            }
//        }
//        return this.multiTexture;
//    }
//    MultiTexture createMultiTexture(int width, int height) {
//        MultiTexture multiTexture = new MultiTexture();
//        multiTexture.addTexture(new MyTexture(new TextureDataAdapter(width, height, Pixmap.Format.Alpha)), "u_texture");
//        multiTexture.addTexture(new MyTexture(new TextureDataAdapter(width/2, height/2, Pixmap.Format.LuminanceAlpha)), "u_UV");
//        return multiTexture;
//    }
//
//    int getFrameBytes() {
//        if (cameraObject==null || cameraObject.getCamera()==null) return -1;
//        Camera.Parameters parameters = cameraObject.getCamera().getParameters();
//        return parameters.getPreviewSize().width * parameters.getPreviewSize().height * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()) / 8;
//    }
//    void initCameraSurfaceView() {
//        SurfaceView surfaceView = new SurfaceView(activity);
//        this.view = surfaceView;
//        ViewGroup cc_layout = (ViewGroup)activity.findViewById(R.id.cc_layout);
//        cc_layout.setVisibility(View.INVISIBLE);
//        System.out.println("############################################### CC_LAYOUT = "+ cc_layout);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        surfaceView.setLayoutParams(params);
//        cameraObject = new CameraController(activity,  surfaceView);
//        cc_layout.addView(surfaceView, params);
//        cameraObject.setOnPreviewReady(new CameraController.PreviewReadyCallback() {
//            @Override
//            public void onPreviewReady() {
//                Camera camera = cameraObject.getCamera();
//                Camera.Parameters parameters = camera.getParameters();
//                Camera.Size size = parameters.getPreviewSize();
//                cameraDataQueue = new CameraDataQueue(getFrameBytes());
//                onCameraConfigured(size.width, size.height, parameters.getPreviewFormat(), camera);
//                cameraObject.setPreviewCallback(new PreviewCallback());
//                cameraObject.getCamera().addCallbackBuffer(cameraDataQueue.allocBuffer());
//            }
//        });
//        cameraObject.setup();
////        activity.findViewById(R.id.gl_game).bringToFront();
//    }
//
//    void startRequestCameraPreview() {
//        run();
//    }
//    void doInitCamera() {
//        initCameraSurfaceView();
//    }
//    @Override
//    public void run() {
//        doInitCamera();
//    }
//
//    void destroyCameraSurfaceView() {
//        if (cameraObject!=null) {
//            cameraObject.stop();
//            cameraObject = null;
//        }
//        ViewGroup cc_layout = (ViewGroup)activity.findViewById(R.id.cc_layout);
//        if (view!=null) {
//            cc_layout.removeViewInLayout(view);
//            view = null;
//        }
//        cc_layout.setVisibility(View.INVISIBLE);
//
//        final Graphics g = Gdx.graphics;
//        AndroidGraphics ag = ((AndroidGraphics) g);
//        View view = ag.getView();
//        System.out.println("view class= '"+view.getClass().getName()+", is GLSurfaceView= '"+(view instanceof GLSurfaceView));
//        GLSurfaceView glSurfaceView = ((GLSurfaceView) view);
////                            glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        glSurfaceView.getHolder().setFormat(PixelFormat.RGB_565);
//    }
//
//    @Override
//    public void onPause() {
//        destroyCameraSurfaceView();
//    }
//    @Override
//    public void onResume() {
//        startRequestCameraPreview();
//        delay = .4f;
//    }
//
//    @Override
//    public void show() {
//        super.show();
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                startRequestCameraPreview();
//            }
//        });
//        activity.registerCallback(this);
//    }
//
//    @Override
//    public void hide() {
//        super.hide();
//        activity.unregisterCallback(this);
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                destroyCameraSurfaceView();
//            }
//        });
//    }
//
//        @Override
//    public void render(float delta) {
//        if (delay>0) {
//            delay-=delta;
//            stage.act(delta);
//            return;
//        }
//        super.render(delta);
//        if (InputUtils.isBackKeyOK()) {
//            game.setScreen(new MainMenuScreen(game));
//        }
//    }
//
//    @Override
//    public void dispose() {
//        super.dispose();
//        if (this.multiTexture!=null)
//            this.multiTexture.dispose();
//        if (this.background!=null)
//            this.background.dispose();
//    }
//
//    String model_tag = null;
//    CameraDataQueue cameraDataQueue;
//    android.fphoenix.face.MainActivity activity;
//    MultiTexture multiTexture;
//    int width;
//    int height;
//    View view;
//    MyBaseButton btn;
//    CameraController cameraObject;
//    Background background;
//    float delay = -1;
//
//    TfPipe imagePipe;
//
//    ClassifierWorker classifierWorker;
//}
