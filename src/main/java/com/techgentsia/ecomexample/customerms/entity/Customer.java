package com.techgentsia.ecomexample.customerms.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

import static com.techgentsia.ecomexample.customerms.constants.AuthConstants.USER_ROLE;
import static com.techgentsia.ecomexample.customerms.constants.MessageConstants.*;

@Entity
public @Data
@Table(name = "customers")
class Customer extends DBTimestamps implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotBlank(message = FIRST_NAME_VALIDATION)
    private String firstName;
    @NotBlank(message = LAST_NAME_VALIDATION)
    private String lastName;
    @NotBlank(message = EMAIL_VALIDATION)
    @Email
    @Column(unique=true)
    private String email;
    private String img;
    @NotBlank(message = PASSWORD_VALIDATION)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String roles = USER_ROLE;
}
