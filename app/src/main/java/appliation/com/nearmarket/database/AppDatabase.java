package appliation.com.nearmarket.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CartDatabase.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartDao cartDao();
}
