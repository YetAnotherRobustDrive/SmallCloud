package org.mint.smallcloud.board.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mint.smallcloud.board.domain.Question;
import org.mint.smallcloud.board.dto.QuestionDto;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mapping(source = "question.answer.id", target = "answerId")
    QuestionDto toQuestionDto(Question question);
}
