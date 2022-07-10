# SVeSE - Sistema di Voto e Scrutinio Elettronico


## Code etiquette
In general, follow the style of the already present code. Some details worth pointing out are:
- use `this.*` only if necessary or for clarity. This may be subject to change
- period dot after javadoc specifications (`@param`s too)



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
