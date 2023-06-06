package org.mint.smallcloud.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NoticeAllEventAfterCommit {
    private final String content;
}
