package biopipes.utils.formats;

import com.tinkerpop.pipes.AbstractPipe;

/**
 * Created by IntelliJ IDEA.
 * User: qvh
 * Date: 8/8/11
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FastaFilter <S> extends AbstractPipe<S, S> {
    StringBuffer sb = null;
    protected S processNextStart() {
        while(sb == null){
              sb = new StringBuffer
        }
        return this.starts.next();
    }
}
