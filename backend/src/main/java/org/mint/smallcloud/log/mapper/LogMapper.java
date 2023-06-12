package org.mint.smallcloud.log.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.log.dto.ResponseLogDto;
import org.mint.smallcloud.log.user.UserLog;
import org.mint.smallcloud.user.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface LogMapper {
    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    @Mapping(source = "userLog.member.nickname", target = "nickName")
    @Mapping(source = "userLog.time", target = "localDateTime")
    @Mapping(source = "userLog.action", target = "action")
    @Mapping(source = "userLog.ipAddr", target = "ipAddr")
    @Mapping(source = "userLog.status", target = "status")
    ResponseLogDto toResponseLogDto(UserLog userLog);
}
