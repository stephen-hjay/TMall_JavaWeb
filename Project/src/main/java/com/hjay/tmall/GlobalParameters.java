package com.hjay.tmall;

public class GlobalParameters {
    // use internal static class for configuration
    public static class OrderStatus{
        public static final String WAITPAY = "waitPay";
        public static final String WAITDELIVERY = "waitDelivery";
        public static final String WAITCONFIRM = "waitConfirm";
        public static final String WAITREVIEW = "waitReview";
        public static final String FINISH = "finish";
        public static final String DELETED = "delete";

    }

    public static class DBUtilsParameters{
        // database ip
        public static final String ip = "127.0.0.1";
        // database port number
        public static final int port=3306;
        //dbname
        public static final String database="tmall1";
        // encoding
        public static final String  encoding_format="UTF-8";
        public static final String loginName="root";
        public static final String   password="hjay1996";
        public static final String driverClassName = "com.mysql.jdbc.Driver";
        public static final String url = "jdbc:mysql//ip:port/"+database+"?CharacterEncoding=UTF-8";
    }

    // useless, properties is more useful for druid
    public static class DBConnUtilsParameters{
        public static final String driverClassName= "com.mysql.jdbc.Driver";
        public static final String  url= "jdbc:mysql://127.0.0.1:3306/tmall1?characterEncoding=UTF-8";
        public static final String username="root";
        public static final String password="hjay1996";
        public static final int initialSize= 5;
        public static final int maxActive=10;
        public static final int maxWait=3000;
        public static final String encoding="UTF-8";
    }

    // Message Information
    public static class ErrorMessage{
        public static final String INVALID_PRODUCT_ID = "Invalid Product ID";
        public static final String INVALID_CATEGORY_ID = "Invalid Category ID";
        public static final String INVALID_ORDER_ID = "Invalid Order ID";
        public static final String INVALID_ORDERITEM_ID = "Invalid Order Item ID";
        public static final String INVALID_PROPERTY_ID = "Invalid Property ID";
        public static final String INVALID_REVIEW_ID = "Invalid Review ID";
        public static final String INVALID_USER_ID = "Invalid User ID";
        public static final String INVALID_PRODUCT_IMAGE_ID = "Invalid Product Image ID";
        public static final String INVALID_REQUEST = "Invalid Request";
    }

    public static class ForeStageDisplay{
        public static final int PRODUCT_EACH_ROW = 8;


    }
}
