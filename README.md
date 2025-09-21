# Assignment 1 -- Implementing Interfaces and Testing

The learning goals of this assignment are:

- Learn the basics of implementing an interface in Java.
- Read a specification and implement it.
- Write JUnit tests to test your implementation.
- Learn to use coverage tools such as JaCoCo and PIT.

Note: **This assignment has a self-review component. See the Submission section.**

## Introduction

A text document is an artifact that contains textual
data in human-readable form such as a txt file. 

In this assignment, we will design an object that 
can encapsulate textual content and operations to 
work on the content.

## Specification

The object is defined as the interface `TextDocument.java`
in `src/main/java/document`. Read the javadocs of the interface
before starting writing code. Specifically, the interface 
provides the following methods:

- `String getText()`. This method returns the texutal content in the
    text document as a string.
- `int getWordCount()`. This method returns the number of words in the
    text document. A word is a sequence of characters separated by whitespace/s.
- `String minusDiff(TextDocument other)`. This method takes another text document
    as input and returns a string containing the sequence of characters that 
    when removed from the document, the remaining ones are a subsequence of the other document.
    The idea is similar to the diff utility tool that reports the characters that
    need to be removed the make the document similar with the one that is being used for comparison.
- `String plusDiff(TextDocument other)`. Like `minusDiff`, except it returns
    the sequence of characters in `other` that need to be added to the text document
    to make it similar to `other`.

For e.g., suppose we are comparing two text documents D1 and D2 with content
"Hello World" and "Helo Word!", respectively. Calling D1.minusDiff(D2) will
return the sequence "ll" as removing it will make D1' a subsequence of D2. 
Further, since adding "!" to D1' will make it the same as D2, D1.plusDiff(D2)
should return "!".

## For you to Do

You are to write an implementation of the `TextDocument` interface called `TextDocumentImpl.java` in the document package.
Note the diff method implementations must use a popular algorithm called the 
longest common subsequence algorithm described in the next section.

You must also write unit tests for your implementation in the `src/test/java/TextDocumentImplTest.java` file.
You can write additional test files if necessary but the main test file is required.

## Longest Common Subsequence Algorithm Description

Your implementation must use this algorithm. Additionally, be sure to provide a public constructor
in your implementation that takes a string as argument and initializes the 
object with the necessary attributes such as the textual content and the word count.

You will need to compute the longest common subsequence (LCS) between two text documents to determine differences between them. Note the naive search to test 
if each subsequence of length N in a given string is also a subsequence in the other string 
is exponential. Hence, you must not implement the naive algorithm. Instead, you 
should use dynamic programming to calculate the LCS in polynomial time.

Dynamic programming is an algorithmic technique that is used to solve problems with
(1) an optimal substructure, that is, dividing the problem into smaller
sub problems and (2) overlapping sub problems, that is, the solutions to the smaller
problems can be reused to solve the higher level problems.

It turns out that the finding the LCS also has the properties needed for dynamic programming.
The LCS(X^A,Y^A) = LCS(X,Y) ^ A (where ^ means concatenation) because 
both strings end with the same symbol A. Hence, the final LCS must also end with A. 
However, if A and B are distinct symbols then 
LCS(X^A, Y^B) is one of the maximal length strings in the set {LCS(X, Y^B), LCS(X^A, Y)}.

Let's look at an example to better appreciate the properties of LCS that make it perfect for
dynamic programming. The LCS("GRAPPLE", "SAPPLE") = LCS("GRAPPL", "SAPPL") ^ "E".
Continuing for the next common symbols, we get LCS("GRAPPLE", "APPLE") = LCS("GR", "S") ^ "APPLE".
Alternatively, if we consider LCS("GRAPPLE", "APPLES") and assume that the final LCS ends
with "E" then the LCS("GRAPPLE", "APPLES") = LCS("GRAPPLE", "APPLE").
However, if the final LCS ends with "S" the LCS("GRAPPLE", "APPLES") = LCS("GRAPPL", "APPLES").
We will pick the LCS that is longer.

## Implementing LCS

Implementing LCS is a two-step process:

1. **Constructing the Table**. To implement the dynamic programming algorithm to compute LCS we have first to construct a table or a 2D array.
    The table will be used to store the LCS sequence length for each step of the calculation.
    Suppose we want to determine LCS(X, Y) where X has length m and Y has length n. 
    The table will have m+1 rows, and n+1 columns. The first row and column of the table
    represents the empty string (&epsilon;). The subsequent rows indicate characters X in the same order
    as they appear in X. Likewise the columns (after the first) indicate characters in Y. For example,
    when finding LCS("GAC", "AGCAT"), a cell in following table stores the length of the LCS seen thus far. For e.g., the cell (1,2) stores the length of LCS("G", "AG").


            |             | &epsilon; | A | G | C | A | T |
            |-------------|-----------|---|---|---|---|---|
            | &epsilon;   | 0         | 0 | 0 | 0 | 0 | 0 |
            | G           | 0         |   |   |   |   |   |
            | A           | 0         |   |   |   |   |   |
            | C           | 0         |   |   |   |   |   |

So how do we populate the table? We do this by iterating over
the strings being compared. For a cell (i,j) in the table
compare the characters at the ith and jth position in the strings being
compared. If they match then we know the character must be in the
LCS. Hence, we increment the new LCS length by 1. Therefore,
the value at cell (i,j) is the value at cell (i-1, j-1) incremented by 1.
If the characters do not match then the value at cell (i,j) is the max
of the values at cell (i-1, j) and cell (i, j-1). Following this 
algorithm the table for LCS("GAC", "AGCAT") will look as 
follows:

    |             | &epsilon; | A | G | C | A | T |
    |-------------|-----------|---|---|---|---|---|
    | &epsilon;   | 0         | 0 | 0 | 0 | 0 | 0 |
    | G           | 0         | 0 | 1 | 1 | 1 | 1 |
    | A           | 0         | 1 | 1 | 1 | 2 | 2 |
    | C           | 0         | 1 | 1 | 2 | 2 | 2 |

The value at cell (3,5) is 2 which indicates the length
of LCS("GAC", "AGCAT") is 2.

2. **Calculating the Difference**. We can use this table to determine the difference between
the two strings X and Y by tracing back. So we start at the last
cell in the table, i and j, where i = 3 and j = 5. 
If X[i] = Y[j] then the character must be in the LCS and we
decrement i and j. However, if X[i] != Y[j] and the length
of LCS(X[1..i-1], Y[1..j]) > the length of LCS(X[1..i], Y[1..j-1])
then we collect X[i] as the character that needs to be removed. 
We also decrement i by 1. Although if the length
of LCS(X[1..i-1], Y[1..j]) <= the length of LCS(X[1..i], Y[1..j-1])
then we decrement j by 1 and consider Y[j] as a character
that needs to be added to X. We stop tracing when we 
have seen all cells in the table necessary to construct
the LCS.

## Testing

You are expected to test your code extensively. You must write Junit test for
every case that you verified. Your test cases must be derived from the given 
specification and the implementation must derived from the unit tests in written.
This is the essence of TDD.

You will be evaluated on the completeness of your tests and its correctness. 
You are expected to use tools such as jacoco and the PIT mutation coverage to
assess your test suite.

## Documentation

Documentation is a key aspect of this course. We expect your submission to be
documented as follows:

- Each interface and class contains a comment above it explaining specifically 
    what it represents. This should be in plain language, understandable by 
    anybody wishing to use it. Comment above a class should be specific: 
    it should not merely state that it is an implementation of a particular 
    interface.
- Each public method in the interface should have information about what this method accomplishes (purpose), the nature and explanation of any arguments, return values and exceptions thrown by it and whether it changes the calling object in any way (contract).
- If a class implements a method declared in an interface that it implements, and the comments in the interface describe this implementation completely and accurately, there is no need to replicate that documentation in the class.
- All comments should be in Javadoc-style.

## Grading Criteria

Your final grade will be based on the aspect such as:

- The correctness of your implementation.
- The completness and correctness of your test suite.
- Adequate documentation that aligns with the style rules.
- Appropriate access control.
- Your code aligns with established design principles and practices.

## Development Environment

The project is setup as a gradle project (similar to lab 1). The gradle
tasks are configured in the `build.gradle` file. You
can look at the file to see the list of tasks allowed.
We highly recommend that you do not change this file
unless you know what you are doing. You can run the following
commands from a terminal:

1. Clean and build will run the checkstyle and the tests
    
    `$ ./gradlew clean build`
    
    You can view the style report for the source code by opening the file `build/reports/checkstyle/main.html` in a browser.
    You can view the style report for the test code by opening the file `build/reports/checkstyle/test.html` in a browser.
2. Run only tests
    
    `$ ./gradlew test`

    You can view the test report by opening the file `build/reports/tests/test/index.html` in a browser.
    You can view the jacoco coverage report by opening the file `build/reports/jacoco/test/html/index.html` in a browser.
3. Run PIT mutation
    
    `$ ./gradlew pitest`
   
    You can view the test report by opening the file `build/reports/pitest/index.html` in a browser.

If you are on Windows replace the `./gradlew` with `./gradlew.bat`.

If you do not want to use the terminal open
the gradle tool window under **View -> Tool Windows -> Gradle**. 
Select the gradle task that you want to run from the gradle menu.

## Submission

Submit your code to ypur private git repository on Pawtograder. Review the instructions [here](https://docs.pawtograder.com/students/assignments/create-submission).
Remember submitting to Pawtograder is the same as pushing to a remote git repository. Here are some useful commands for the terminal:

```bash
git add <files>  # or use . to add all files
git commit -m "your commit message"
git push
```

Note the following for your submission:

- Fix any style errors before submitting code, otherwise your submission will not be considered for grading. 
- Follow the expected naming conventions for classes and test files. Otherwise, your submission will not be considered for grading.
- You can push code up to three times an hour. The rate limiting is to encourage you to test your code locally before pushing.
- When you push code, your solution will be graded against instructor tests. Some will be visible to you and some will not. You will get feedback on the tests that are visible to you. For hidden tests, you will only if they passed or failed.
- The tests you write will be assessed for completeness and correctness using mutation testing and code coverage.
- You can push code multiple times before the deadline. The last submission before the deadline will be considered for grading.
- You can take up to 1 late token for this assignment. Read how late tokens are applied [here](https://docs.pawtograder.com/students/assignments/late-tokens).
- ** There is a self-review component to this assignment. You must complete the self-review 24 hrs after the due date. ** Read how to take a self review [here](https://docs.pawtograder.com/students/assignments/self-review).