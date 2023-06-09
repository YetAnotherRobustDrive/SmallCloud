package org.mint.smallcloud.label.domain;

import lombok.Getter;

@Getter
public enum DefaultLabelType {
    defaultTrash( "!$@*%&Trash"),
    defaultFavorite("!$@*%&Favorite");

    private final String labelName;
    DefaultLabelType(String labelName) {
        this.labelName = labelName;
    }

}
