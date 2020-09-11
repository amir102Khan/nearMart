package appliation.com.nearmarket.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CartDatabase.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartDao cartDao();
}
