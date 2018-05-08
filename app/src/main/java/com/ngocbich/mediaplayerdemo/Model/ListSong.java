package com.ngocbich.mediaplayerdemo.Model;

/**
 * Created by Ngoc Bich on 5/3/2018.
 */

public class ListSong {
    private String Artist;
    private String Category;
    private  String IdSong;
    private String Name;

    public ListSong() {
    }

    public ListSong(String artist, String category, String idSong, String name) {
        Artist = artist;
        Category = category;
        IdSong = idSong;
        Name = name;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getIdSong() {
        return IdSong;
    }

    public void setIdSong(String idSong) {
        IdSong = idSong;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
