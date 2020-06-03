import java.util.*;

 public class TarjanStronglyConnectedComponent {

    private Map<Integer, Integer> visitedTime;
    private Map<Integer, Integer> lowTime;
    private Set<Integer> onStack;
    private Deque<Integer> stack;
    private Set<Integer> visited;
    private List<Set<Integer>> result;
    private int time;

    public List<Set<Integer>> scc(Graph graph) {


        time = 0;
        visitedTime = new HashMap<>();
        lowTime = new HashMap<>();
        onStack = new HashSet<>();
        stack = new LinkedList<>();
        visited = new HashSet<>();
        result = new ArrayList<>();


        for (int i=1 ; i<=graph.getsize() ; i++) {
            if (visited.contains(i)) {
                continue;
            }
            sccUtil(i,graph);
        }
        return result;
    }

    private void sccUtil(Integer vertex,Graph graph) {

        visited.add(vertex);
        visitedTime.put(vertex, time);
        lowTime.put(vertex, time);
        time++;
        stack.addFirst(vertex);
        onStack.add(vertex);

        for (int i=0 ; i<graph.getAdj().get(vertex).size() ; i++) {
            int child = graph.getAdj().get(vertex).get(i).to;
            if (!visited.contains(child)) {
                sccUtil(child,graph);

                lowTime.put(vertex,Math.min(lowTime.get(vertex),lowTime.get(child)));
            }

            else if (onStack.contains(child)) {
                lowTime.put(vertex,Math.min(lowTime.get(vertex),visitedTime.get(child)));
            }
        }

        if (visitedTime.get(vertex) == lowTime.get(vertex)) {
            Set<Integer> stronglyConnectedComponenet = new HashSet<>();
            Integer v;
            do {
                v = stack.pollFirst();
                onStack.remove(v);
                stronglyConnectedComponenet.add(v);
            } while (!vertex.equals(v));
            result.add(stronglyConnectedComponenet);
        }
    }
}