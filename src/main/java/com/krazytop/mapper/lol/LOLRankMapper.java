package com.krazytop.mapper.lol;

import com.krazytop.api_gateway.model.generated.RIOTRankDTO;
import com.krazytop.entity.riot.rank.RIOTRank;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LOLRankMapper {

    RIOTRankDTO toDTO(RIOTRank rank);
}
