import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        //create the graph
        Platform.startup(() -> {


            Digraph<String, String> g = new DigraphEdgeList<>();
//... see example below

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);
        Scene scene = new Scene(graphView, 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFXGraph Visualization");
        /*
            g.insertVertex("A");
            g.insertVertex("B");
            g.insertVertex("C");
            g.insertVertex("D");
            g.insertVertex("E");
            g.insertVertex("F");
            g.insertVertex("G");

            g.insertEdge("A", "B", "1");
            g.insertEdge("A", "C", "2");
            g.insertEdge("A", "D", "3");
            g.insertEdge("A", "E", "4");
            g.insertEdge("A", "F", "5");
*/


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = 0;
            int start=0,end=0,t=0;
            try {
                n = Integer.parseInt(br.readLine());
                 t = Integer.parseInt(br.readLine());
                 start = Integer.parseInt(br.readLine());
                 end = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        for(int i=1 ;i<=n ; i++)
            g.insertVertex(String.valueOf(i));

        SFG sfg = new SFG(n,start,end);
        String space = "";
        while (t-- > 0){
            String[]str = new String[0];
            try {
                str = br.readLine().split(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }

            int from = Integer.parseInt(str[0]);
            int to = Integer.parseInt(str[1]);
            int weight = Integer.parseInt(str[2]);
            g.insertEdge(String.valueOf(from), String.valueOf(to), String.valueOf(weight) + space);
            space += " ";
            if(from == end && to == end && weight == 1)
                continue;
            if(from == start && to == start && weight == 1)
                continue;
            sfg.addEdge(from,to,weight);
        }
        System.out.println(sfg.calcuateGain());

            stage.setScene(scene);
            stage.show();

//IMPORTANT - Called after scene is displayed so we can have width and height values
            graphView.init();

            graphView.update();


        });
    }
}
