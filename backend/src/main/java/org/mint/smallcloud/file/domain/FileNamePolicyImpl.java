package org.mint.smallcloud.file.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileNamePolicyImpl implements FileNamePolicy {
    private static final Pattern fileSuffixPattern = Pattern.compile("_[0-9]+$");

    public String nextFileName(String fileName) {
        if (fileSuffixPattern.matcher(fileName).find()) {
            return fileName.replaceAll("_[0-9]+$", "_" + (Integer.parseInt(fileName.substring(fileName.lastIndexOf("_") + 1)) + 1));
        } else {
            return fileName + "_1";
        }
    }
}
