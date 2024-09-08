package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public interface ReportService
{
    /**
     * 根据日期区间统计营业额数据
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnover(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end);

    /**
     * 根据日期区间统计订单数据
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end);


    /**
     * 根据日期区间统计销量top10菜品
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end);
}
