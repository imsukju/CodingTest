package com.test1017.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
	@CreationTimestamp
	protected LocalDateTime creationDate;
	
	protected String lastModifiedBy;
	
	@UpdateTimestamp
	protected LocalDateTime lastModifiedDate;
}
