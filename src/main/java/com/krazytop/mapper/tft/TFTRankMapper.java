package com.krazytop.mapper.tft;

import com.krazytop.api_gateway.model.generated.RIOTRankDTO;
import com.krazytop.entity.riot.rank.RIOTRank;
import com.krazytop.mapper.DateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateMapper.class)
public interface TFTRankMapper {

    RIOTRankDTO toDTO(RIOTRank rank);
}
