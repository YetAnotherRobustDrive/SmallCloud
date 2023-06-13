package org.mint.smallcloud.log.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.log.dto.ResponseLogDto;
import org.mint.smallcloud.log.user.UserLog;

@Mapper(componentModel = "spring")
public interface LogMapper {
    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    @Mapping(source = "userLog.member.nickname", target = "nickName")
    ResponseLogDto toResponseLogDto(UserLog userLog);
}
