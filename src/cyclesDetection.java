
import java.util.*;

public class cyclesDetection {
    Set<Integer> blockedSet;
    Map<Integer, Set<Integer>> blockedMap;
    Deque<Integer> stack;
    Deque<Integer> gainstack;
    List<pair> allCycles;

    static class pair{
        List<Integer> list;
        int gain;

        public pair(List<Integer> list, int gain) {
            this.list = list;
            this.gain = gain;
        }
    }
    public List<pair> simpleCyles(Graph graph) {

        blockedSet = new HashSet<>();
        blockedMap = new HashMap<>();
        stack = new LinkedList<>();
        allCycles = new ArrayList<>();
        gainstack = new LinkedList<>();
        int startIndex = 1;
        TarjanStronglyConnectedComponent tarjan = new TarjanStronglyConnectedComponent();
        while(startIndex <= graph.getsize()) {
            Graph subGraph = createSubGraph(startIndex, graph);
            List<Set<Integer>> sccs = tarjan.scc(subGraph);

            Optional<Integer> maybeLeastVertex = leastIndexSCC(sccs, subGraph,graph);
            if(maybeLeastVertex.isPresent()) {
                Integer leastVertex = maybeLeastVertex.get();
                blockedSet.clear();
                blockedMap.clear();
                findCyclesInSCG(leastVertex, leastVertex,1,subGraph);
                startIndex = leastVertex+ 1;
            } else {
                break;
            }
        }
        return allCycles;
    }

    private Optional<Integer> leastIndexSCC(List<Set<Integer>> sccs, Graph subGraph,Graph graph) {
        long min = Integer.MAX_VALUE;
        Integer minVertex = null;
        Set<Integer> minScc = null;
        for(Set<Integer> scc : sccs) {
            if(scc.size() == 1) {
                continue;
            }
            for(Integer vertex : scc) {
                if(vertex < min) {
                    min = vertex;
                    minVertex = vertex;
                    minScc = scc;
                }
            }
        }

        if(minVertex == null) {
            return Optional.empty();
        }

        Graph graphScc = new Graph(graph.getsize());
        for(int i=1 ; i< graph.getAdj().size() ; i++)
            for(int j=0 ; j<graph.getAdj().get(i).size() ; j++){
            if(minScc.contains(i) && minScc.contains(graph.getAdj().get(i).get(j))) {
                graphScc.addEdge(i,graph.getAdj().get(i).get(j).to,graph.getAdj().get(i).get(j).weight);
            }
        }
        return Optional.of(minVertex);
  }

    private void unblock(Integer u) {
        blockedSet.remove(u);
        if(blockedMap.get(u) != null) {
            blockedMap.get(u).forEach( v -> {
                if(blockedSet.contains(v)) {
                    unblock(v);
                }
            });
            blockedMap.remove(u);
        }
    }

    private boolean findCyclesInSCG(
            Integer startVertex,
            Integer currentVertex,int gain,Graph graph) {
        boolean foundCycle = false;
        stack.push(currentVertex);
        blockedSet.add(currentVertex);

        for (int i =0 ; i<graph.getAdj().get(currentVertex).size() ; i++) {
            Integer neighbor = graph.getAdj().get(currentVertex).get(i).to;
            //if neighbor is same as start vertex means cycle is found.
            //Store contents of stack in final result.
            if (neighbor == startVertex) {
                gain *= graph.getAdj().get(currentVertex).get(i).weight;
                List<Integer> cycle = new ArrayList<>();
                stack.push(startVertex);
                cycle.addAll(stack);
                Collections.reverse(cycle);
                stack.pop();
                allCycles.add(new pair(cycle,gain));
                foundCycle = true;
            } //explore this neighbor only if it is not in blockedSet.
            else if (!blockedSet.contains(neighbor)) {
                boolean gotCycle =
                        findCyclesInSCG(startVertex, neighbor,gain * graph.getAdj().get(currentVertex).get(i).weight,graph);
                foundCycle = foundCycle || gotCycle;
            }
        }
        //if cycle is found with current vertex then recursively unblock vertex and all vertices which are dependent on this vertex.
        if (foundCycle) {
            //remove from blockedSet  and then remove all the other vertices dependent on this vertex from blockedSet
            unblock(currentVertex);
        } else {
            //if no cycle is found with current vertex then don't unblock it. But find all its neighbors and add this
            //vertex to their blockedMap. If any of those neighbors ever get unblocked then unblock current vertex as well.
            for (int i =0 ; i<graph.getAdj().get(currentVertex).size() ; i++) {
                Integer w = graph.getAdj().get(currentVertex).get(i).to;
                Set<Integer> bSet = getBSet(w);
                bSet.add(currentVertex);
            }
        }
        //remove vertex from the stack.
        stack.pop();
        return foundCycle;
    }

    private Set<Integer> getBSet(Integer v) {
        return blockedMap.computeIfAbsent(v, (key) ->
                new HashSet<>() );
    }

    private Graph createSubGraph(int startVertex, Graph graph) {
        Graph subGraph = new Graph(graph.getsize());
        for(int i=startVertex ; i<graph.getAdj().size() ; i++)
            for(int j=0 ; j<graph.getAdj().get(i).size() ; j++){
                if(graph.getAdj().get(i).get(j).to >= startVertex)
                    subGraph.addEdge(i,graph.getAdj().get(i).get(j).to,graph.getAdj().get(i).get(j).weight);
            }
        return subGraph;
    }
    public List<pair> selfLoops(Graph graph){
        List<pair>ans = new ArrayList<>();
        for(int i=1 ; i<=graph.getsize() ; i++)
            for(int j=0 ; j<graph.getChildrens(i).size() ; j++){
                if(graph.getChildrens(i).get(j).to == i){
                    List<Integer>list = new ArrayList<>();
                    list.add(i);
                    list.add(i);
                    ans.add(new pair(list,graph.getChildrens(i).get(j).weight));
                }
            }
        return ans;
    }
/*
    public static void main(String args[]) {
        cyclesDetection johnson = new cyclesDetection();
        Graph graph = new Graph(9);
        graph.addEdge(1, 2,1);
        graph.addEdge(1, 8,1);
        graph.addEdge(1, 5,1);
        graph.addEdge(2, 9,1);
        graph.addEdge(2, 7,1);
        graph.addEdge(2, 3,1);
        graph.addEdge(3, 1,1);
        graph.addEdge(3, 2,1);
        graph.addEdge(3, 6,1);
        graph.addEdge(3, 4,1);
        graph.addEdge(6, 4,1);
        graph.addEdge(4, 5,1);
        graph.addEdge(5, 2,1);
        graph.addEdge(8, 9,1);
        graph.addEdge(9, 8,1);

        List<List<Integer>> allCycles = johnson.simpleCyles(graph);
        allCycles.forEach(cycle -> {
            StringJoiner joiner = new StringJoiner("->");
            cycle.forEach(vertex -> joiner.add(String.valueOf(vertex)));
            System.out.println(joiner);
        });
    }

 */
}

