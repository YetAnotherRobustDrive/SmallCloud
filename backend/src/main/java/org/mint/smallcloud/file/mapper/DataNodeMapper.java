package org.mint.smallcloud.file.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.dto.DataNodeDto;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.mapper.LabelMapper;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.share.dto.ShareDto;
import org.mint.smallcloud.share.mapper.ShareMapper;

@Mapper(componentModel = "spring")
public interface DataNodeMapper {
    DataNodeMapper INSTANCE = Mappers.getMapper(DataNodeMapper.class);

    @Mapping(source = "dataNode.author.username", target = "authorName")
    @Mapping(source = "dataNode.parentFolder.id", target = "parentFolderId")
    @Mapping(source = "dataNode.shares", target = "shares", qualifiedByName = "toShareDto")
    @Mapping(source = "dataNode.labels", target = "labels", qualifiedByName = "toLabelDto")
    DataNodeDto toDataNodeDto(DataNode dataNode);

    @Named("toLabelDto")
    static LabelDto toLabelDto(Label label) {
        return LabelMapper.INSTANCE.toLabelDto(label);
    }

    @Named("toShareDto")
    static ShareDto toShareDto(Share share) {
        return ShareMapper.INSTANCE.toShareDto(share);
    }

}
