package com.dariawan.contactapp.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address implements Serializable {

    private String address1;
    private String address2;
    private String address3;
    private String postalCode;    
}
