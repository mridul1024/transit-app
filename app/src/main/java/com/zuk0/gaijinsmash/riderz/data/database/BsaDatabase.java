package com.zuk0.gaijinsmash.riderz.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.model.bsa_response.BsaXmlResponse;

@Database(entities = {BsaXmlResponse.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class BsaDatabase extends RoomDatabase {

    private static BsaDatabase INSTANCE;
    public abstract BsaDao getBsaDAO();

    public static BsaDatabase getRoomDb(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BsaDatabase.class, "advisories")
                    .addMigrations(MIGRATION)
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        if(INSTANCE.isOpen()) {
            INSTANCE.close();
        }
        INSTANCE = null;
    }

    // Edit this to create a new migration for database - and use ".addMigrations(example)
    private static final Migration MIGRATION = new Migration(1,2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE stations ADD COLUMN last_update INTEGER");
        }
    };

}
