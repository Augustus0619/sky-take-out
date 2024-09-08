package com.sky.service;

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
}
