package com.zuk0.gaijinsmash.riderz.data.local.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.zuk0.gaijinsmash.riderz.data.local.converter.Converters;
import com.zuk0.gaijinsmash.riderz.data.local.dao.EtdDao;
import com.zuk0.gaijinsmash.riderz.data.local.entity.etd_response.EtdXmlResponse;

@Database(entities = {EtdXmlResponse.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class EtdDatabase extends RoomDatabase {

    private static EtdDatabase INSTANCE;
    public abstract EtdDao getEtdDao();

    public static EtdDatabase getRoomDb(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EtdDatabase.class, "estimates")
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
            database.execSQL("ALTER TABLE estimates ADD COLUMN last_update INTEGER");
        }
    };
}
