package org.mint.smallcloud.board.dto;

import org.mint.smallcloud.board.domain.BoardType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BoardDto {

    private final Long id;

    private final String title;

    @Size(min = 1)
    @NotBlank(message = "보드 내용은 필수로 들어가야 합니다.")
    private final String content;

    @NotNull(message = "보드 타입은 생략될 수 없습니다.")
    private final BoardType boardType;

    public BoardDto(Long id, String title, @Size(min = 1) @NotBlank(message = "보드 내용은 필수로 들어가야 합니다.") String content, @NotNull(message = "보드 타입은 생략될 수 없습니다.") BoardType boardType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
    }

    public static BoardDtoBuilder builder() {
        return new BoardDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public @Size(min = 1) @NotBlank(message = "보드 내용은 필수로 들어가야 합니다.") String getContent() {
        return this.content;
    }

    public @NotNull(message = "보드 타입은 생략될 수 없습니다.") BoardType getBoardType() {
        return this.boardType;
    }

    public static class BoardDtoBuilder {
        private Long id;
        private String title;
        private @Size(min = 1) @NotBlank(message = "보드 내용은 필수로 들어가야 합니다.") String content;
        private @NotNull(message = "보드 타입은 생략될 수 없습니다.") BoardType boardType;

        BoardDtoBuilder() {
        }

        public BoardDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BoardDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BoardDtoBuilder content(@Size(min = 1) @NotBlank(message = "보드 내용은 필수로 들어가야 합니다.") String content) {
            this.content = content;
            return this;
        }

        public BoardDtoBuilder boardType(@NotNull(message = "보드 타입은 생략될 수 없습니다.") BoardType boardType) {
            this.boardType = boardType;
            return this;
        }

        public BoardDto build() {
            return new BoardDto(this.id, this.title, this.content, this.boardType);
        }

        public String toString() {
            return "BoardDto.BoardDtoBuilder(id=" + this.id + ", title=" + this.title + ", content=" + this.content + ", boardType=" + this.boardType + ")";
        }
    }
}

