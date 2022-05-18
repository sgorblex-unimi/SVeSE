# SVeSE - Sistema di Voto e Scrutinio Elettronico


## Code etiquette
In general, follow the style of the already present code. Some details worth pointing out are:
- use `this.*` only if necessary or for clarity. This may be subject to change
- period dot after javadoc specifications (`@param`s too)



## TODO
An uncomprensive list of TODOs. Also check code TODOs.

### Java
- [ ] use sets instead of lists when appropriate
- [ ] immutable choice; votingpaper can be composite, in which case it has a map mapping valid choices to the respective sub-votingpaper
- [ ] actual implementations

### Shell script
- [ ] try to avoid using sudo (move postgresql lock file?)
