package org.mint.smallcloud.share.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.share.dto.ShareDto;

@Mapper(componentModel = "spring")
public interface ShareMapper {
    ShareMapper INSTANCE = Mappers.getMapper(ShareMapper.class);

    @Mapping(source = "share.file.id", target = "fileId")
    @Mapping(source = "share", target = "type", qualifiedByName = "getType")
    @Mapping(source = "share", target = "targetName", qualifiedByName = "getTargetName")
    ShareDto toShareDto(Share share);

    @Named("getType")
    static String getType(Share share) {
        return share.getClass().getSimpleName();
    }

    @Named("getTargetName")
    static String getTargetName(Share share) {
        return share.getTargetName();
    }

}
