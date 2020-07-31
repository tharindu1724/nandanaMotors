package lk.nandanaMotors.asset.customer.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.nandanaMotors.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Customer")
public class Customer extends AuditEntity {

    @Size(min = 5, message = "Your name cannot be accepted")
    private String name;

    @Size(max = 10, min = 9, message = "Mobile number length should be contained 10 and 9")
    @Column(unique = true)
    private String mobile;

    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NULL", length = 255)
    private String address;

    @Column(unique = true)
    private String code; // ex. {yearLastTwo}{less than ten thousand}

    @Column(unique = true)
    private String email;
}