package appliation.com.nearmarket.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM cartdatabase")
    List<CartDatabase> getAll();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(CartDatabase task);

    @Delete
    void delete(CartDatabase task);

    @Update
    void update(CartDatabase task);

    @Query("DELETE FROM cartdatabase")
    void deleteAll();


}
