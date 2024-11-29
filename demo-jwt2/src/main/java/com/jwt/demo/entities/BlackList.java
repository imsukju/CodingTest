package com.jwt.demo.entities;

import lombok.*;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jwt_blacklist")
public class BlackList {
	 

	String token;
	private Timestamp expiration;
}
