

## Maven

* Build tool
* Project management tool

## Common problems and activites

* Multiple jars
    * include all spring jars to my proj? both at compile time and distribution bundle time
    * what are these jars?
* Dependencies and version
    * all dependent jars?
    * matching the right versions?
* Project structure
    * web app?
* Building, publishing and deploying

```bash
wget ...mavrn dist
export M2_HOME=...
export PATH=$(PATH):...

mvn --version

mvn archetype:generate

```

* group id (like package name)
* artifact id  (like app name)
* version (1.0-SNAPSHOT by default)

No need to worry about indirect dependencies of direct dependency in pom, maven will handle that for us