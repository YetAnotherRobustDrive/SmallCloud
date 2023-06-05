package org.mint.smallcloud.label.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum defaultLabelType {
    defaultTrash( "!$@*%&Trash"),
    defaultFavorite("!$@*%&Favorite"),
    defaultDraft("!$@*%&Draft"),
    defaultExpiration("!$@*%&Expiration"),
    defaultFinal("!$@*%&Final"),
    defaultSecurity("!$@*%&Security"),
    defaultPublic("!$@*%&Public"),
    defaultSensitivity("!$@*%&Sensitivity");

    private final String labelName;
    defaultLabelType(String labelName) {
        this.labelName = labelName;
    }

}
