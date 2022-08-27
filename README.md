# SVeSE
SVeSE (acronym for *Sistema di Voto e Scrutinio Elettronico*, Electronic System for Voting and Scrutinizing), is a all-in-one web application and system for digital voting and scrutinizing, made as a workshop project for the Software Engineering course of the Computer Science Bachelor's @ Unimi in academic year 2021/22.

The current state of the project is of prototype. The project has been already submitted for evaluation so further developments by us are unlikely (although who knows). The state of the project at submission time is marked by the tag `submitted`. Project documentation (*relazione di progetto*) is available in Italian [here](https://github.com/sgorblex-unimi/SVeSE_docs).



## Dependencies
- Java
- Maven
- PostgreSQL



## Run
Since the project is in state of prototype, the only supported running mode is debug mode. Refer to the utility script for that.


### Utility script
The script `SVeSE.sh` contains a series of utility functions for dealing with the system. See `SVeSE.sh --help`.


### Data

#### Importing people data
You need to externally import database data for the people/users. You can use `SVeSE.sh run-sql` for that.

For example, to import the given `example_data.sql` first run the system once:
```sh
./SVeSE.sh build-db
./SVeSE.sh run
```
(interrupt with CTRL+C). This will build and set up the database. Then, to import the data:
```sh
./SVeSE.sh run-sql example_data.sql
```

Of course, you can pick any method of your choice to populate the database.

#### Session administrator
You need to set the session administrator to a valid person in the database. To do that, just create a file called `admin.txt` in the system directory which contains the administrator's SSN:
```sh
echo "DAD52" > admin.txt
```



## Contribution
The project is now open to external contribution via [issue](https://github.com/sgorblex-unimi/SVeSE/issues) or [pull request](https://github.com/sgorblex-unimi/SVeSE/fork). A good place to start are TODOs (see below).


### Code etiquette
In general, follow the style of the already present code. Some details worth pointing out are:
- use `this.*` only if necessary or for clarity. This may be subject to change
- period dot after javadoc specifications (`@param`s too)
- always insert `@throws` clause in javadoc, but insert `throws` in method signature only if particularly meaningful (or in checked exceptions obviously)



## TODO
An incomprehensive list of TODOs. Also check code TODOs.

### Java
- [ ] session persistence
- [ ] cache system (e.g. results)
- [ ] use sets instead of lists when appropriate

#### View
- [ ] multiple voting papers support
- [ ] better exception handling (provide specific dialogs)
- [ ] check and deny multiple insertions of the same choice during creation of a voting paper (Map.put retval)
- [ ] improve subvoting paper creation dialog (remove esc to quit and fix grid refresh)
- [ ] log access to specific roles
- [ ] home/welcome page
- [ ] login timer
- [ ] voting session timer
- [ ] better interface in general


#### Model
- [ ] voting stations and managers
- [ ] blank votes
- [ ] better session state (`!ready != concluded`)
- [ ] picture support

### Shell script
- [ ] try to avoid using sudo (move postgresql lock file?)

### Other
- [ ] Dockerfile
