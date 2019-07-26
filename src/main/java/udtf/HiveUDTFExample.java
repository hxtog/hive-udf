package udtf;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;

import java.util.ArrayList;

public class HiveUDTFExample extends GenericUDTF {
    private ArrayList<Object[]> processInputRecord(String name){
        ArrayList<Object[]> result = new ArrayList<>();

        // ignoring null or empty input
        if ((name == null) || name.isEmpty()) {
            return result;
        }

        String[] tokens = name.split("\\s+");

        if (tokens.length == 2) {
            result.add(new Object[] {tokens[0], tokens[1]});
        } else if ((tokens.length == 4) && (("and").equals(tokens[1]))) {
            result.add(new Object[]{tokens[0], tokens[3]});
            result.add(new Object[]{tokens[2], tokens[3]});
        }

        return result;
    }

    @Override
    public void process(Object[] args) throws HiveException {
        if (args[0] != null) {
            ArrayList<Object[]> results = processInputRecord(args[0].toString());
            for (Object[] r : results) {
                forward(r);
            }
        }
    }

    @Override
    public void close() {
    }
}
