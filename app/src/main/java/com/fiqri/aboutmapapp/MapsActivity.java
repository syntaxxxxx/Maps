package com.fiqri.aboutmapapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE2 = 2;

    @BindView(R.id.locationAwal)
    EditText locationAwal;
    @BindView(R.id.locationAkhir)
    EditText locationAkhir;
    @BindView(R.id.textJarak)
    TextView textJarak;
    @BindView(R.id.textWaktu)
    TextView textWaktu;
    private GoogleMap mMap;

    Double latawal,lonawal,latakhir,lonakhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Jakarta and move the camera
        LatLng jakarta = new LatLng(-6.195221, 106.7947115);
        mMap.addMarker(new MarkerOptions().position(jakarta).title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        mMap.addMarker(new MarkerOptions()
                .position(jakarta).icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_delete)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta));

        // Pertama adalah buat auto zoom pada maps seperti gambar dibawah ini
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 16));

        // Aktifkan setting maps such as indoor zoom or outdoor zoom
        // Like is current location
        // Like is compas

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // TODO CONSIDER CALLING

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                        12);
            }

            return;
        }

        mMap.setPadding(60, 225,16,157);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.clear();
    }

    @OnClick({R.id.locationAwal, R.id.locationAkhir})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.locationAwal:

                //TODO 1 : SEARCH LOCATION

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
            case R.id.locationAkhir:

                //TODO 2 : HASIL LOCATION

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE2);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

                break;
        }
    }

        // TODO 3 RESPON SERACH LOCATION

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                // TODO 4 : GET COORDINAT RESULT NAME LOCATION
                LatLng place1 = place.getLatLng();
                latawal = place.getLatLng().latitude;
                lonawal = place.getLatLng().longitude;

                // TODO 5 : GET MARKER BASED COORDINAT
                mMap.addMarker(new MarkerOptions().position(place1).title(place.getId().toString()));

                // TODO 6: SET FOCUS CAMERA BIAR MARKER SELALU DI BAGIAN TENGAH
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place1,14));

                // TODO 7 : PINDAHIN NAMA LOKASI KE EDITTEX
                locationAwal.setText(place.getAddress().toString());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: HANDLE THE ERROR


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE2) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                // TODO 4 : GET COORDINAT RESULT NAME LOCATION
                LatLng place2 = place.getLatLng();
                latakhir = place.getLatLng().latitude;
                lonakhir = place.getLatLng().longitude;

                // TODO 5 : GET MARKER BASED COORDINAT
                mMap.addMarker(new MarkerOptions().position(place2).title(place.getAddress().toString()));

                // TODO 6: SET FOCUS CAMERA BIAR MARKER SELALU DI BAGIAN TENGAH
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place2,14));

                // TODO 7 : PINDAHIN NAMA LOKASI KE EDITTEX
                locationAkhir.setText(place.getAddress().toString());

                actionRoute();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void actionRoute() {

        String origin = latawal.toString()+","+lonawal.toString();
        String destination = latakhir.toString()+","+lonakhir.toString();
        String mode = "driving";

        ConfigRetrofit.service.ini_request_route(origin,destination,mode).enqueue(new Callback<ResponseRoute>() {
            @Override
            public void onResponse(Call<ResponseRoute> call, Response<ResponseRoute> response) {

                Log.d("response route :", response.message());

                if (response.isSuccessful()){
                    //get response from response route
                    //get semua object
                    ResponseRoute json = response.body();

                    //get json array
                    ArrayList<ResponseRoute.Object0> routearray = json.getRoutes();

                    //get object 0
                    ResponseRoute.Object0 object0 = routearray.get(0);

                    //get overview polyline
                    ResponseRoute.Object0.OverView overview = object0.getOverview_polyline();

                    //get points
                    String points = overview.getPoints();

                    String text = object0.getLegs().get(0).getDistance().getText();
                    Double value = object0.getLegs().get(0).getDistance().getValue();

                    Log.d("point", points);

                    new DirectionMapsV2(MapsActivity.this).gambarRoute(mMap,points);

                    //String point = response.body().getRoutes().get(0).getOverview_polyline().getPoints();
                }

            }

            @Override
            public void onFailure(Call<ResponseRoute> call, Throwable t) {

            }
        });
    }

    public void normal(View view)   {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void terrain(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void satelit(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void hybrid(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
