#!/bin/sh
# Copyright (C) 2021 Alessandro "Sgorblex" Clerici Lorenzini and Edoardo "Miniman" Della Rossa.
#
# This file is part of SVeSE.
#
# SVeSE is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# SVeSE is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with SVeSE.  If not, see <https://www.gnu.org/licenses/>.


set -e

DB_USER=svese
DB_PW="svese"
DB_NAME=svese
DB_PORT=65432

BASE=$(readlink -f "${0%/*}")
DB_DIR="$BASE/database"



USAGE="SVeSE: Sistema di Voto e Scrutinio Elettronico by Miniblex corp.

USAGE:
$0 VERB

Where VERB is one of:
build-db	(re)creates PostgreSQL database
run		runs the system
run-sql	FILE	runs SQL from the given file in the PostgreSQL database
run-tests	runs the tests (debug purposes)
start-db	starts PostgreSQL database (debug purposes)
stop-db		stops PostgreSQL database (debug purposes)"

DB_INIT_SQL="CREATE ROLE $DB_PW WITH login ENCRYPTED PASSWORD '$DB_PW';
CREATE DATABASE $DB_NAME WITH OWNER $DB_USER;"


# $1: command
# $2: program name (can omit if equals to $1)
checkSw(){
	name="${2:-$1}"
	if ! command -v "$1" >/dev/null; then
		printf "%s not detected (%s command not found in your PATH). Please install %s and try again.\n" "$name" "$1" "$name" >&2
		return 1
	fi
}

checkSw pg_ctl PostgreSQL
checkSw mvn Maven
[ ! -d /run/postgresql ] && sudo mkdir /run/postgresql && sudo chown $USER /run/postgresql


checkdbexists(){
	if [ ! -d $DB_DIR ]; then
		printf "Database directory not found. Run $0 build-db first.\n" >&2
		return 1
	fi
}

dbrunning(){
	[ -f "$DB_DIR/postmaster.pid" ]
}

db(){
	prefix="pg_ctl -D \"$DB_DIR\""
	[ "$1" = start ] && eval $prefix -o -p"$DB_PORT" $@ || eval $prefix $@
}

builddb(){
	if dbrunning; then
		db stop
	fi
	rm -rf $DB_DIR
	db init
	db start
	printf "%s\n" "$DB_INIT_SQL" | psql -p $DB_PORT postgres
	db stop
}

run(){
	checkdbexists
	if dbrunning; then
		db stop
	fi
	db start
	trap "sleep 0" INT
	! mvn spring-boot:run
	db stop
}

runtests(){
	checkdbexists
	if dbrunning; then
		db stop
	fi
	db start
	trap "sleep 0" INT
	! mvn test
	db stop
}

startdb(){
	checkdbexists
	if dbrunning; then
		printf "%s\n" "Already running."
		return 0
	fi
	db start
}

stopdb(){
	checkdbexists
	if ! dbrunning; then
		printf "%s\n" "Not running."
		return 0
	fi
	db stop
}

runsql(){
	checkdbexists
	dbrunning && wasrunning=true
	if [ -z $wasrunning ]; then
		db start
	fi
	cat "$1" | psql -p $DB_PORT -U $DB_USER "$DB_NAME"
	if [ -z $wasrunning ]; then
		db stop
	fi
}


if [ -z $1 ]; then
	printf "$USAGE\n" >&2
	exit 1
fi

if [ $# -gt 2 ]; then
	if [ "$1" != run-sql ] || [ $# -ne 3 ]; then
		printf "ERROR: Provide only one argument.\n" >&2
	fi
fi

case "$1" in
	--help|-h)
		printf "$USAGE\n"
		;;
	build-db)
		builddb
		;;
	run)
		run
		;;
	run-sql)
		runsql "$2"
		;;
	run-tests)
		runtests
		;;
	start-db)
		startdb
		;;
	stop-db)
		stopdb
		;;
	*)
		printf "ERROR: Unknown verb.\n" >&2
esac
