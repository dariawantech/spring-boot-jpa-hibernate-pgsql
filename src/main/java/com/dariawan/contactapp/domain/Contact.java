package com.dariawan.contactapp.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@ApiModel(description = "Class representing a contact in the application.")
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
public class Contact implements Serializable {

    private static final long serialVersionUID = 4048798961366546485L;

    @ApiModelProperty(notes = "Unique identifier of the Contact.", 
            example = "1", required = true, position = 0)
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ApiModelProperty(notes = "Name of the contact.", 
            example = "Jessica Abigail", required = true, position = 1)
    @NotBlank
    private String name;
    
    @ApiModelProperty(notes = "Phone number of the contact.", 
            example = "62482211", required = false, position = 2)
    private String phone;
    
    @ApiModelProperty(notes = "Email address of the contact.", 
            example = "jessica@ngilang.com", required = false, position = 3)
    private String email;
    
    @ApiModelProperty(notes = "Address line 1 of the contact.", 
            example = "888 Constantine Ave, #54", required = false, position = 4)
    private String address1;
    
    @ApiModelProperty(notes = "Address line 2 of the contact.", 
            example = "San Angeles", required = false, position = 5)
    private String address2;
    
    @ApiModelProperty(notes = "Address line 3 of the contact.", 
            example = "Florida", required = false, position = 6)
    private String address3;
    
    @ApiModelProperty(notes = "Postal code of the contact.", 
            example = "32106", required = false, position = 7)
    private String postalCode;
    
    @ApiModelProperty(notes = "Notes about the contact.", 
            example = "Meet her at Spring Boot Conference", required = false, position = 8)
    @Column(length = 4000)
    private String note;    
}
