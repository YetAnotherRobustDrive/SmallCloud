package org.mint.smallcloud.notification.event;

public class NoticeAllEventAfterCommit {
    private final String content;

    public NoticeAllEventAfterCommit(String content) {
        this.content = content;
    }

    public static NoticeAllEventAfterCommitBuilder builder() {
        return new NoticeAllEventAfterCommitBuilder();
    }

    public String getContent() {
        return this.content;
    }

    public static class NoticeAllEventAfterCommitBuilder {
        private String content;

        NoticeAllEventAfterCommitBuilder() {
        }

        public NoticeAllEventAfterCommitBuilder content(String content) {
            this.content = content;
            return this;
        }

        public NoticeAllEventAfterCommit build() {
            return new NoticeAllEventAfterCommit(this.content);
        }

        public String toString() {
            return "NoticeAllEventAfterCommit.NoticeAllEventAfterCommitBuilder(content=" + this.content + ")";
        }
    }
}
