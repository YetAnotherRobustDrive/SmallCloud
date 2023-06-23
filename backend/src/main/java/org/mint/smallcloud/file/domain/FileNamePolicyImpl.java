package org.mint.smallcloud.file.domain;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class FileNamePolicyImpl implements FileNamePolicy {
    private static final Pattern fileSuffixPattern = Pattern.compile("_[0-9]+$");
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FileNamePolicyImpl.class);

    public FileNamePolicyImpl() {
    }

    public String nextFileName(String fileName) {
        if (fileSuffixPattern.matcher(fileName).find()) {
            return fileName.replaceAll("_[0-9]+$", "_" + (Integer.parseInt(fileName.substring(fileName.lastIndexOf("_") + 1)) + 1));
        } else {
            return fileName + "_1";
        }
    }
}
