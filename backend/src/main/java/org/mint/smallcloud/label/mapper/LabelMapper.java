package org.mint.smallcloud.label.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelDto;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelMapper INSTANCE = Mappers.getMapper(LabelMapper.class);

    @Mapping(source = "label.name", target = "name")
    @Mapping(source = "label.owner.username", target = "owner.username")
    @Mapping(source = "label.owner.nickname", target = "owner.nickname")
    LabelDto toLabelDto(Label label);
}
