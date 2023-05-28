package org.mint.smallcloud.label.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelDto;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelMapper INSTANCE = Mappers.getMapper(LabelMapper.class);

    LabelDto toLabelDto(Label label);
}
