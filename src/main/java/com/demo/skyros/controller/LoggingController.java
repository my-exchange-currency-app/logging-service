package com.demo.skyros.controller;

import com.demo.skyros.entity.ClientRequestEntity;
import com.demo.skyros.service.ClientRequestService;
import com.demo.skyros.vo.RequestCriteria;
import com.demo.skyros.vo.RequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("request")
public class LoggingController {

    @Autowired
    private ClientRequestService clientRequestService;

    @GetMapping("find")
    public List<ClientRequestEntity> findClientRequestPerDate(@RequestBody RequestCriteria criteria) {
        return clientRequestService.findClientRequestPerDate(criteria);
    }

    @PostMapping("add")
    public void saveClientRequest(@RequestBody RequestVO requestVO) {
        clientRequestService.saveClientRequest(requestVO);
    }

}
