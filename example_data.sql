CREATE EXTENSION IF NOT EXISTS pgcrypto;
INSERT INTO person VALUES
-- ssn		birth_date	birth_place	enabled	first_name	last_name	password
('DAD52',	'1952-03-11',	'Cambridge',	true,	'Douglas',	'Adams',	crypt('42', gen_salt('bf'))),
('JVN55',	'1255-03-15',	'Budapest',	true,	'John',		'von Neumann',	crypt('CentralProcessUnit', gen_salt('bf'))),
('ALL15',	'1815-12-10',	'London',	true,	'Ada',		'Lovelace',	crypt('Algorithm', gen_salt('bf'))),
('ATU12',	'1912-12-06',	'London',	true,	'Alan',		'Turing',	crypt('Christopher', gen_salt('bf')))
ON CONFLICT DO NOTHING;
