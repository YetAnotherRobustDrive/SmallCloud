package org.mint.smallcloud.file.Repository;

import org.junit.jupiter.api.Test;
import org.mint.smallcloud.data.FileLocation;
import org.mint.smallcloud.file.File;
import org.mint.smallcloud.file.FileType;
import org.mint.smallcloud.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void save() {
        FileLocation fileLocation = new FileLocation("testLocation");
        FileType fileType = new FileType("testName", "txtType");
        LocalDateTime localDateTime = LocalDateTime.of(2023, 4,23,12,34,56);

        User author = new User();
        author.setNickname("testAuthor");

        File file1 = new File();
        Long fileSize = 123L;

        File file = new File();
        file.setFileType(fileType);

        File saveFile = fileRepository.save(file);

        assertEquals("testName",saveFile.getName());
        assertEquals("txtType",saveFile.getFileType().getType());
        assertEquals(LocalDateTime.of(2023, 4,23,12,34,56),localDateTime);
        assertEquals("testLocation",fileLocation.getLocation());
        assertEquals("testAuthor", author.getNickname());
        assertEquals(123L,fileSize);

    }
}