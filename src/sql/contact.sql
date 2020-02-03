--
-- Spring Boot + JPA/Hibernate + PostgreSQL RESTful CRUD API Example (https://www.dariawan.com)
-- Copyright (C) 2020 Dariawan <hello@dariawan.com>
--
-- Creative Commons Attribution-ShareAlike 4.0 International License
--
-- Under this license, you are free to:
-- # Share - copy and redistribute the material in any medium or format
-- # Adapt - remix, transform, and build upon the material for any purpose,
--   even commercially.
--
-- The licensor cannot revoke these freedoms
-- as long as you follow the license terms.
--
-- License terms:
-- # Attribution - You must give appropriate credit, provide a link to the
--   license, and indicate if changes were made. You may do so in any
--   reasonable manner, but not in any way that suggests the licensor
--   endorses you or your use.
-- # ShareAlike - If you remix, transform, or build upon the material, you must
--   distribute your contributions under the same license as the original.
-- # No additional restrictions - You may not apply legal terms or
--   technological measures that legally restrict others from doing anything the
--   license permits.
--
-- Notices:
-- # You do not have to comply with the license for elements of the material in
--   the public domain or where your use is permitted by an applicable exception
--   or limitation.
-- # No warranties are given. The license may not give you all of
--   the permissions necessary for your intended use. For example, other rights
--   such as publicity, privacy, or moral rights may limit how you use
--   the material.
--
-- You may obtain a copy of the License at
--   https://creativecommons.org/licenses/by-sa/4.0/
--   https://creativecommons.org/licenses/by-sa/4.0/legalcode
--
CREATE TABLE contact
(
  id bigserial NOT NULL,
  name character varying(255),
  phone character varying(255),
  email character varying(255),
  address1 character varying(255),
  address2 character varying(255),
  address3 character varying(255),
  postal_code character varying(255),
  note character varying(4000),
  CONSTRAINT contact_pkey PRIMARY KEY (id)
);

ALTER TABLE contact OWNER TO barista;

insert into contact (name, phone, email)
values 
('Monkey D. Luffy', '09012345678', 'luffy@strawhatpirat.es'),
('Roronoa Zoro', '09023456789', 'zoro@strawhatpirat.es'),
('Nami', '09034567890', 'nami@strawhatpirat.es'),
('Usopp', '09045678901', 'usopp@strawhatpirat.es'),
('Vinsmoke Sanji', '09056789012', 'sanji@strawhatpirat.es'),
('Tony Tony Chopper', '09067890123', 'chopper@strawhatpirat.es'),
('Nico Robin', '09078901234', 'robin@strawhatpirat.es'),
('Franky', '09089012345', 'franky@strawhatpirat.es'),
('Brook', '09090123456', 'brook@strawhatpirat.es')

-- truncate table contact;
-- select * from contact;