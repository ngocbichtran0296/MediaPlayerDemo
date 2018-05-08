package com.ngocbich.mediaplayerdemo;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngocbich.mediaplayerdemo.Model.ListSong;

import java.util.concurrent.TimeUnit;

import dyanamitechetan.vusikview.VusikView;

public class PlayMusic extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    private String songId = "";
    private FirebaseDatabase database;
    private DatabaseReference songs;

    private ImageButton play_pause, prev, next;
    private TextView name, artist, timer;

    private SeekBar seekBar;

    private VusikView vusikView;

    private MediaPlayer mediaPlayer;
    private int lenghtSong=0;
    private int realtime=0;

    private Handler handler = new Handler();

    private String id;
    private String path = "https://docs.google.com/uc?export=download&id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        //Firebase
        database = FirebaseDatabase.getInstance();
        songs = database.getReference("ListSong");

        //init view
        init();



        //get song id from intent
        if (getIntent() != null) {
            songId = getIntent().getStringExtra("SongId");
        }
        if (!songId.isEmpty()) {
            getDataSong(songId);
            play();
        }
    }

    private void play() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        play_pause.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String file_path=path+id;

                final ProgressDialog mDialog = new ProgressDialog(PlayMusic.this);

                AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        mDialog.setMessage("Please wait");
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            mediaPlayer.setDataSource(strings[0]);
                            mediaPlayer.prepare();
                        } catch (Exception e) {

                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        lenghtSong = mediaPlayer.getDuration();
                        realtime = lenghtSong;

                        timer.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(lenghtSong),
                                TimeUnit.MILLISECONDS.toSeconds(realtime) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtime))));
                        if (!mediaPlayer.isPlaying()) {

                            mediaPlayer.start();
                            play_pause.setImageResource(R.drawable.pausebt);
                          vusikView.start();
                        } else {
                            mediaPlayer.pause();

                            play_pause.setImageResource(R.drawable.playbt);
                            vusikView.stopNotesFall();
                        }

                        updateSeekbar();

                        mDialog.dismiss();
                    }
                };

                //get link from googledrive
                mp3Play.execute(file_path);
            //  vusikView.start();

            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaPlayer.isPlaying()) {
                    SeekBar seekBar = (SeekBar) v;
                    int playPosition = (lenghtSong / 100) * seekBar.getProgress();

                    mediaPlayer.seekTo(playPosition);

                }
                return false;
            }
        });


    }
Runnable updater;
    private void updateSeekbar() {
        seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / lenghtSong) * 100));
        if (mediaPlayer.isPlaying()) {
            updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekbar();
                   // realtime -= 1000;
                 //   timer.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realtime),
                   //         TimeUnit.MILLISECONDS.toSeconds(realtime) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtime))));
                }
            };
            handler.postDelayed(updater, 1000);
        }
    }

    private void init() {
        vusikView = (VusikView) findViewById(R.id.musicView);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(99);

        play_pause = findViewById(R.id.playPause);
        prev = findViewById(R.id.playPrev);
        next = findViewById(R.id.playNext);

        timer = findViewById(R.id.Timer);
        name = findViewById(R.id.Name);
        artist = findViewById(R.id.Artist);


    }


    private void getDataSong(String songId) {
        songs.child(songId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListSong song = dataSnapshot.getValue(ListSong.class);

                //set view
                artist.setText(song.getArtist());
                name.setText(song.getName());

                id = song.getIdSong();//get id of song to access googledrive
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        play_pause.setImageResource(R.drawable.playbt);
        vusikView.stopNotesFall();

    }

    @Override
    public void onBackPressed() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();

        }

        mediaPlayer.release();

        vusikView.stopNotesFall();

        handler.removeCallbacks(updater);
     //   seekBar.setProgress(0);


        super.onBackPressed();
    }
}

