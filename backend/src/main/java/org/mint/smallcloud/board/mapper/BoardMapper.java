package org.mint.smallcloud.board.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.board.domain.Board;
import org.mint.smallcloud.board.dto.BoardDto;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    BoardDto toBoardDto(Board board);
}
