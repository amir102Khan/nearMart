package appliation.com.nearmarket.Model;

import android.widget.ProgressBar;

public class ProductModel {
    private String Image = "";
    private String Name = "";
    private String Price = "";
    private String productKey = "";
    private String mainCategory = "";
    private String minimumQty = "";
    private String subCategory = "";
    private String unit = "";
    private String desc = "";
    private int count = 0;
    private boolean isMoreThan1Unit = false;

    public boolean isMoreThan1Unit() {
        return isMoreThan1Unit;
    }

    public void setMoreThan1Unit(boolean moreThan1Unit) {
        isMoreThan1Unit = moreThan1Unit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getMinimumQty() {
        return minimumQty;
    }

    public void setMinimumQty(String minimumQty) {
        this.minimumQty = minimumQty;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ProductModel(String productImage,
                        String productName,
                        String prductPrice,
                        String prodcutKey,
                        String mainCategory,
                        String subCategory,
                        String unit,
                        String minimumQty,
                        String desc,
                        boolean isMoreThan1Unit){

        this.Image = productImage;
        this.Name = productName;
        this.Price = prductPrice;
        this.productKey = prodcutKey;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
        this.unit = unit;
        this.minimumQty = minimumQty;
        this.desc = desc;
        this.isMoreThan1Unit = isMoreThan1Unit;
    }

    public ProductModel(){

    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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
}
