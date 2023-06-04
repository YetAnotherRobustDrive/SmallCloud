package org.mint.smallcloud.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.user.domain.Member;
import org.mint.smallcloud.user.dto.UserLabelDto;
import org.mint.smallcloud.user.dto.UserProfileResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "member.group.name", target = "groupName")
    UserProfileResponseDto toUserProfileResponseDto(Member member);
}
