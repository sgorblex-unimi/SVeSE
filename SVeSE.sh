#!/bin/sh

set -e

BASE=$(readlink -f "${0%/*}")
DB_DIR=$BASE/database

USAGE="SVeSE: Sistema di Voto e Scrutinio Elettronico by Miniblex corp.

USAGE:
$0 VERB

Where VERB is one of:
build-db	(re)creates PostgreSQL database
run		runs the system"

# TODO: very temporary and not secure
DB_INIT_SQL="CREATE ROLE svese WITH login ENCRYPTED PASSWORD 'svese';
CREATE DATABASE svese WITH OWNER svese;"
DB_PORT=65432												# TODO: check port


builddb(){
	if ! command -v pg_ctl >/dev/null; then
		printf "PostgreSQL not detected (pg_ctl command not found in your PATH). Please install PostgreSQL and try again.\n" >&2
		return 1
	fi
	[ ! -d /run/postgresql ] && sudo mkdir /run/postgresql && sudo chown $USER /run/postgresql	# TODO: ugly
	[ -f $DB_DIR/postmaster.pid ] && pg_ctl -D $DB_DIR stop
	rm -rf $DB_DIR											# TODO: unsafe
	pg_ctl -D $DB_DIR init
	pg_ctl -D $DB_DIR start -o -p$DB_PORT
	printf "%s\n" "$DB_INIT_SQL" | psql -p $DB_PORT postgres
	pg_ctl -D $DB_DIR stop
}

run(){
	if ! command -v mvn >/dev/null; then
		printf "Maven not detected (mvn command not found in your PATH). Please install Maven and try again.\n" >&2
		return 1
	fi
	if ! command -v pg_ctl >/dev/null; then
		printf "PostgreSQL not detected (pg_ctl command not found in your PATH). Please install PostgreSQL and try again.\n" >&2
		return 1
	fi
	if [ ! -d $DB_DIR ]; then
		printf "Database directory not found. Run $0 build-db first.\n" >&2
		return 1;
	fi
	if [ -f $DB_DIR/postmaster.pid ]; then
		pg_ctl -D $DB_DIR stop
	fi
	[ ! -d /run/postgresql ] && sudo mkdir /run/postgresql && sudo chown $USER /run/postgresql	# TODO: ugly
	[ -f $DB_DIR/postmaster.pid ] && pg_ctl -D $DB_DIR stop
	pg_ctl -D $DB_DIR start -o -p$DB_PORT
	trap "sleep 0" INT
	mvn spring-boot:run || true
	pg_ctl -D $DB_DIR stop
}

if [ -z $1 ]; then
	printf "$USAGE\n" >&2
	exit 1
fi

if [ $# -gt 2 ]; then
	printf "ERROR: Provide only one argument.\n" >&2
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
	*)
		printf "ERROR: Unknown verb.\n" >&2
esac
