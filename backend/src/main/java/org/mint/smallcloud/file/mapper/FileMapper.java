package org.mint.smallcloud.file.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.file.domain.File;
import org.mint.smallcloud.file.dto.FileDto;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.mapper.LabelMapper;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.share.dto.ShareDto;
import org.mint.smallcloud.share.mapper.ShareMapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(source = "file.parentFolder.id", target = "parentFolderId")
    @Mapping(source = "file.author.username", target = "authorName")
    @Mapping(source = "file.labels", target = "labels", qualifiedByName = "toLabelDto")
    @Mapping(source = "file.shares", target = "shares", qualifiedByName = "toShareDto")
    @Mapping(source = "file.size", target = "size")
    @Mapping(source = "file.location.location", target = "location")
    FileDto toFileDto(File file);

    @Named("toLabelDto")
    static LabelDto toLabelDto(Label label) {
        return LabelMapper.INSTANCE.toLabelDto(label);
    }

    @Named("toShareDto")
    static ShareDto toShareDto(Share share) {
        return ShareMapper.INSTANCE.toShareDto(share);
    }
}
