# FOL Resolver
Implements inference using Resolution, a prove by contradiction approach for First Order Logic Statements 

##Resolution
* Resolution is a method of inference leading to a refutation theorem-proving technique for sentences in propositional logic and first-order logic(FOL)     
* In other words, iteratively applying the resolution rule along with unification in a suitable way allows for inferencing whether a FOL statement is satisfiable or unsatisfiable     
* Attempting to prove a satisfiable FOL statement as unsatisfiable may result in a non-terminating computation; this problem doesn't occur in propositional logic     
* Resolution works on statements in Conjunctive Normal Form(CNF) and hence FOL statements are first converted to CNF     
* Resolution is a complete and sound inference procedure because it works on CNF which is universal  

##How to Execute:
KB should be in the following form:
* An implication of the form p1 ∧ p2 ∧ ... ∧ pm ⇒ q, where its premise is a conjunction of literals and its conclusion is a single literal. Remember that a literal is an atomic sentence
  or a negated atomic sentence.
*  A single literal: q or ~q

KB and queries are given as input to the Inference Engine  
1st Line consists of the number of queries, followed by the queries themselves    
Subsequent lines contain the number of statements in the KB followed by the KB

Output is either TRUE or FALSE  
True if the query can be inferred from the given KB  
False if the query cannot be inferred from the given KB

##Example
Input-  
>2  
Alert(Bob,NSAIDs)  
Alert(Bob,VitC)  
5  
Take(x,Warfarin) => ~Take(x,NSAIDs)  
HighBP(x) => Alert(x,NSAIDs)  
Take(Bob,Antacids)  
Take(Bob,VitA)  
HighBP(Bob)   

Output should be -  
>TRUE   
FALSE
 