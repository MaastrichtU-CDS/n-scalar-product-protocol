# N-party scalar product protocol

This repo contains an implementation for te n-party scalar product protocol. 
The python version is purely local. The java version has been converted into a fullblown webservice and can be run as a spring-boot project.

InitData and InitRandom are dummy methods, these should be overrriden for your specific project settings.
For an example of a project using this library see https://gitlab.com/fvandaalen/vertibayes

# Project setup
The project requires the following properties:
```
servers=<list of urls for the other servers>
secretServer=<url to the commodity server>
server.port=<port to be used>
server=<server id>
```
