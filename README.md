# DPGP-AML
## Test Dataset
The document provides the **conference** and **anatomy** test datasets. To download and test other datasets, please visit the [OAEI](https://oaei.ontologymatching.org/).
## Program Configuration
The document provides the required **libraries** for ontology parsing and matching, as well as the **pre-stored similarity matrix files**. Readers are advised to configure the current environment's **JDK** before running.
## Ontology Parsing
The **path** of the ontology file needs to be modified before parsing, including the **source ontology** and the **target ontology**.
```
String s1 = "D:\\paper1\\DPGP-AML\\conference-dataset\\edas.owl";
```
To select the content for parsing from the ontology file, such as **class, property, or reference**, readers need to modify the corresponding **method**.
```
List<List<String>> source1 = Parse_Class.Find_Class(s1);
List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
List<String> reference = Parse_Algnment.parseRefalignFile(s3);
```
## Similarity Methods
To improve the execution speed, the similarity matrix needs to be preloaded during the **decoding** phase. Readers are required to modify the **file name** of the similarity matrix, the corresponding **test cases**, and the **row and column parameters** of the matrix.
```
 if (linkedTree.get(i).getStr().equals("NGram")) {
linkedTree.get(i).setValue(readWuMatrix.read("cmt-Conference-NGram", 29, 59));}         
```
You can use the **read method** under the **readWuMatrix module** to read the row and column information of the matrix.
To modify the similarity matrix, you can recalculate it and use the **writeFile method** under the **readWuMatrix module** to write the matrix information. 
Note that the similarity measure based on **linguistic methods** requires downloading [WordNet](https://wordnet.princeton.edu/).
## Main
The **character set** can be configured before the main function runs, in order to restrict the operators in the GP trees.
```
String[] str1 = {"top1", "top30", "top50"};
String[] str2 = {"add", "sub", "mul", "div", "max", "min", "avg"};
String[] str3 = {"NGram", "SMOA", "wuAndPalmer", "sonGreedy"};
```
The main function can configure the **parameters** of the DPGP-AML method.
You can modify the classification method in the program for **updating PSA**. The document provides corresponding methods under the **MachineLearning module**.
```
double[][] QMS_EV = voteAndUpdate.getQMS_EV(errorRate, expertNumber, QMS, reference);
List<String> updatePSA = voteAndUpdate.updatePSA(QMS, PSA, RF, optimalWeight, QMS_EV);
```
The readers can further match the **properties** of the ontology through the matching results of the **class**. You can use the **property method** under the **Main module** to perform this operation.
```
for (int i = 0; i < sourceObject.get(3).size(); i++) {
            String[] domainAndRangeOfSourceObject = splitString(sourceObject.get(3).get(i));
            if (sourceClass.equals(domainAndRangeOfSourceObject[0]) || sourceClass.equals(domainAndRangeOfSourceObject[1]))
                sourcePropertySet.add(sourceObject.get(1).get(i));
        }
for (int i = 0; i < targetObject.get(3).size(); i++) {
            String[] domainAndRangeOfTargetObject = splitString(targetObject.get(3).get(i));
            if (targetClass.equals(domainAndRangeOfTargetObject[0]) || targetClass.equals(domainAndRangeOfTargetObject[1]))
                targetPropertySet.add(targetObject.get(1).get(i));
        }
```
The document provides a **tokenization** strategy in the **ExcludeMethods method** under the **Main module** for further fine-grained matching.
