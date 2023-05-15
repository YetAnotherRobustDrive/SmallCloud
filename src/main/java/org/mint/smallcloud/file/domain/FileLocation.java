package org.mint.smallcloud.file.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileLocation implements Serializable {
    @Column(name = "LOCATION", length = 40)
    @Size(min = 1, max = 40, message = "파일 로키이션은 40자 이하로 작성해 주세요")
    private String location;

    protected FileLocation(String location) {
        this.location = location;
    }

    public static FileLocation of(String location) {
        return new FileLocation(location);
    }
}
