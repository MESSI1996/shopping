package com.tz.controller.common.intercepter.scheduler;

import com.tz.service.IOrderService;
import com.tz.utils.PropertiesUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 定时关闭订单
 */
@Component
public class CloseOrder {

    @Autowired
    IOrderService orderService;
    @Scheduled(cron = "0/1 * * * * *")
    public void closeOrder(){
        System.out.println("订单关闭");
        Integer hour=Integer.parseInt(PropertiesUtils.readKey("close.order.time"));
        String date= com.tz.utils.DateUtils.dateTostr(DateUtils.addHours(new Date(),-hour));
        orderService.closeOrder(date);
    }
}
