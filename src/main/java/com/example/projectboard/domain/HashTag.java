package com.example.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "hashtagName",unique = true),
        @Index(columnList = "createAt"),
        @Index(columnList = "createBy")
    }

)
@Entity
public class HashTag extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToMany(mappedBy = "hashtags")
    private Set<Article> articles = new LinkedHashSet<>();
    @Setter
    @Column(nullable = false)
    private String hashtagName;

    protected HashTag() {}

    public HashTag(String hashtagName) {
        this.hashtagName = hashtagName;
    }

    public static HashTag of (String hashtagName){
        return new HashTag(hashtagName);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashTag that)) return false;
        return this.getId() != null && this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
