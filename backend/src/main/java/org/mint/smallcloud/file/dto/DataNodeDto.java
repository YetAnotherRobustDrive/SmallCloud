package org.mint.smallcloud.file.dto;

import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.share.dto.ShareDto;

import java.time.LocalDateTime;
import java.util.List;

public class DataNodeDto {
    private final Long id;
    private final String name;
    private final String parentFolderId;
    private final LocalDateTime createdDate;
    private final String authorName;
    private final List<LabelDto> labels;
    private final List<ShareDto> shares;

    protected DataNodeDto(DataNodeDtoBuilder<?, ?> b) {
        this.id = b.id;
        this.name = b.name;
        this.parentFolderId = b.parentFolderId;
        this.createdDate = b.createdDate;
        this.authorName = b.authorName;
        this.labels = b.labels;
        this.shares = b.shares;
    }

    public static DataNodeDtoBuilder<?, ?> builder() {
        return new DataNodeDtoBuilderImpl();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getParentFolderId() {
        return this.parentFolderId;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public List<LabelDto> getLabels() {
        return this.labels;
    }

    public List<ShareDto> getShares() {
        return this.shares;
    }

    public static abstract class DataNodeDtoBuilder<C extends DataNodeDto, B extends DataNodeDtoBuilder<C, B>> {
        private Long id;
        private String name;
        private String parentFolderId;
        private LocalDateTime createdDate;
        private String authorName;
        private List<LabelDto> labels;
        private List<ShareDto> shares;

        public B id(Long id) {
            this.id = id;
            return self();
        }

        public B name(String name) {
            this.name = name;
            return self();
        }

        public B parentFolderId(String parentFolderId) {
            this.parentFolderId = parentFolderId;
            return self();
        }

        public B createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return self();
        }

        public B authorName(String authorName) {
            this.authorName = authorName;
            return self();
        }

        public B labels(List<LabelDto> labels) {
            this.labels = labels;
            return self();
        }

        public B shares(List<ShareDto> shares) {
            this.shares = shares;
            return self();
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "DataNodeDto.DataNodeDtoBuilder(id=" + this.id + ", name=" + this.name + ", parentFolderId=" + this.parentFolderId + ", createdDate=" + this.createdDate + ", authorName=" + this.authorName + ", labels=" + this.labels + ", shares=" + this.shares + ")";
        }
    }

    private static final class DataNodeDtoBuilderImpl extends DataNodeDtoBuilder<DataNodeDto, DataNodeDtoBuilderImpl> {
        private DataNodeDtoBuilderImpl() {
        }

        protected DataNodeDtoBuilderImpl self() {
            return this;
        }

        public DataNodeDto build() {
            return new DataNodeDto(this);
        }
    }
}
