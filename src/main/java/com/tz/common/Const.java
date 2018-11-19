package com.tz.common;

public class Const {
    public static final String CURRENTUSER="currentuser";

    public static  final String TRADE_SUCCESS="TRADE_SUCCESS";


    public static  final String AUTOLOGINCTOKEN="autologintoken";

    public  enum ResponseCodeEnum{
        NEED_LOGIN(2,"需要登陆"),
        NO_PRIVILEGE(3,"无权限")
        ;
        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private  ResponseCodeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }


    public enum RoleEnum{
        ROLE_ADMIN(0,"管理员"),
        ROLE_CUSTOMER(1,"普通用户")
        ;
        private int code;
        private String desc;

       private RoleEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum ProductStatusEnm{
        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OUTLINE(2,"下架"),
        PRODUCT_DELETE(3,"删除")


        ;
        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private  ProductStatusEnm(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }




    public  enum CartCheckedEnum{
        PRODUCT_CHECKED(1,"商品已经勾选"),
        PRODUCT_UNCHECKED(2,"商品未被勾选")
        ;
        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private  CartCheckedEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }






    public  enum OrderStatusdEnum{
        ORDER_CANCELED(0,"已取消"),
        ORDER_UN_PAY(10,"未付款"),
        ORDER_PAYED(20,"已付款") ,
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_CLOSED(60,"交易关闭")
        ;
        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private  OrderStatusdEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static OrderStatusdEnum  codeOf(Integer code){

            for (OrderStatusdEnum orderStatusdEnum: values()){
                if (code==orderStatusdEnum.getCode()){
                    return  orderStatusdEnum;
                }
            }
                return  null;
        }
    }







    public  enum PaymentdEnum{
        ONLINE(1,"线上支付")

        ;
        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private  PaymentdEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }


        public static PaymentdEnum  codeOf(Integer code){

            for (PaymentdEnum paymentdEnum: values()){
                if (code==paymentdEnum.getCode()){
                    return  paymentdEnum;
                }
            }
            return  null;
        }


    }




    public  enum OrderSendEnum{
        SEND_SUCCESS(0,"发货成功"),
        SEND_FAIL(1,"发货失败")
        ;
        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private  OrderSendEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }






    }




    public  enum PaymentdPlatformEnum{
        ALIPAY(1,"支付宝")

        ;
        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private  PaymentdPlatformEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }



    }







}
