package com.demo.skyros.vo;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class CurrencyReportVO {

    private List<CurrencyExchangeVO> currencyExchangeVO;
    private Date from;
    private Date to;

    public List<CurrencyExchangeVO> getCurrencyExchangeVO() {
        return currencyExchangeVO;
    }

    public void setCurrencyExchangeVO(List<CurrencyExchangeVO> currencyExchangeVO) {
        this.currencyExchangeVO = currencyExchangeVO;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
