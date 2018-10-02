package com.example.nizamuddinshamrat.flashlight;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Things to change in Layout
    ImageView lightImage;
    Button lightButton;

    // For checking values
    boolean lightOn = false;
    boolean hasFlash = false;

    //Use camera HardWare
    android.hardware.Camera camera;
    android.hardware.Camera.Parameters parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize id
        lightButton = findViewById(R.id.light_button);
        lightImage = findViewById(R.id.lightImage);

        hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (hasFlash){
            requestCameraForPermission();
            try {
                camera = android.hardware.Camera.open();
                parameters = camera.getParameters();
            } catch (Exception e) {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
// On create Finnish

    // set drawable file in layout as user response
    void setDrawableAsUserResponse() {

        if (lightOn) {
            lightButton.setBackgroundResource(R.drawable.turn_on_button_backround);
            lightImage.setBackgroundResource(R.drawable.tun_on_image);
        } else {
            lightButton.setBackgroundResource(R.drawable.turn_off_button_backround);
            lightImage.setBackgroundResource(R.drawable.turn_off_image);
        }
    }
//On click flash On/Of button
    public void flashLightAction(View view) {
        if (hasFlash) {
            if (lightOn) {

                parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
                lightOn = false;
            } else {
                // Flash Off
                parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
                lightOn = true;
                Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
            }

            setDrawableAsUserResponse();
        } else {
            //hasn't Flash Light
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Your Mobile Doesn't Have Flash Light")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDialog.create().show();
        }


    }

    //Request For App needed Permission
    void requestCameraForPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else {

            // Permission Denied
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Give Permission")
                    .setMessage("You have to give camera permission to use flash light")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestCameraForPermission();
                        }
                    });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        }
    }
}
