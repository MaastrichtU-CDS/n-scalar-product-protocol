# N-party scalar product protocol

This repo contains an implementation for te n-party scalar product protocol. 
The python version is purely local. The java version has been converted into a fullblown webservice and can be run as a spring-boot project.

InitData and InitRandom are dummy methods, these should be overrriden for your specific project.
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
If you want to calculate the scalar product protocol of vectors containing decimal-values the following approach can be used:
1) Pick a precision, e.g. 5 decimals.
2) Create your precisionMultiplier by calculating 10^precision +1, in our example this would be 10^6. 
3) Multiply all your data by your precisionMultiplier & then round these numbers to the nearest integer
4) Perform the n-party-scalar-product protocol as normal on these large integers
5) Divide the result by precisionMultiplier^n, where n is the number of parties involved in the protocol that contain decimal values. This will 
result in the final result that is accurate up to the selected amount of decimals.

# Handling a Hybird split
To handle a Hybrid split in your data include an attributecolumn in all relevant datasets named "locallyPresent" with "bool" as it's type. Locally available data should have the value "TRUE". Missing records are then inserted as a row that has the value "FALSE" for this attribute. This should be handled in a preprocessing step.

Important to note; datasets still need to have the same ordering for their records. It is assumed that recordlinkage is handled in a preprocessing step as well.

This functionality is only available in the java implementation.