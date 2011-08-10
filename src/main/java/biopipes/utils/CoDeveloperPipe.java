package biopipes.utils;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;
import com.tinkerpop.pipes.filter.ObjectFilterPipe;
import com.tinkerpop.pipes.sideeffect.AggregatorPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectPipe;
import com.tinkerpop.pipes.transform.IdentityPipe;
import com.tinkerpop.pipes.transform.InPipe;
import com.tinkerpop.pipes.transform.OutPipe;
import com.tinkerpop.pipes.util.Pipeline;
import scala.collection.script.Start;
import scala.reflect.New;
import scala.reflect.This;

import java.security.Identity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 7/30/11
 * Time: 11:24 PM
 */
public class CoDeveloperPipe extends Pipeline<Vertex,Vertex> {
  public CoDeveloperPipe() {
//     List singleObjectList = new ArrayList(){
//            public boolean add(Object object) {
//                this.clear();
//                super.add(object);
//                return true;
//            }
//        };

     IdentityPipe<Vertex> idp = new IdentityPipe<Vertex>();
         HashSet foo = new HashSet<Vertex>();
     SideEffectPipe pipe0 = new AggregatorPipe(foo);
     Pipe pipe1 = new OutPipe("created");
     Pipe pipe2 = new InPipe("created");
     Pipe pipe3 = new ObjectFilterPipe(pipe0.getSideEffect(), ComparisonFilterPipe.Filter.NOT_EQUAL);

      System.out.println(pipe0.getSideEffect());
      System.out.println(foo);
      this.setPipes(idp, pipe0, pipe1, pipe2,  pipe3);
   }


}

//public class CoDeveloperPipe extends Pipeline<Vertex,Vertex> {
//public CoDeveloperPipe() {
//   Pipe pipe1 = new OutPipe("created");
//   Pipe pipe2 = new InPipe("created");
//   this.setPipes(pipe1, pipe2);
//}
//}