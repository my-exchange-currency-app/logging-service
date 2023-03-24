package com.demo.skyros.vo;


import java.util.Date;
import java.util.List;

public class CurrencyReportVO {

    private List<CurrencyExchangeVO> currencyExchangeVOList;
    private Date from;
    private Date to;

    public List<CurrencyExchangeVO> getCurrencyExchangeVOList() {
        return currencyExchangeVOList;
    }

    public void setCurrencyExchangeVOList(List<CurrencyExchangeVO> currencyExchangeVOList) {
        this.currencyExchangeVOList = currencyExchangeVOList;
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
