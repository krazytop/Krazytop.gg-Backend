package com.krazytop.mapper.lol;

import com.krazytop.api_gateway.model.generated.LOLPatchDTO;
import com.krazytop.api_gateway.model.generated.LOLRunePerkNomenclatureDTO;
import com.krazytop.nomenclature.lol.LOLPatch;
import com.krazytop.nomenclature.lol.LOLRunePerkNomenclature;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LOLPatchMapper {

    LOLPatchDTO toDTO(LOLPatch patch);
    List<LOLRunePerkNomenclatureDTO> toDTOList(List<LOLRunePerkNomenclature> perkList);
    List<List<LOLRunePerkNomenclatureDTO>> toDTOListOfLists(List<List<LOLRunePerkNomenclature>> perkList);
}
