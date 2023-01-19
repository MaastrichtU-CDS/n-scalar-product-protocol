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

    **It is important to ensure that your data is rounded properly with no trailing values after your defined precision. Trailing values can result in rounding errors which reduce the precision**
4) Perform the n-party-scalar-product protocol as normal on these large integers
5) Divide the result by precisionMultiplier^n, where n is the number of parties involved in the protocol as dataproviders. This will result in the final result that is accurate up to the selected amount of decimals.

# Handling a Hybird split
To handle a Hybrid split in your data include an attributecolumn in all relevant datasets named "locallyPresent" with "bool" as it's type. Locally available data should have the value "TRUE". Missing records are then inserted as a row that has the value "FALSE" for this attribute. This should be handled in a preprocessing step.

Important to note; datasets still need to have the same ordering for their records. It is assumed that recordlinkage is handled in a preprocessing step as well.

This functionality is only available in the java implementation.

# Data format:
The following file-types can be used as databases: 
- csv
- arff
- parquet

In the case of a csv two header rows are expect. Row 1 contains attribute types, row 2 contains attribute names.
A script to convert from CSV to parquet can be found in ./script.

# Python v.s. Java
The python version was originally developed to compare run-time performances and the ease of implementation within vantage6. It is currently several iterations behind and does not have all funcitonality.

# How to use this code as a library
To use this project as a library can simply include the jar as a dependency in your maven project.
For examples of how to include it in your own project please have a look at the following projects:

- https://github.com/MaastrichtU-CDS/vertibayes
- https://github.com/MaastrichtU-CDS/bayesianEnsemble
- https://github.com/CARRIER-project/verticox

Vertibayes and BayesianEnsemble are pure java projects that use the n-party protocol as a library. These projects show how to initiallize the data correctly and utilize the calculations to perform the K2 algorithm as well as the Maximum likelihood for a bayesian network.
Verticox is a mixed java-python project, where the java project purely exists to open the the n-party protocol library functionality for use within the python code. The python code determines which data is to be used and then triggers the calculation.