package org.mint.smallcloud.file.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="SEGMENTS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Segment {
    private @Id @GeneratedValue Long id;
    
    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    private File file; // related file id(mpd file)
    
    private String location; // object quantifier in terms of object storage
    private String name; // name of file
    private Long size; // size of file
}
