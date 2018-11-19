package com.tz.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 服务端返回到前端的高复用的对象
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse <T>{
    private int status;//返回前端的状态码
    private T data;//返回前端的数据
    private String msg;//封装错误信息，status不等于0的情况

    private ServerResponse() {

    }
    private ServerResponse(int status) {
        this.status = status;

    }
    private ServerResponse(int status,String msg) {
        this.status = status;

    }
    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;

    }
    private ServerResponse(int status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    /**
     *成功获取数据回调
     */

    public  static ServerResponse serverResponseBySuccess(){
        return  new ServerResponse(ResponseCode.SUCCESS);
    }
    public  static <T> ServerResponse serverResponseBySuccess(T data){
        return  new ServerResponse(ResponseCode.SUCCESS,data);
    }
    public  static <T> ServerResponse serverResponseBySuccess(T data,String meg){
        return  new ServerResponse(ResponseCode.SUCCESS,data,meg);
    }
    /**
     *失败时回调
     */

    public  static ServerResponse serverResponseByError(){
        return  new ServerResponse(ResponseCode.ERROR);
    }
    public  static ServerResponse serverResponseByError(int status){
        return  new ServerResponse(status);
    }
    public  static ServerResponse serverResponseByError(int status,String msg){
        return  new ServerResponse(status,msg);
    }
    public  static ServerResponse serverResponseByError(String msg){
        return  new ServerResponse(ResponseCode.ERROR,msg);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    /**
     * 判断接口是否正确返回
     * status==0
     */
    @JsonIgnore
    public  boolean  isSuccess(){
        return  this.status==ResponseCode.SUCCESS;
    }
}
