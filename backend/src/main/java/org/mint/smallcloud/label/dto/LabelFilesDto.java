package org.mint.smallcloud.label.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.file.domain.DataNode;
import org.mint.smallcloud.file.dto.DataNodeDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class LabelFilesDto {
    @Size(min=1, max=10, message = "라벨 이름은 10자 이하로 작성해주세요.")
    @NotBlank
    private final String name;
    private final List<DataNodeDto> files;
}
