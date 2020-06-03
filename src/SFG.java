import java.util.*;

public class SFG {
    private static Graph graph;
    private static cyclesDetection cyclesDecetion;
    private int start;
    private int target;
    public static int dom = 1;
    static class pair{
        List<Integer>list;
        int gain;
        public pair(List<Integer> list, int gain) {
            this.list = list;
            this.gain = gain;
        }
    }

    public SFG(int n,int start , int target){
        graph = new Graph(n);
        cyclesDecetion = new cyclesDetection();
        this.start = start;
        this.target = target;
    }

    public void addEdge(int from,int to,int weight){
        graph.addEdge(from, to, weight);
    }

    public double calcuateGain(){
        Deque<Integer> stack = new ArrayDeque<>();
        List<pair> forwardpaths = new ArrayList<>();
        boolean[]visited = new boolean[graph.getsize()+1];
        stack.add(start);
        visited[start] = true;
        forward_paths(start,graph,stack,forwardpaths,visited,1);
        List<cyclesDetection.pair> loops = cyclesDecetion.simpleCyles(graph);
        List<cyclesDetection.pair> selfloops = cyclesDecetion.selfLoops(graph);
        loops.addAll(selfloops);
        int numerator = 0;
        int domenator = 1;
        for(pair path : forwardpaths){
            int p = path.gain;
            int delta = 1;
            for(cyclesDetection.pair loop : loops){
                if(no_intersect(path.list,loop.list))
                    delta -= loop.gain;
            }
            numerator += p*delta;
        }
        backtrack_cycles(0,new ArrayDeque<>(),loops,1);
        return numerator / (double)dom;
    }

    private void forward_paths(int node , Graph graph, Deque<Integer> stack, List<pair> list,boolean[]visited,int gain){
        if(node == target){
            List<Integer> ans = new ArrayList<>();
            ans.addAll(stack);
            list.add(new pair(ans,gain));
            return;
        }
        for(int i=0 ; i<graph.getChildrens(node).size() ; i++){
            if(!visited[graph.getChildrens(node).get(i).to]){
                visited[graph.getChildrens(node).get(i).to] = true;
                stack.add(graph.getChildrens(node).get(i).to);
                forward_paths(graph.getChildrens(node).get(i).to,graph,stack,list,visited,gain * graph.getChildrens(node).get(i).weight);
                visited[graph.getChildrens(node).get(i).to] = false;
                stack.pollLast();
            }
        }

    }

    public void backtrack_cycles(int index , Deque<List<Integer>> stack , List<cyclesDetection.pair> cycles,int gain){
        if(index == cycles.size()) {
            if(stack.size() > 0){
                if((stack.size()&1) == 1){
                    dom -= gain;
                }else
                    dom+= gain;
            }
            return;
        }
        backtrack_cycles(index+1,stack,cycles,gain);
        if(stack.size() == 0 || no_intersect_with_stack(cycles.get(index).list,stack)) {
            stack.add(cycles.get(index).list);
            backtrack_cycles(index+1,stack,cycles,gain*cycles.get(index).gain);
            stack.pollLast();
        }
    }


    public boolean no_intersect(List<Integer> path , List<Integer> loop){
        for(int i=0 ; i<path.size() ;i++)
            for(int j=0 ; j<loop.size() ; j++){
                if(path.get(i) == loop.get(j))
                    return false;
            }
        return true;
    }

    public boolean no_intersect_with_stack(List<Integer> loop ,Deque<List<Integer>>stack){
       for(List<Integer> list : stack){
           if(!no_intersect(list,loop))
               return false;
       }
       return true;
    }

}
