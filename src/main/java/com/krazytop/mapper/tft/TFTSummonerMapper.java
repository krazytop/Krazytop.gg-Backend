package com.krazytop.mapper.tft;

import com.krazytop.api_gateway.model.generated.RIOTSummonerDTO;
import com.krazytop.entity.riot.RIOTSummoner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TFTSummonerMapper {

    RIOTSummonerDTO toDTO(RIOTSummoner summoner);
}
