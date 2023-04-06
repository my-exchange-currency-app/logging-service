package com.demo.skyros.service;

import com.demo.skyros.entity.ClientRequestEntity;
import com.demo.skyros.entity.EntityAudit;
import com.demo.skyros.repo.ClientRequestRepo;
import com.demo.skyros.vo.CurrencyExchangeVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class ClientRequestService {

    private static final String REQUEST_ID = "REQUEST_ID";
    Logger logger = LoggerFactory.getLogger(ClientRequestService.class);
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();
    private ClientRequestRepo clientRequestRepo;

    public ClientRequestService(ClientRequestRepo clientRequestRepo) {
        this.clientRequestRepo = clientRequestRepo;
    }

    public void saveClientRequest(HttpServletRequest request, String tag) {
        String[] pathList = request.getRequestURI().split("/");
        ClientRequestEntity clientRequest = new ClientRequestEntity();
        clientRequest.setTag(tag);
        clientRequest.setRequestId(request.getHeader(REQUEST_ID));
        CurrencyExchangeVO currencyExchangeVO = prepareCurrencyExchangeVO(pathList);
        clientRequest.setRequestBody(gson.toJson(currencyExchangeVO));
        clientRequest.setAudit(prepareAudit());
        getClientRequestRepo().save(clientRequest);
    }

    public List<ClientRequestEntity> findClientRequestPerDate(Date from, Date to) {
        return getClientRequestRepo().findByAuditCreatedDateBetween(from, to);
    }

    public void saveClientRequest(String requestPath, String requestId) {
        if (null != requestId) {
            String[] pathList = requestPath.split("/");
            CurrencyExchangeVO currencyExchangeVO = prepareCurrencyExchangeVO(pathList);
            ClientRequestEntity clientRequest = new ClientRequestEntity();

            if (requestPath.contains("currency-conversion")) {
                clientRequest.setTag("conversion");
            }
            if (requestPath.contains("currency-exchange")) {
                clientRequest.setTag("exchange");
            }

            clientRequest.setRequestId(requestId);
            clientRequest.setRequestBody(gson.toJson(currencyExchangeVO));
            clientRequest.setAudit(prepareAudit());
            getClientRequestRepo().save(clientRequest);
        }
    }

    public void saveClientRequest(String requestId, CurrencyExchangeVO vo) {
        if (null != requestId) {
            ClientRequestEntity clientRequest = new ClientRequestEntity();
            clientRequest.setRequestId(requestId);
            clientRequest.setRequestBody(gson.toJson(vo));
            clientRequest.setAudit(prepareAudit());
            getClientRequestRepo().save(clientRequest);
        }
    }

    public String prepareRequestBody(HttpServletRequest request) {
        try {
            return IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            logger.error("failed to prepare requestBody", ex.getMessage());
        }
        return null;
    }

    public CurrencyExchangeVO prepareCurrencyExchangeVO(String[] pathList) {
        CurrencyExchangeVO currencyExchangeVO = new CurrencyExchangeVO();
        for (int i = 0; i < pathList.length; i++) {
            if (pathList[i].equals("from")) {
                String from = pathList[i + 1];
                currencyExchangeVO.setFrom(from);
            }
            if (pathList[i].equals("to")) {
                String to = pathList[i + 1];
                currencyExchangeVO.setTo(to);
            }
            if (pathList[i].equals("quantity")) {
                String quantity = pathList[i + 1];
                currencyExchangeVO.setQuantity(new BigDecimal(quantity));
            }
        }
        return currencyExchangeVO;
    }

    public EntityAudit prepareAudit() {
        EntityAudit audit = new EntityAudit();
        audit.setCreatedBy("system");
        audit.setCreatedDate(new Date());
        audit.setLastModifiedBy("system");
        audit.setLastModifiedDate(new Date());
        return audit;
    }

    public ClientRequestRepo getClientRequestRepo() {
        return clientRequestRepo;
    }

    public void setClientRequestRepo(ClientRequestRepo clientRequestRepo) {
        this.clientRequestRepo = clientRequestRepo;
    }

    public GsonBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(GsonBuilder builder) {
        this.builder = builder;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
