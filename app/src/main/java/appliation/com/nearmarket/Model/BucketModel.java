package appliation.com.nearmarket.Model;

public class BucketModel {
    private String Name = "";
    private String Price = "";
    private String productKey = "";
    private int count = 0;
    private String quantity = "";
    private String itemTotalPrice = "";

    public BucketModel(){

    }

    public BucketModel(String name,String price, String productKey,String quantity,int count,String itemTotalPrice ){
        this.Name = name;
        this.Price = price;
        this.productKey = productKey;
        this.quantity = quantity;
        this.count = count;
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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
}
