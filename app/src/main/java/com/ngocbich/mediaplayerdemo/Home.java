package com.ngocbich.mediaplayerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngocbich.mediaplayerdemo.Common.Common;
import com.ngocbich.mediaplayerdemo.Interface.ItemClickListener;
import com.ngocbich.mediaplayerdemo.Model.ListSong;
import com.ngocbich.mediaplayerdemo.ViewHolder.ListViewHolder;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference listsong;

    TextView username;

    RecyclerView recyclerList;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<ListSong,ListViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("List");
        setSupportActionBar(toolbar);

        //init firebase
        database=FirebaseDatabase.getInstance();
        listsong=database.getReference("ListSong");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name for user
        View headerView=navigationView.getHeaderView(0);
        username=headerView.findViewById(R.id.userName);
        username.setText(Common.currentUser.getName());


        //load list song
        recyclerList=(RecyclerView)findViewById(R.id.recyclerList);
        recyclerList.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);

        recyclerList.setLayoutManager(layoutManager);
         loadList();
    }

    private void loadList() {
        adapter=new FirebaseRecyclerAdapter<ListSong, ListViewHolder>(ListSong.class,R.layout.listsong_item,ListViewHolder.class,listsong) {
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ListSong model, int position) {
                viewHolder.listsongname.setText(model.getName());
                viewHolder.listsongartist.setText(model.getArtist());

                final ListSong clickItem=model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                       // Toast.makeText(Home.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();

                        //start new activity to play music
                        Intent playmusic=new Intent(Home.this,PlayMusic.class);
                        playmusic.putExtra("SongId",adapter.getRef(position).getKey());//send SongId to new Activity
                        startActivity(playmusic);
                    }
                });
            }
        };
        recyclerList.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.nav_list){

        }else if(id==R.id.nav_playlist){

        }else if(id==R.id.nav_signout){
            Intent signin=new Intent(Home.this,SignIn.class);
            signin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signin);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
