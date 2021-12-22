//package com.codingwithmitch.googlemaps2018.ui;
//
//import android.Manifest;
//import android.app.ActivityManager;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.InputType;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.codingwithmitch.googlemaps2018.R;
//import com.codingwithmitch.googlemaps2018.UserClient;
//import com.codingwithmitch.googlemaps2018.adapters.ChatroomRecyclerAdapter;
//import com.codingwithmitch.googlemaps2018.models.Chatroom;
//import com.codingwithmitch.googlemaps2018.models.User;
//import com.codingwithmitch.googlemaps2018.models.UserLocation;
//import com.codingwithmitch.googlemaps2018.services.LocationService;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.FirebaseFirestoreSettings;
//import com.google.firebase.firestore.GeoPoint;
//import com.google.firebase.firestore.ListenerRegistration;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.annotation.Nullable;
//
//import static com.codingwithmitch.googlemaps2018.Constants.ERROR_DIALOG_REQUEST;
//import static com.codingwithmitch.googlemaps2018.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
//import static com.codingwithmitch.googlemaps2018.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;
//import static com.niloo.trackmylocation.Constants.ERROR_DIALOG_REQUEST;
//import static com.niloo.trackmylocation.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
//import static com.niloo.trackmylocation.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;
//
//
//public class MainActivity extends AppCompatActivity implements
//        View.OnClickListener,
//        ChatroomRecyclerAdapter.ChatroomRecyclerClickListener {
//
//    private static final String TAG = "MainActivity";
//
//    //widgets
//    private ProgressBar mProgressBar;
//
//    //vars
//    private ArrayList<Chatroom> mChatrooms = new ArrayList<>();
//    private Set<String> mChatroomIds = new HashSet<>();
//    private ChatroomRecyclerAdapter mChatroomRecyclerAdapter;
//    private RecyclerView mChatroomRecyclerView;
//    private ListenerRegistration mChatroomEventListener;
//    private FirebaseFirestore mDb;
//    private boolean mLocationPermissionGranted = false;
//    private FusedLocationProviderClient mFusedLocationClient;
//    private UserLocation mUserLocation;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mProgressBar = findViewById(R.id.progressBar);
//        mChatroomRecyclerView = findViewById(R.id.chatrooms_recycler_view);
//
//        findViewById(R.id.fab_create_chatroom).setOnClickListener(this);
//
//        mDb = FirebaseFirestore.getInstance();
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        initSupportActionBar();
//        initChatroomRecyclerView();
//    }
//
//
//    private void startLocationService() {
//        if (!isLocationServiceRunning()) {
//            Intent serviceIntent = new Intent(this, LocationService.class);
////        this.startService(serviceIntent);
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//
//                MainActivity.this.startForegroundService(serviceIntent);
//            } else {
//                startService(serviceIntent);
//            }
//        }
//    }
//
//    private boolean isLocationServiceRunning() {
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if ("com.codingwithmitch.googledirectionstest.services.LocationService".equals(service.service.getClassName())) {
//                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
//                return true;
//            }
//        }
//        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
//        return false;
//    }
//
//
//    private boolean checkMapServices() {
//        if (isServicesOK()) {
//            if (isMapsEnabled()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void buildAlertMessageNoGps() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    public boolean isMapsEnabled() {
//        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//            return false;
//        }
//        return true;
//    }
//
//    private void getLocationPermission() {
//        /*
//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.
//         */
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }
//
//    public boolean isServicesOK() {
//        Log.d(TAG, "isServicesOK: checking google services version");
//
//        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
//
//        if (available == ConnectionResult.SUCCESS) {
//            //everything is fine and the user can make map requests
//            Log.d(TAG, "isServicesOK: Google Play Services is working");
//            return true;
//        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
//            //an error occured but we can resolve it
//            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
//            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
//            dialog.show();
//        } else {
//            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
//        }
//        return false;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        mLocationPermissionGranted = false;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLocationPermissionGranted = true;
//                } else {
//
//                    getLocationPermission();
//                }
//            }
//
//        }
//    }
//
//
//
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(mChatroomEventListener != null){
//            mChatroomEventListener.remove();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(checkMapServices()){
//            if(mLocationPermissionGranted){
//                getChatrooms();
//                getUserDetails();
//            }
//            else{
//                getLocationPermission();
//            }
//        }
//    }
//
//
//
//    private void showDialog(){
//        mProgressBar.setVisibility(View.VISIBLE);
//    }
//
//    private void hideDialog(){
//        mProgressBar.setVisibility(View.GONE);
//    }
//
//
//}
//
//
//
//
//
//
//
//
//package com.codingwithmitch.googlemaps2018.services;
//
//
//        import android.Manifest;
//        import android.app.Notification;
//        import android.app.NotificationChannel;
//        import android.app.NotificationManager;
//        import android.app.Service;
//        import android.content.Context;
//        import android.content.Intent;
//        import android.content.pm.PackageManager;
//        import android.location.Location;
//
//        import android.os.Build;
//        import android.os.IBinder;
//        import android.os.Looper;
//        import android.support.annotation.NonNull;
//        import android.support.annotation.Nullable;
//        import android.support.v4.app.ActivityCompat;
//        import android.support.v4.app.NotificationCompat;
//        import android.util.Log;
//
//        import com.codingwithmitch.googlemaps2018.R;
//        import com.codingwithmitch.googlemaps2018.UserClient;
//        import com.codingwithmitch.googlemaps2018.models.User;
//        import com.codingwithmitch.googlemaps2018.models.UserLocation;
//        import com.google.android.gms.location.FusedLocationProviderClient;
//        import com.google.android.gms.location.LocationCallback;
//        import com.google.android.gms.location.LocationRequest;
//        import com.google.android.gms.location.LocationResult;
//        import com.google.android.gms.location.LocationServices;
//        import com.google.android.gms.tasks.OnCompleteListener;
//        import com.google.android.gms.tasks.Task;
//        import com.google.firebase.auth.FirebaseAuth;
//        import com.google.firebase.firestore.DocumentReference;
//        import com.google.firebase.firestore.FirebaseFirestore;
//        import com.google.firebase.firestore.GeoPoint;
//
//
//public class LocationService extends Service {
//
//    private static final String TAG = "LocationService";
//
//    private FusedLocationProviderClient mFusedLocationClient;
//    private final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
//    private final static long FASTEST_INTERVAL = 2000; /* 2 sec */
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        if (Build.VERSION.SDK_INT >= 26) {
//            String CHANNEL_ID = "my_channel_01";
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
//                    "My Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setContentTitle("")
//                    .setContentText("").build();
//
//            startForeground(1, notification);
//        }
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand: called.");
//        getLocation();
//        return START_NOT_STICKY;
//    }
//
//    private void getLocation() {
//
//        // ---------------------------------- LocationRequest ------------------------------------
//        // Create the location request to start receiving updates
//        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
//        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequestHighAccuracy.setInterval(UPDATE_INTERVAL);
//        mLocationRequestHighAccuracy.setFastestInterval(FASTEST_INTERVAL);
//
//
//        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d(TAG, "getLocation: stopping the location service.");
//            stopSelf();
//            return;
//        }
//        Log.d(TAG, "getLocation: getting location information.");
//        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, new LocationCallback() {
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//
//                        Log.d(TAG, "onLocationResult: got location result.");
//
//                        Location location = locationResult.getLastLocation();
//
//                        if (location != null) {
//                            User user = ((UserClient)(getApplicationContext())).getUser();
//                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//                            UserLocation userLocation = new UserLocation(user, geoPoint, null);
//                            saveUserLocation(userLocation);
//                        }
//                    }
//                },
//                Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
//    }
//
//    private void saveUserLocation(final UserLocation userLocation){
//
//        try{
//            DocumentReference locationRef = FirebaseFirestore.getInstance()
//                    .collection(getString(R.string.collection_user_locations))
//                    .document(FirebaseAuth.getInstance().getUid());
//
//            locationRef.set(userLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        Log.d(TAG, "onComplete: \ninserted user location into database." +
//                                "\n latitude: " + userLocation.getGeo_point().getLatitude() +
//                                "\n longitude: " + userLocation.getGeo_point().getLongitude());
//                    }
//                }
//            });
//        }catch (NullPointerException e){
//            Log.e(TAG, "saveUserLocation: User instance is null, stopping location service.");
//            Log.e(TAG, "saveUserLocation: NullPointerException: "  + e.getMessage() );
//            stopSelf();
//        }
//
//    }
//}