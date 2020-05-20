package appliation.com.nearmarket.Model;

import java.util.List;

public class AddToCartModel {
    public AddToCartModel(){

    }

    private List<BucketModel> carts ;
    private String cartId;
    public AddToCartModel(List<BucketModel> carts){
        this.carts = carts;
    }

    public List<BucketModel> getCarts() {
        return carts;
    }

    public void setCarts(List<BucketModel> carts) {
        this.carts = carts;
    }
}
