package org.mint.smallcloud.label.domain;

public enum DefaultLabelType {
    defaultTrash("!$@*%&Trash"),
    defaultFavorite("!$@*%&Favorite");

    private final String labelName;

    DefaultLabelType(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return this.labelName;
    }
}
