package org.mint.smallcloud.share.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.file.mapper.FolderMapper;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.share.dto.ShareDto;

@Mapper(componentModel = "spring")
public interface ShareMapper {
    ShareMapper INSTANCE = Mappers.getMapper(ShareMapper.class);

    ShareDto toShareDto(Share share);

}
