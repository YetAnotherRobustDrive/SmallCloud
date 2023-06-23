package org.mint.smallcloud.file.dto;


public class FileDto extends DataNodeDto {
    private final Long size;
    private final String location;

    protected FileDto(FileDtoBuilder<?, ?> b) {
        super(b);
        this.size = b.size;
        this.location = b.location;
    }

    public static FileDtoBuilder<?, ?> builder() {
        return new FileDtoBuilderImpl();
    }

    public Long getSize() {
        return this.size;
    }

    public String getLocation() {
        return this.location;
    }

    public static abstract class FileDtoBuilder<C extends FileDto, B extends FileDtoBuilder<C, B>> extends DataNodeDtoBuilder<C, B> {
        private Long size;
        private String location;

        public B size(Long size) {
            this.size = size;
            return self();
        }

        public B location(String location) {
            this.location = location;
            return self();
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "FileDto.FileDtoBuilder(super=" + super.toString() + ", size=" + this.size + ", location=" + this.location + ")";
        }
    }

    private static final class FileDtoBuilderImpl extends FileDtoBuilder<FileDto, FileDtoBuilderImpl> {
        private FileDtoBuilderImpl() {
        }

        protected FileDtoBuilderImpl self() {
            return this;
        }

        public FileDto build() {
            return new FileDto(this);
        }
    }
}
