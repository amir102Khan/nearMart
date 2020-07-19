package appliation.com.nearmarket.interfaces;

public interface Constants {

    String ISLOGIN = "isLogin";
    String ISADMIN = "isAdmin";
    String CONSUMER = "consumer";
    String PROVIDER = "provider";
    String USER_TYPE = "";
    String USER_ID = "userId";

    String ADMIN_PHONE = "7007230145";
    //String ADMIN_PHONE = "9450246245";
    // grocery list types
    String GROCERY_TYPE = "groceryType";
    String BRANDED_FOODS = "Branded Food";
    String PERSONAL_CARE = "Personal Care";
    String HEALTH = "Health";
    String HOME_CARE = "Home Care";

    //vegetable list types
    String VEGY_TYPE = "vegyType";
    String FRESH_FRUITS = "Fresh Fruits";
    String FRESH_VEGGIES = "Fresh Veggies";

    //gifts list types
    String CUSTOMIZED_FRAMES = "Customized Frames";
    String CUSTOMIZED_ITEMS = "Customize Gift Item";
    String CUSTOMIZED_TSHIRTS = "Customized T-Shirts";
    String CUSTOMIZED_CHOCLATE = "Customized Chocolates";

    // additional services items

    String ADDITIONAL_TYPE = "Additional Type";
    String PHOTOGRAPHY = "Photography";
    String EVENT_MANAGEMENT = "Event Management";


    //counter selector
    int ADD = 1;
    int SUB = 0;

    String dummy_image = "https://i.picsum.photos/id/488/200/300.jpg";

    //firebase tables
    String PRODUCTS_TYPE = "Products Type";
    String USER = "User";
    String BUCKET = "Bucket";

    String RUPEES = "₹";

    // main category
    String MAIN_CATEGORY = "mainCategory";
    String GROCERY = "Grocery";
    String VEGETABLES = "Veggies & Fruits";
    String GIFTS = "Gifts";
    String ADDITIONAL_SERVICE = "Additional Service";

    // intents
    String MOVE_TO = "moveTo";
    String basket = "basket";

    //all fragments
    int HOME = 0;
    //int TYPE = 1;
    int PROFILE = 2;
    int BASKET = 1;

    String CARTS = "carts";



    String STORAGE_PATH_UPLOADS = "uploads/";
    String DATABASE_PATH_UPLOADS = "uploads";

    String BANNER = "banner";
    String ORDER_PLACED = "orders";

    String APP_STATUS = "App status";
    String ACTIVE = "Active";
    String BLOCKED = "Blocked";


    String DATE = "yyyyMMdd_HHmmss";
    String DATE2 = "yyyy-MM-dd";
    String DATE3 = "dd MMM yyyy, h:mm a";

    int PENDING = 0;
    int ACCEPTED = 1;
    int ON_THE_WAY = 2;
    int REJECTED = 3;
    int DELIVERED = 4;

    String ONLINE_PAYMENT = "Payed online";
    String COD = "Cash on delivery";

    String NON_DELIVERY_TIME = "Delivery is only allowed between 10 AM to 8 PM";

    String IS_FROM_SIGN_UP = "Is from sign up";
}
