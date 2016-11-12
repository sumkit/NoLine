package summer.noline.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import summer.noline.CameraPreview;
import summer.noline.R;

public class ScannerActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private FrameLayout cameraPreview;

    private boolean codeScanned = false;

    //camera
    private Camera camera = null;
    private ImageScanner imageScanner = null;
    private boolean previewing = true;
    private Handler autoFocusHandler;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        actionBar = this.getSupportActionBar();
        actionBar.setTitle("Check In");
        cameraPreview = (FrameLayout) findViewById(R.id.cameraPreview);

        imageScanner = new ImageScanner();
        imageScanner.setConfig(0, Config.X_DENSITY, 3);
        imageScanner.setConfig(0, Config.Y_DENSITY, 3);

        autoFocusHandler = new Handler();

        if(checkCameraHardware(this)) {
            try {
                camera = Camera.open();
            } catch(Exception e) {
                Toast.makeText(this, "Camera is not available.", Toast.LENGTH_LONG).show();
                Log.e("ScannerActivity", "camera: "+e.toString());
            }
        }

        cameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(camera != null && codeScanned) {
                    codeScanned=false;
                    camera.setPreviewCallback(previewCallback);
                    camera.startPreview();
                    previewing = true;
                    camera.autoFocus(autoFocusCallback);
                }
            }
        });


        mPreview = new CameraPreview(this, camera, previewCallback, autoFocusCallback);
        cameraPreview.addView(mPreview);
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            Toast.makeText(this, "No camera on this device.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            autoFocusHandler.postDelayed(runAutoFocus, 9000);
        }
    };
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(bytes);

            int result = imageScanner.scanImage(barcode);

            if(result != 0) {
                previewing = false;
                camera.setPreviewCallback(null);
                camera.stopPreview();

                SymbolSet syms = imageScanner.getResults();
                for (Symbol sym : syms) {
                    String scanText = "barcode result " + sym.getData();
                    Toast.makeText(ScannerActivity.this, scanText, Toast.LENGTH_LONG).show();
                    Log.e("ScannerActivity", "scan text: "+scanText);
                    codeScanned = true;
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ticket:
                Intent toTix = new Intent(ScannerActivity.this, MainActivity.class);
                startActivity(toTix);
                return true;

            case R.id.qr_code:
                return true;
            case R.id.profile:
                Intent toPro = new Intent(ScannerActivity.this, ProfileActivity.class);
                startActivity(toPro);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    private Runnable runAutoFocus = new Runnable() {
        @Override
        public void run() {
            if(previewing) {
                camera.autoFocus(autoFocusCallback);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null) {
            previewing = false;
            camera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            camera.release();
            camera = null;
        }
    }
}
