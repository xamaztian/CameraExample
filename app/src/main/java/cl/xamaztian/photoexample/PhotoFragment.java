package cl.xamaztian.photoexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;

import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class PhotoFragment extends Fragment {

    private MagicalPermissions magicalPermissions;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 70;
    private MagicalCamera magicalCamera;
private ImageView imageView;


    public PhotoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.photoIV);

        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(getActivity(), RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        magicalCamera.resultPhoto(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);

            Bitmap photo = magicalCamera.getPhoto();

            String path = magicalCamera.savePhotoInMemoryDevice(photo, "myPhotoName", "myDirectoryName", MagicalCamera.JPEG, true);

            if (path != null) {

                imageView.setImageBitmap(photo);

                Toast.makeText(getContext(), "The photo is save in device, please check this path: " + path, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Sorry your photo don't write in device", Toast.LENGTH_SHORT).show();
        }
    }

    public void takePhoto() {
        magicalCamera.takeFragmentPhoto(this);
        //magicalCamera.selectedFragmentPicture(FragmentSample.this, "My Header Example");
    }
}
