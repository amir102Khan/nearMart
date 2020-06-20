package appliation.com.nearmarket.Model;

import java.io.Serializable;
import java.util.List;

import appliation.com.nearmarket.database.CartDatabase;

public class PlaceOrderModel implements Serializable {
    private String userName;
    private String userPhone;
    private String userAddress;
    private String totalBill;
    private String amountToPay;
    private String deliveryCharge;
    private List<CartDatabase> orders;
    private String orderTime;
    private int orderStatus;
    private String orderId;
    private String delevryTimeSlot;

    private String paymentType;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public PlaceOrderModel(){

    }

    public PlaceOrderModel(String userName,String userPhone,String userAddress,String totalBill,
                           String amountToPay,String deliveryCharge,String orderTime,
                           List<CartDatabase> orders,int orderStatus,String orderId,String delevryTimeSlot,String paymentType){
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.totalBill = totalBill;
        this.amountToPay = amountToPay;
        this.deliveryCharge = deliveryCharge;
        this.orderTime = orderTime;
        this.orders = orders;
        this.orderStatus = orderStatus;
        this.orderId = orderId;
        this.delevryTimeSlot = delevryTimeSlot;
        this.paymentType = paymentType;
    }

    public String getDelevryTimeSlot() {
        return delevryTimeSlot;
    }

    public void setDelevryTimeSlot(String delevryTimeSlot) {
        this.delevryTimeSlot = delevryTimeSlot;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(String amountToPay) {
        this.amountToPay = amountToPay;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public List<CartDatabase> getOrders() {
        return orders;
    }

    public void setOrders(List<CartDatabase> orders) {
        this.orders = orders;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
