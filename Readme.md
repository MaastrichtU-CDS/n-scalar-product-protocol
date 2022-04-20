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
# Handling decimals
This implementation works with BigInteger, and thus expects integers as its input.
If you want to calculate the scalar product protocol of vectors containing decimals the following approach can be used:
1) Pick a precision, e.g. 5 decimals.
2) Multiply all decimals by 10^precision, in our example this would be 10^5. This number is your precisionMultiplier
3) Perform the n-party-scalar-product protocol as normal
4) Divide the result by precisionMultiplier^n, where n is the number of parties involved in the protocol. This will 
result in the final result that is accurate up to the selected amount of decimals.