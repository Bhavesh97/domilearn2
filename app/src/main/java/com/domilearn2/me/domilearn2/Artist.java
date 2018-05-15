package com.domilearn2.me.domilearn2;

/**
 * Created by hp on 15-05-2018.
 */
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Artist {
    private String artistId;
    private String artistName;


    public Artist(){

    }

    public Artist(String artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;

    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }


}