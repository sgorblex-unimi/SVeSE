# SVeSE - Sistema di Voto e Scrutinio Elettronico



## Utility script
The script `SVeSE.sh` contains a series of utility functions for dealing with the system. See `SVeSE.sh --help`.



## Data


### Importing people data
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


### Session administrator
You need to set the session administrator to a valid person in the database. To do that, just create a file called `admin.txt` in the system directory which contains the administrator's SSN:
```sh
echo "DAD52" > admin.txt
```



## Code etiquette
In general, follow the style of the already present code. Some details worth pointing out are:
- use `this.*` only if necessary or for clarity. This may be subject to change
- period dot after javadoc specifications (`@param`s too)
- always insert `@throws` clause in javadoc, but insert `throws` in method signature only if particularly meaningful (or in checked exceptions obviously)



## TODO
An incomprehensive list of TODOs. Also check code TODOs.

### Java
- [ ] use sets instead of lists when appropriate
- [x] actual implementations
- [ ] persistence
- [x] login role mechanism

#### Model
- [ ] voting stations and managers
- [x] guarantors
- [ ] blank votes

#### Interface
- [ ] work with model

### Shell script
- [ ] try to avoid using sudo (move postgresql lock file?)

### Other
- [ ] Dockerfile
