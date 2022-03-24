# Design assessment


```
<The goal of this document is to analyse the structure of your project, compare it with the design delivered
on April 30, discuss whether the design could be improved>
```

# Levelized structure map
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the Levelized structure map,
with all elements explosed, all dependencies, NO tangles; and report it here as a picture>
```

### 
![LSM](/ezshop-master//Deliverables/immagini/LSM.png)


# Structural over complexity chart
```
<Applying Structure 101 to your project, version to be delivered on june 4, produce the structural over complexity chart; and report it here as a picture>
```

### 
![StructuralOverComplexityChart](/ezshop-master//Deliverables/immagini/StructuralOverComplexityChart.png)


# Size metrics

```
<Report here the metrics about the size of your project, collected using Structure 101>
```



| Metric                                    | Measure |
| ----------------------------------------- | ------- |
| Packages                                  |    5    |
| Classes (outer)                           |    52   |
| Classes (all)                             |    52   |
| NI (number of bytecode instructions)      |   8959  |
| LOC (non comment non blank lines of code) |   3852  |



# Items with XS

```
<Report here information about code tangles and fat packages>
```

| Item | Tangled | Fat  | Size | XS   |
| ---- | ------- | ---- | ---- | ---- |
| ezshop-master.it.polito.ezshop.data.EZShop |         |   179   |   2994   |   986   |
|      |         |      |      |      |



# Package level tangles

```
<Report screen captures of the package-level tangles by opening the items in the "composition perspective" 
(double click on the tangle from the Views->Complexity page)>
```
There are no package level tangles in our project.

# Summary analysis
```
<Discuss here main differences of the current structure of your project vs the design delivered on April 30>
<Discuss if the current structure shows weaknesses that should be fixed>
```
Main differences with design delivered on April 30: main differences with design delivered on April 30 are some classes and methods (TicketEntry, Database) that have been added. Order, SaleTransaction and ReturnTransaction don't extend BalanceOperation anymore.
Possible weakness to be fixed: fat classes, backward dependency between Database and EZShop which can be avoided through a more complex database management.  We could add at least one more package for sake of modularity (in main package) but we were forced to keep in etc.data: ezshop.class and its interface, because of given testing (and main) script code. We decided to keep whole model and data here.
