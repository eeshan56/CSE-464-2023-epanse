# CSE-464-2023-epanse

Command to download the code:
$ git clone https://github.com/eeshan56/CSE-464-2023-epanse

After this, open the Project folder "CSE-464-2023-epanse" in IntelliJ. Run Main.java that is present in src/main/java/org/example/ folder

File Structure:

![image](https://user-images.githubusercontent.com/42989115/228449072-3fc0942e-8457-439b-b655-7ae8a3e45dd3.png)

Main.java - Runs the functionality
GraphManager.java - Contains main methods that are relevant to the Features
Path.java - class suggested as per file
pom.xml - contains all dependencies to run project

Screenshots:
Run Main.java :-

![image](https://user-images.githubusercontent.com/42989115/228449486-4a61a737-1ac4-476a-a9e9-827b68b4df12.png)

1) Parsing Graph: Enter 1
![image](https://user-images.githubusercontent.com/42989115/228449615-3cecf43d-4c6e-4f32-9654-fca819ce6f63.png)

2) Output Nodes and Graphs: Enter 2

![image](https://user-images.githubusercontent.com/42989115/228449869-e4c6e137-8e88-4c18-8add-9d2bf0850758.png)

And so on for all the other features

3) Get png of graph: Enter 8

![image](https://user-images.githubusercontent.com/42989115/228450221-b1b1ee1c-3961-4506-89f4-65b58b592131.png)
![image](https://user-images.githubusercontent.com/42989115/228450420-2894b5b0-f7be-4d87-87a9-e513babb5f9f.png)


5) Get BFS & DFS: Enter 9

![image](https://user-images.githubusercontent.com/42989115/228450644-b869b7a6-5b61-4c26-8321-4051fb60e802.png)

![image](https://user-images.githubusercontent.com/42989115/228450704-45d52da8-9ddc-4ba0-b101-e91488f7c957.png)

Workflow Results:
![image](https://user-images.githubusercontent.com/42989115/232683852-630365f8-5ae5-46c2-a650-51d245725d96.png)

### Important Commits for Project Part-2:
https://github.com/eeshan56/CSE-464-2023-epanse/commit/7e245d1872719e2583841006cc29601f1a79ecef
https://github.com/eeshan56/CSE-464-2023-epanse/commit/f2bd7888d334268cbaffab6c978f88c9071a2b34
https://github.com/eeshan56/CSE-464-2023-epanse/commit/92ab1009e93e61e7ca5f37d601755d112d5eb8ad
https://github.com/eeshan56/CSE-464-2023-epanse/commit/6f0ad9d99ee3e5243dd8aa24b72a9a5a197ec9e1
https://github.com/eeshan56/CSE-464-2023-epanse/commit/d67c649427ca29e48d0bc6cb5b17753189885c6f
https://github.com/eeshan56/CSE-464-2023-epanse/commit/d89798e524fa065ac4bd090c4c0bf72703808178
https://github.com/eeshan56/CSE-464-2023-epanse/commit/272fdf92b47147757c3d798b1e37702c02ef060f
https://github.com/eeshan56/CSE-464-2023-epanse/commit/c4809b993a03e783275b7dc137437ffcc2478cee

### Part 3:

#### a) Refactoring:

1) [Refactor 1: Changed if-else to ternary operations (commit link)](https://github.com/eeshan56/CSE-464-2023-epanse/commit/12d7674ddfd5ff4a3a76a106c6405e2d8a1370e9)

In this code refactoring, redundant if-else conditions were made inline:

    isDuplicate = false
    .
    .
    .
    if(node.name().equals(nodeName))
        isDuplicate = true

This was converted to:

    isDuplicate = (!isDuplicate) ? node.name().equals(nodeName) : true;

2) [Refactor 2: Added String Constants in place of String values (commit link)](https://github.com/eeshan56/CSE-464-2023-epanse/commit/83148dc1c78e987dc66a71b67a2962e14bf91195)

In this code refactoring, String literal values like "bfs", "png" were replaced by storing
them in constant variables like:

    public static final String DEFAULT_DOT_FILEPATH = "src/main/java/org/example/color.dot";
    public static final String REGEX_UNDIRECTED_CONNECTOR = "--";
    public static final String REGEX_DIRECTED_CONNECTOR = "-->";
    public static final String JPG_STR = "jpg";
    public static final String PNG_STR = "png";
    public static final String BFS_STR = "bfs";

These constants were then used throughout the Main and GraphManagerTest classes to ensure
re-usability.

3) [Refactor 3: Removed Dead Code and Unnecessary Repetition (commit link)](https://github.com/eeshan56/CSE-464-2023-epanse/commit/a31747dce4a0379aebfbab32e353dfbed7b019ee)

In this code refactoring, commented code that was dysfunctional and of no use was removed.
Also, unnecessary initialization of the variable "defaultPngFilename" was corrected to initializing
it just once at the start of the execution and using the same value throughout all features.

4) [Refactor 4: Rename Variables (commit link)](https://github.com/eeshan56/CSE-464-2023-epanse/commit/4c2e08a0a952dea15b21d279c5cf5186d0f02e63)

In this code refactoring, variables in the for loop iterations like:

    for(MutableNode node : nodeSet) {}

were updated to:

    for(MutableNode currNode : nodeSet) {}

Or:

    for(MutableNode currPathNode : currPath) {}

5) [Refactor 5: Extracted method to check if graph is uninitialized (commit link)](https://github.com/eeshan56/CSE-464-2023-epanse/commit/810728f9d9aae98b59978f8b36c1325399886454)

In this code refactoring, similar operations that were repeatedly being executed
were added to a function and that function was called each time those set of operations
were needed. It was the case of checking if the graph had been initialized or not:

    if(this.graph == null || this.dotFilePath == null) {
        this.initGraph();
    }

These steps were added to a function:

    public void checkIfGraphIsNull() {
        if(this.graph == null || this.dotFilePath == null) {
            this.initGraph();
        }
    }

#### b) Design Pattern for BFS/DFS: [commit link](https://github.com/eeshan56/CSE-464-2023-epanse/commit/10fa16e05fe2694d1ddbdf226c9c90d93ddd2784)

In this feature, I applied the Template pattern to extract common behaviors of BFS and DFS and create separate classes to implement the differences between the two search algorithms.

The GraphSearchTemplate class is the abstract base class that defines the common algorithmic steps for graph search. This class contains a public method graphSearch that defines the template algorithm for graph search. This method takes in a source node, a destination node, and a search algorithm as input parameters.

The BreadthFirstSearch and DepthFirstSearch classes are concrete subclasses of GraphSearchTemplate that implement the specific differences between BFS and DFS. These classes override the graphSearch method and provide their own implementation of the search algorithm.

By using the Template pattern, I was able to extract the common algorithmic steps of graph search and implement the specific differences between BFS and DFS in separate classes. This approach makes the code more modular, easier to read and maintain, and allows us to easily add new search algorithms in the future.

To use these classes, simply create an instance of the BreadthFirstSearch or DepthFirstSearch class, and call the graphSearch method with the appropriate parameters.

Example Usage
Here's an example of how to use BreadthFirstSearch and DepthFirstSearch classes to perform a graph search:

    MutableNode src = new MutableNode("A");
    MutableNode dst = new MutableNode("D");

    GraphSearchTemplate bfs = new BreadthFirstSearch();
    Path bfsResult = bfs.GraphSearch(src, dst);
    
    GraphSearchTemplate dfs = new DepthFirstSearch();
    Path dfsResult = dfs.GraphSearch(src, dst);

#### c) Strategy Pattern for BFS/DFS:

To implement the ability to choose between Breadth First Search (BFS) and Depth First Search (DFS)
in the GraphSearch method, I applied the strategy pattern. I created an interface
Algorithm to define the common behavior of the search algorithms, and the abstract class
GraphSearchTemplate that implements this interface to define the specific behavior for BFS and DFS, respectively.

The GraphSearch method of GraphManager class was then modified to take an Algorithm
parameter that determines which algorithm to use, and creates an instance of either
BreadthFirstSearch or DepthFirstSearch accordingly. This instance is then passed to
GraphSearchTemplate's search method, which calls the useQueue method to determine whether
to use a queue or stack, and performs the search accordingly.