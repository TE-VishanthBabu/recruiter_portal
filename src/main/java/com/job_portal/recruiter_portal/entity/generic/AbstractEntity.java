package com.job_portal.recruiter_portal.entity.generic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractEntity extends UUIDEntity {

    @CreatedBy
    @Column(name = "created_by", nullable = true, updatable = false)
    protected String createdBy;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date creationDate;

    @LastModifiedBy
    @Column(name = "modified_by", nullable = true)
    protected String lastModifiedBy;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date lastModificationDate;

    @Version
    @Setter
    @JsonIgnore
    private Long version;

    @Setter
    private boolean deleted = Boolean.FALSE;
}
