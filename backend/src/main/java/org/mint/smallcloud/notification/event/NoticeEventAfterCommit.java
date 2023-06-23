package org.mint.smallcloud.notification.event;

import org.mint.smallcloud.user.domain.Member;

public class NoticeEventAfterCommit {
    private final String content;
    private final Member owner;

    public NoticeEventAfterCommit(String content, Member owner) {
        this.content = content;
        this.owner = owner;
    }

    public static NoticeEventAfterCommitBuilder builder() {
        return new NoticeEventAfterCommitBuilder();
    }

    public String getContent() {
        return this.content;
    }

    public Member getOwner() {
        return this.owner;
    }

    public static class NoticeEventAfterCommitBuilder {
        private String content;
        private Member owner;

        NoticeEventAfterCommitBuilder() {
        }

        public NoticeEventAfterCommitBuilder content(String content) {
            this.content = content;
            return this;
        }

        public NoticeEventAfterCommitBuilder owner(Member owner) {
            this.owner = owner;
            return this;
        }

        public NoticeEventAfterCommit build() {
            return new NoticeEventAfterCommit(this.content, this.owner);
        }

        public String toString() {
            return "NoticeEventAfterCommit.NoticeEventAfterCommitBuilder(content=" + this.content + ", owner=" + this.owner + ")";
        }
    }
}
