package org.mint.smallcloud.file.dto;


public class DirectoryDto extends DataNodeDto {
    protected DirectoryDto(DirectoryDtoBuilder<?, ?> b) {
        super(b);
    }

    public static DirectoryDtoBuilder<?, ?> builder() {
        return new DirectoryDtoBuilderImpl();
    }

    public static abstract class DirectoryDtoBuilder<C extends DirectoryDto, B extends DirectoryDtoBuilder<C, B>> extends DataNodeDtoBuilder<C, B> {
        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "DirectoryDto.DirectoryDtoBuilder(super=" + super.toString() + ")";
        }
    }

    private static final class DirectoryDtoBuilderImpl extends DirectoryDtoBuilder<DirectoryDto, DirectoryDtoBuilderImpl> {
        private DirectoryDtoBuilderImpl() {
        }

        protected DirectoryDtoBuilderImpl self() {
            return this;
        }

        public DirectoryDto build() {
            return new DirectoryDto(this);
        }
    }
}
