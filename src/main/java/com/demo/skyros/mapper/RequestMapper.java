package com.demo.skyros.mapper;

import com.demo.skyros.entity.ClientRequestEntity;
import com.demo.skyros.vo.RequestVO;
import org.mapstruct.Mapper;

@Mapper
public interface RequestMapper extends CommonMapper<ClientRequestEntity, RequestVO> {
}
