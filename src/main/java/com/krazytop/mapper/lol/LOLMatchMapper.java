package com.krazytop.mapper.lol;

import com.krazytop.api_gateway.model.generated.LOLMatchDTO;
import com.krazytop.entity.lol.LOLMatch;
import com.krazytop.mapper.DateMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateMapper.class)
public interface LOLMatchMapper {

    LOLMatchDTO toDTO(LOLMatch board);
}
