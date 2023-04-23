package com.demo.skyros.service;

import com.demo.skyros.entity.ClientRequestEntity;
import com.demo.skyros.entity.EntityAudit;
import com.demo.skyros.mapper.RequestMapper;
import com.demo.skyros.repo.ClientRequestRepo;
import com.demo.skyros.vo.CurrencyExchangeVO;
import com.demo.skyros.vo.RequestCriteria;
import com.demo.skyros.vo.RequestVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Getter
@Setter
public class ClientRequestService {

    private static final String REQUEST_ID = "REQUEST_ID";
    Logger logger = LoggerFactory.getLogger(ClientRequestService.class);
    private GsonBuilder builder = new GsonBuilder();
    private Gson gson = builder.create();
    private ClientRequestRepo clientRequestRepo;
    @Autowired
    private RequestMapper requestMapper;

    public ClientRequestService(ClientRequestRepo clientRequestRepo) {
        this.clientRequestRepo = clientRequestRepo;
    }

    public List<RequestVO> findClientRequestPerDate(RequestCriteria criteria) {
        List<ClientRequestEntity> requestEntities = getClientRequestRepo().findByAuditCreatedDateBetween(criteria.getFrom(), criteria.getTo());
        return getRequestMapper().entityListToVOList(requestEntities);
    }

    public List<RequestVO> findAll() {
        return getRequestMapper().entityListToVOList(getClientRequestRepo().findAll());
    }

    public void saveClientRequest(RequestVO requestVO) {
        if (null != requestVO.getRequestId()) {
            ClientRequestEntity clientRequest = new ClientRequestEntity();
            clientRequest.setRequestId(requestVO.getRequestId());
            clientRequest.setTag(requestVO.getTag());
            clientRequest.setRequestBody(requestVO.getRequestBody());
            clientRequest.setAudit(prepareAudit());
            getClientRequestRepo().save(clientRequest);
        }
    }

    @Deprecated
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

    @Deprecated
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

}
