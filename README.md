osgi-unit
=========

Unit testing libraries for OSGi projects.

The core class is WithOSGi: this is a JUnit rule that acts as a container 
for an OSGi framework instance that unit tests can use to access resources
(such as services) within the OSGi framework.

The framework can be initialised by hand or using the Gradle plugin incorporated
in this library to initialise the framework.
