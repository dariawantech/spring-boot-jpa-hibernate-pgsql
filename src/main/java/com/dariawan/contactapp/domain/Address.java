/**
 * Spring Boot + JPA/Hibernate + PostgreSQL RESTful CRUD API Example (https://www.dariawan.com)
 * Copyright (C) 2020 Dariawan <hello@dariawan.com>
 *
 * Creative Commons Attribution-ShareAlike 4.0 International License
 *
 * Under this license, you are free to:
 * # Share - copy and redistribute the material in any medium or format
 * # Adapt - remix, transform, and build upon the material for any purpose,
 *   even commercially.
 *
 * The licensor cannot revoke these freedoms
 * as long as you follow the license terms.
 *
 * License terms:
 * # Attribution - You must give appropriate credit, provide a link to the
 *   license, and indicate if changes were made. You may do so in any
 *   reasonable manner, but not in any way that suggests the licensor
 *   endorses you or your use.
 * # ShareAlike - If you remix, transform, or build upon the material, you must
 *   distribute your contributions under the same license as the original.
 * # No additional restrictions - You may not apply legal terms or
 *   technological measures that legally restrict others from doing anything the
 *   license permits.
 *
 * Notices:
 * # You do not have to comply with the license for elements of the material in
 *   the public domain or where your use is permitted by an applicable exception
 *   or limitation.
 * # No warranties are given. The license may not give you all of
 *   the permissions necessary for your intended use. For example, other rights
 *   such as publicity, privacy, or moral rights may limit how you use
 *   the material.
 *
 * You may obtain a copy of the License at
 *   https://creativecommons.org/licenses/by-sa/4.0/
 *   https://creativecommons.org/licenses/by-sa/4.0/legalcode
 */
package com.dariawan.contactapp.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description = "Class representing an address.")
@Getter
@Setter
public class Address implements Serializable {

    @ApiModelProperty(notes = "Address line 1 of the contact.", 
            example = "888 Constantine Ave, #54", required = false, position = 0)
    @Size(max = 50)
    private String address1;
    
    @ApiModelProperty(notes = "Address line 2 of the contact.", 
            example = "San Angeles", required = false, position = 1)
    @Size(max = 50)
    private String address2;
    
    @ApiModelProperty(notes = "Address line 3 of the contact.", 
            example = "Florida", required = false, position = 2)
    @Size(max = 50)
    private String address3;
    
    @ApiModelProperty(notes = "Postal code of the contact.", 
            example = "32106", required = false, position = 3)
    @Size(max = 20)
    private String postalCode;    
}
