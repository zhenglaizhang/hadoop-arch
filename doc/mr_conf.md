

## Resources

* This is used in Hadoop to separate out the default properties for the system, defined internally in a file called core-default.xml, from the site-specific overrides in core-site.xml.
* Properties defined in resources that are added later override the earlier definitions
* properties that are marked as final cannot be overridden in later definitions
* Administrators mark properties as `final` in the daemon’s site files that they don’t want users to change in their client-side configuration files or job submission parameters.
* Variable expansion
    * Configuration properties can be defined in terms of other properties, or system properties.
    * System properties take priority over properties defined in resource files:
    * useful for overriding properties on the command line by using `-Dproperty=value` JVM arguments.