-- Copyright (C) 2021 Alessandro "Sgorblex" Clerici Lorenzini and Edoardo "Miniman" Della Rossa.
--
-- This file is part of SVeSE.
--
-- SVeSE is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- SVeSE is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with SVeSE.  If not, see <https://www.gnu.org/licenses/>.


CREATE EXTENSION IF NOT EXISTS pgcrypto;
INSERT INTO person VALUES
-- ssn		birth_date	birth_place	enabled	first_name	last_name	password
('DAD52',	'1952-03-11',	'Cambridge',	true,	'Douglas',	'Adams',	crypt('42', gen_salt('bf'))),
('JVN55',	'1955-03-15',	'Budapest',	true,	'John',		'von Neumann',	crypt('CentralProcessUnit', gen_salt('bf'))),
('ALL15',	'1815-12-10',	'London',	true,	'Ada',		'Lovelace',	crypt('Algorithm', gen_salt('bf'))),
('ATU12',	'1912-12-06',	'London',	true,	'Alan',		'Turing',	crypt('Christopher', gen_salt('bf')))
ON CONFLICT DO NOTHING;
