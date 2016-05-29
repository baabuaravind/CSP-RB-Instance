# CSP-RB-Instance
Artificial Intelligence

Write a program that generates a random consistent binary CSP and solves it using the techniques
listed in Section 2.

1 Binary CSP Instances

Binary CSP instances should be randomly generated using the model RB proposed in [1]. The choice of
this model is motivated by the fact that it has exact phase transition and the ability to generate asymp-
totically hard instances. More precisely, you need to randomly generate each CSP instance as follows
using the parameters n, p,  and r where n is the number of variables, p (0 < p < 1) is the constraint
tightness, and r and  (0 < r;  < 1) are two positive constants used by the model RB [1].
1. Select with repetition rn ln n random constraints. Each random constraint is formed by selecting
without repetition 2 of n variables.
2. For each constraint we uniformly select without repetition pd2 incompatible pairs of values, where
d = n is the domain size of each variable.
3. All the variables have the same domain corresponding to the rst d natural numbers (0 : : : d 􀀀 1).
According to [1], the phase transition pt is calculated as follows: pt = 1 􀀀 e􀀀=r. Solvable problems
are therefore generated with p < pt.


2 Solving Techniques

The following backtrack search strategies should be implemented.
BT Standard Backtracking.
FC Forward Checking.
FLA Full Look Ahead (also called MAC).
In addition, the user should be given the option to run Arc Consistency (AC) before the actual
backtrack search.


3 Input and Output

3.1 User Input
 n, p,  and r (to generate the CSP instance).
 The chosen solving strategy (BT, FC or FLA) with or without AC before the search.
3.2 User Output
 The CSP instance.
 The solution to the CSP instance.
 The time needed to solve the instance.
1


4 Example of an RB Instance

4.1 Input Parameters
 Number Of Variables (n): 4
 Constraint Tightness (p): 0.33
 Constant : 0.8
 Constant r: 0.7
4.2 Generated RB Instance
 Domain Size (n): 3
 Number Of Constraints (rn ln n): 4
 Number of Incompatible Tuples (pd2): 3
Variables : {X0, X1, X2, X3}
Domain : {0, 1, 2}
Constraints: Incompatible Tuples
(X2,X3): 1,2 2,2 2,0
(X1,X2): 1,0 2,0 2,1
(X3,X1): 2,2 0,2 2,0
(X0,X2): 2,2 2,1 1,2
