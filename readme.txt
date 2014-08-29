Running my program is similar to the one given.
Run the Gui main. Choosing a configuration by uncommenting a line.
The tests for correctness can be run as a JUnit test.
The tests for effeciency are a class with a main, which report results to the command line.

The project brief suggested that we only add a ModelParallel class, however to minimally modify
the other classes, I made an abstract class AbstractModel which is extended by both Model 
(the original) and ModelParallel. Therefore in other classes I simply had to make them deal
with AbstractModels rather than just the sequantial Model class.