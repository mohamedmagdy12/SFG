import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private List<List<Edge>> adj = new ArrayList<>();
    private int size;
    public Graph(int v) {
        for (int i = 0; i <= v; i++)
            adj.add(new ArrayList<>());
        size = v;
    }

    public int getsize(){
        return size;
    }

    public List<List<Edge>> getAdj() {
        return adj;
    }

    public void addEdge(int from, int to, int weight) {
        adj.get(from).add(new Edge(to,weight));
    }
    public List<Edge> getChildrens(int node){
        return adj.get(node);
    }



    class Edge{
        int from,to,weight;

        public Edge(int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }
}

 