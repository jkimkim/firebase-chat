package tech.jkimtech.firebase_chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.IpSecAlgorithm;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    static ArrayList<MusicFiles>musicFiles;
    static boolean shuffleBoolean = false, repeatBoolean = false;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        actionBar = getSupportActionBar();
        actionBar.setTitle("All Songs");

        BottomNavigationView navigationView = findViewById(R.id.mnavigatiom_view);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        //mProfileTv = findViewById(R.id.profileTv);

        actionBar.setTitle("All Songs");
        Songs_Fragment fragment5 = new Songs_Fragment();
        FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
        ft5.replace(R.id.content, fragment5, "");
        ft5.commit();

        permission();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_musicfiles:

                            actionBar.setTitle("All Songs");
                            Songs_Fragment fragment5 = new Songs_Fragment();
                            FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                            ft5.replace(R.id.content, fragment5, "");
                            ft5.commit();
                            return true;

                        case R.id.nav_albums:
                            actionBar.setTitle("Profile");
                            AlbumFragment fragment6 = new AlbumFragment();
                            FragmentTransaction ft6 = getSupportFragmentManager().beginTransaction();
                            ft6.replace(R.id.content, fragment6, "");
                            ft6.commit();
                            return true;

                        case R.id.nav_artists:
                            actionBar.setTitle("Artists");
                            ArtistsFragment fragment7 = new ArtistsFragment();
                            FragmentTransaction ft7 = getSupportFragmentManager().beginTransaction();
                            ft7.replace(R.id.content, fragment7, "");
                            ft7.commit();
                            return true;

                        case R.id.nav_playlist:
                            actionBar.setTitle("Playlists");
                            Playlists fragment8 = new Playlists();
                            FragmentTransaction ft8 = getSupportFragmentManager().beginTransaction();
                            ft8.replace(R.id.content, fragment8, "");
                            ft8.commit();
                            return true;
                    }

                    return false;
                }
            };

    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE);

        }
        else {
            musicFiles = getAllAudio(this);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Do anything you want permission related;
                musicFiles = getAllAudio(this);

            }
            else {
                ActivityCompat.requestPermissions(MainActivity2.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CODE);
            }
        }
    }


    public static ArrayList<MusicFiles> getAllAudio(Context context){
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);


                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);
                // take log.e to check;
                Log.e("path :"+ path, "Album :" + album);
                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }
}