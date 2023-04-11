package org.mint.smallcloud.file.Repository;

import org.mint.smallcloud.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File,Long> {

}
