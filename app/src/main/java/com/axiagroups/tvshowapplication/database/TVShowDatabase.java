package com.axiagroups.tvshowapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.axiagroups.tvshowapplication.dao.TVShowDao;
import com.axiagroups.tvshowapplication.models.TVShow;

/**
 * Created by Ahsan Habib on 5/5/2024.
 * shehanuk.ahsan@gmail.com
 */
@Database(entities = TVShow.class, version = 1, exportSchema = false)
public abstract class TVShowDatabase extends RoomDatabase {
    private static TVShowDatabase tvShowDatabase;
    public static synchronized TVShowDatabase getTvShowDatabase(Context context) {
        if (tvShowDatabase == null) {
            tvShowDatabase = Room.databaseBuilder(
                    context,
                    TVShowDatabase.class,
                    "tv_show_db"
            ).build();
        }
        return tvShowDatabase;
    }
    public abstract TVShowDao tvShowDao();
}
