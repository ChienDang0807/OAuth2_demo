package com.example.demo.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "User")
@Table(name = "user_login")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	//sequenceName là tên sequence trong csdl , allocationSize phải bằng increment by trong oracle
	@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
	Integer id;
	
	@Column(name = "username")
	 String username;
	
	@Column(name = "password")
	String password;
	
	@Column(name = "firstname")
	 String firstName;
	
	@Column(name = "lastname")
	String lastName;
	
	@Column(name = "dob")
	LocalDate dob;

	@ManyToMany
	Set<Role> roles;
}
