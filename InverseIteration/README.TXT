The inverse iteration algorithm was primarily copied from the Power-Method pdf. However, there are a few differences. 

1)The shift was not used since we're interested in the smallest eigenvalue.
2)The inverse was not actually calculated. y was calculated via a forward and back substitution which was accomplished through a Cholesky decomposition of A. We're allowed to do this since it's symmetric positive definite.
3)The convergence criteria was the difference between two consecutive eigenvalues. If this difference is less than 0.001, stop.

How to compile/run

1) Extract the .java file to some directory.
2) Navigate to that directory and type "javac InverseIteration.java"
3) Run with "java InverseIteration"
