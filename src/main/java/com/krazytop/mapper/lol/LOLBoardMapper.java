package com.krazytop.mapper.lol;

import com.krazytop.api_gateway.model.generated.RIOTBoardDTO;
import com.krazytop.entity.riot.RIOTBoard;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LOLBoardMapper {

    RIOTBoardDTO toDTO(RIOTBoard board);
}
