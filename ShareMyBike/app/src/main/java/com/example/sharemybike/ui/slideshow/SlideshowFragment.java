package com.example.sharemybike.ui.slideshow;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sharemybike.Bike;
import com.example.sharemybike.R;
import com.example.sharemybike.databinding.FragmentSlideshowBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private EditText txtLongitude;
    private EditText txtLatitude;
    private EditText txtLocation;
    private EditText txtCity;
    private EditText txtDescription;
    private ImageView imgSofa;
    private Button btnPhoto;
    private Button btnAddMyBike;

    private static final int PICK_IMAGE_REQUEST = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        txtLongitude = root.findViewById(R.id.txtLongitude);
        txtLatitude = root.findViewById(R.id.txtLatitude);
        txtLocation = root.findViewById(R.id.txtLocation);
        txtCity = root.findViewById(R.id.txtCity);
        txtDescription = root.findViewById(R.id.txtDescription);
        imgSofa = root.findViewById(R.id.imgSofa);
        btnPhoto = root.findViewById(R.id.btnPhoto);
        btnAddMyBike = root.findViewById(R.id.btnAddMyBike);

        btnAddMyBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddBikeClick();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");


                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateCoordinates(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        requestLocationUpdates();

        return root;
    }

    private void requestLocationUpdates() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {

            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                requestLocationUpdates();
            } else {

                Toast.makeText(requireContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();

            try {

                InputStream imageStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


                imgSofa.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void onAddBikeClick() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("bikes_list");


        String urlImagen = "gs://sharemybike-e3567.appspot.com/bike1.jpg";
        String owner = "Cristiano";
        String description = txtDescription.getText().toString();
        String city = txtCity.getText().toString();

        String longitude = txtLongitude.getText().toString();
        String latitude = txtLatitude.getText().toString();
        String email = "cristiano@gmail.com";


        Bike miBicicleta = new Bike(urlImagen, owner, description, city, Double.valueOf(longitude), Double.valueOf(latitude), "", null, email);


        String key = mDatabase.push().getKey();


        mDatabase.child(key).setValue(miBicicleta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Bicicleta añadida con éxito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al añadir la bicicleta", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void updateCoordinates(double latitude, double longitude) {

        txtLatitude.setText(String.valueOf(latitude));
        txtLongitude.setText(String.valueOf(longitude));


        if (locationManager != null) {
            try {
                locationManager.removeUpdates(locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (locationManager != null) {
            try {
                locationManager.removeUpdates(locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }
}
