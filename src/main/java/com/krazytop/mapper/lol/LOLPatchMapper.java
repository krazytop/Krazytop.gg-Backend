package com.krazytop.mapper.lol;

import com.krazytop.api_gateway.model.generated.LOLPatchDTO;
import com.krazytop.nomenclature.lol.LOLPatch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LOLPatchMapper {

    LOLPatchDTO toDTO(LOLPatch patch);
    LOLPatch toEntity(LOLPatchDTO metadataDTO);
}
