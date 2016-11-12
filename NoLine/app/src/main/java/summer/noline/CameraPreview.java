package summer.noline;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by sumkit on 11/7/16.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera camera = null;
    private Camera.PreviewCallback previewCallback = null;
    private Camera.AutoFocusCallback autoFocusCallback = null;

    private SurfaceHolder surfaceHolder;

    public CameraPreview(Context context, Camera camera,
                         android.hardware.Camera.PreviewCallback previewCallback,
                         android.hardware.Camera.AutoFocusCallback autoFocusCallback) {
        super(context);
        this.camera = camera;
        this.previewCallback = previewCallback;
        this.autoFocusCallback = autoFocusCallback;

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //this is deprecated, but required for Android versions < 3.0
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // tell the camera where to draw the preview
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            Log.e("CameraPreview", "surfaceCreated: "+e.toString());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(surfaceHolder.getSurface() == null) {
            //preview surface does not exist
            return;
        }

        try {
            camera.stopPreview();
        } catch(Exception e) {
            // tried to stop a non-existent preview
            Log.e("CameraPreview", "stop preview: "+e.toString());
        }

        try {
            // Hard code camera surface rotation 90 degs to match Activity view in portrait
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback(this.previewCallback);
            camera.startPreview();
            camera.autoFocus(autoFocusCallback);
        } catch (IOException e) {
            Log.e("CameraPreview", "set up camera: "+e.toString());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Camera preview released in activity, do nothing here
    }
}
