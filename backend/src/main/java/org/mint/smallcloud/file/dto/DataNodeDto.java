package org.mint.smallcloud.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mint.smallcloud.label.dto.LabelDto;
import org.mint.smallcloud.share.dto.ShareDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@Getter
public class DataNodeDto {
    private Long id;
    private String name;
    private String parentFolderId;
    private LocalDateTime createdDate;
    private String authorName;
    private List<LabelDto> labels;
    private List<ShareDto> shares;
}
