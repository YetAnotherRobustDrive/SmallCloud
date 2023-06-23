package org.mint.smallcloud.file.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.file.domain.Folder;
import org.mint.smallcloud.file.dto.DirectoryDto;
import org.mint.smallcloud.label.domain.Label;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.label.mapper.LabelMapper;
import org.mint.smallcloud.share.domain.Share;
import org.mint.smallcloud.share.dto.ShareDto;
import org.mint.smallcloud.share.mapper.ShareMapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);

    @Mapping(source = "folder.parentFolder.id", target = "parentFolderId")
    @Mapping(source = "folder.author.username", target = "authorName")
    @Mapping(source = "folder.labels", target = "labels", qualifiedByName = "toLabelDto")
    @Mapping(source = "folder.shares", target = "shares", qualifiedByName = "toShareDto")
    DirectoryDto toDirectoryDto(Folder folder);

    @Named("toLabelDto")
    static LabelDto toLabelDto(Label label) {
        return LabelMapper.INSTANCE.toLabelDto(label);
    }

    @Named("toShareDto")
    static ShareDto toShareDto(Share share) {
        return ShareMapper.INSTANCE.toShareDto(share);
    }
}
