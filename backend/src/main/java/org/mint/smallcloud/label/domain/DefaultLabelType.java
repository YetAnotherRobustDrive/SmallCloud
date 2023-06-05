package org.mint.smallcloud.label.domain;

import lombok.Getter;

@Getter
public enum DefaultLabelType {
    defaultTrash( "!$@*%&Trash"),
    defaultFavorite("!$@*%&Favorite"),
    defaultDraft("!$@*%&Draft"),
    defaultExpiration("!$@*%&Expiration"),
    defaultFinal("!$@*%&Final"),
    defaultSecurity("!$@*%&Security"),
    defaultPublic("!$@*%&Public"),
    defaultSensitivity("!$@*%&Sensitivity");

    private final String labelName;
    DefaultLabelType(String labelName) {
        this.labelName = labelName;
    }

}
