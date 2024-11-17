# Conference and Anatony
The document provides the compiled .class files of the source code, test datasets for conference and anatomy, a library package for ontology parsing and matching, the compiled similarity matrix, and the WordNet tool.
## 1.Environment Configuration
This project is running on IntelliJ IDEA, with the SDK version set to Oracle OpenJDK version 17, and the SDK language configured as SDK default (17).
## 2.Parameter configuration
The parameter settings of this project are consistent with those in "Collaborative Ontology Matching with Dual Population Genetic Programming and Active Meta-Learning."
## 3.Address Configuration
The addresses that need to be modified and configured for this project include: the ontology file path, the similarity matrix path, and the row and column information of the matrix.  
In addition, the function call can be implemented by modifying the identifier set of the tree nodes, and the classifier call can be implemented by modifying the method statements of the classifier.
## 4.Related Description
The **Parse package** contains ontology parsing methods used to retrieve information such as the ontology's ID, label, comment, subclass and superclass, related properties, and standard alignment.  
The **similarity package** includes methods for similarity calculation, which are used to compute or read/write similarity matrices.  
The **math package** includes various mathematical functions for the construction and classification layers.  
The **Tree package** provides methods for constructing grammer trees, defining tree nodes, and encoding/decoding grammer trees.  
The **newFitness package** stores information about the fitness function, which is used to obtain PSA, calculate the fitness function, and retrieve matching results.  
The **evolutionaryAlgorithms package** contains evolutionary methods for selection, deterministic crossover, random mutation, and enhancing population diversity operations.  
The **wRF package** is used to generate decision tree nodes, train a weighted random forest classifier, and determine optimal weights.  
The **MachineLearning package** contains various classification algorithms.  
The **AML package** includes active meta-learning methods for identifying suspicious matching pairs, aggregating expert votes, and updating PSA.  
The **main package** contains the main function, defining the overall framework of DPGP-AML and serving as the entry point of the program. It also provides the property method used for matching the ontology's properties. 

# Large Biomedic  
The document provides the compiled .class files of the source code, test datasets for large biomedic, a library package for ontology parsing and matching, and the WordNet tool. Due to the similarity matrix file being too large, exceeding GitHub's transfer limits, it has not been included in this document.
## 1.Environment Configuration
This project is running on IntelliJ IDEA, with the SDK version set to Oracle OpenJDK version 17, and the SDK language configured as SDK default (17).
## 2.Parameter configuration
The parameter settings of this project are consistent with those in "Collaborative Ontology Matching with Dual Population Genetic Programming and Active Meta-Learning."
## 3.Address Configuration
The addresses that need to be modified and configured for this project include: the ontology file path, the path of the block similarity matrix, the row and column information of the matrix, the scale information of the blocks, and the PSA path.  
In addition, the function call can be implemented by modifying the identifier set of the tree nodes, and the classifier call can be implemented by modifying the method statements of the classifier.
## 4.Related Description
To improve the operational efficiency under large-scale ontologies, directly read the precomputed and stored similarity matrix. The relevant ontology parsing and similarity matrix methods are the same as those in "Conference and Anatomy."  
The **math package** includes various mathematical functions for the construction and classification layers.  
The **Tree package** provides methods for constructing grammer trees, defining tree nodes, and encoding/decoding grammer trees.  
The **newFitness package** stores information about the fitness function, which is used to calculate the fitness function, and retrieve matching results.  
The **evolutionaryAlgorithms package** contains evolutionary methods for selection, deterministic crossover, random mutation, and enhancing population diversity operations.  
The **wRF package** is used to generate decision tree nodes, train a weighted random forest classifier, and determine optimal weights.  
The **MachineLearning package** contains various classification algorithms.  
The **AML package** includes active meta-learning methods for identifying suspicious matching pairs, aggregating expert votes, and updating PSA.  
The **main package** contains the main function, defining the overall framework of DPGP-AML and serving as the entry point of the program. It also integrates information and addresses from the preprocessing files, corresponding read and write methods, as well as testing methods in non-interactive mode.

# Beneficial Effect  
The matching results of high quality can be obtained under various heterogeneous ontologies and different expert error rates.
