package org.mint.smallcloud.file.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.share.dto.ShareDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
public class DataNodeDto {
    private final Long id;
    private final String name;
    private final String parentFolderId;
    private final LocalDateTime createdDate;
    private final String authorName;
    private final List<LabelDto> labels;
    private final List<ShareDto> shares;
}
