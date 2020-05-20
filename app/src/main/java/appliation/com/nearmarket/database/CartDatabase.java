package appliation.com.nearmarket.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CartDatabase implements Serializable {
    @NonNull
    @PrimaryKey()
    private String productKey;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo (name = "price")
    private String Price;


    @ColumnInfo (name =  "count")
    private int count;

    @ColumnInfo (name = "quantity")
    private String quantity ;

    @ColumnInfo (name = "item_total_price")
    private String itemTotalPrice ;

    @ColumnInfo (name = "product_image")
    private String productImage;

    @ColumnInfo (name = "minimum_qty")
    private String qty;

    @ColumnInfo (name = "unit")
    private String unit;
    /**
     * getter and setters
     * @return
     */


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
