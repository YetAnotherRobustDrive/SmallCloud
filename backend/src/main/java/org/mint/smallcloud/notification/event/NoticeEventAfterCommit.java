package org.mint.smallcloud.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.mint.smallcloud.user.domain.Member;

@Getter
@AllArgsConstructor
@Builder
public class NoticeEventAfterCommit {
    private final String content;
    private final Member owner;
}
