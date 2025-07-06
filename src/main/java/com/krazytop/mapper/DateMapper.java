package com.krazytop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DateMapper {

    default OffsetDateTime toDTO(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }
}
