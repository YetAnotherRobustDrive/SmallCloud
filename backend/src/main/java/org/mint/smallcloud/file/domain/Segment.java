package org.mint.smallcloud.file.domain;

import javax.persistence.*;

@Table(name = "SEGMENTS")
@Entity
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

    public Segment(Long id, File file, String location, String name, Long size) {
        this.id = id;
        this.file = file;
        this.location = location;
        this.name = name;
        this.size = size;
    }

    protected Segment() {
    }

    public static SegmentBuilder builder() {
        return new SegmentBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public File getFile() {
        return this.file;
    }

    public String getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public Long getSize() {
        return this.size;
    }

    public static class SegmentBuilder {
        private Long id;
        private File file;
        private String location;
        private String name;
        private Long size;

        SegmentBuilder() {
        }

        public SegmentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SegmentBuilder file(File file) {
            this.file = file;
            return this;
        }

        public SegmentBuilder location(String location) {
            this.location = location;
            return this;
        }

        public SegmentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SegmentBuilder size(Long size) {
            this.size = size;
            return this;
        }

        public Segment build() {
            return new Segment(this.id, this.file, this.location, this.name, this.size);
        }

        public String toString() {
            return "Segment.SegmentBuilder(id=" + this.id + ", file=" + this.file + ", location=" + this.location + ", name=" + this.name + ", size=" + this.size + ")";
        }
    }
}
