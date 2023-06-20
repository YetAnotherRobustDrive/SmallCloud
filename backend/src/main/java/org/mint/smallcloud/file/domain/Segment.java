package org.mint.smallcloud.file.domain;

import lombok.*;

import javax.persistence.*;

@Table(name="SEGMENTS")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "FILE_ID")
    private File file; // related file id(mpd file)
    
    private String location; // object quantifier in terms of object storage
    private String name; // name of file
    private Long size; // size of file
}
