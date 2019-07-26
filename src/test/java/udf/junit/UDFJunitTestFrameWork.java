package udf.junit;

import junit.framework.TestCase;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.logging.slf4j.Log4jLoggerFactory;
import org.slf4j.Logger;

public class UDFJunitTestFrameWork extends TestCase {
    private static Logger log = (new Log4jLoggerFactory()).getLogger("UDF");

    private static void notNull(String message, Object object, String udfName) {
        if (object == null) {
            log.error(udfName + ": " + message);
        }
        assertNotNull(message, object);
    }

    private static void notEqual(Object expected, Object actual, String udfName) {
        if (!expected.equals(actual)) {
            log.error(udfName + ": " + "EXPECTED= " + expected.toString() + " & ACTUAL= " + actual.toString() + " (Result not Equal the Expected Value!!!).");
        }
        assertEquals("Result not Equal the Expected Value!!!", expected, actual);
    }

    private static void typeEqual(Object expected, Object actual, String udfName) {
        if (!(actual.getClass()).equals(expected)) {
            log.error(udfName + ": " + "EXPECTED TYPE= " + expected.toString() + " & ACTUAL TYPE= " + actual.toString() + " (Result Type not Equal the Expected Type!!!).");
        }
        assertEquals("Result Type not Equal the Expected Type!!!", actual.getClass(), expected);
    }

    protected static void runAndVerify(GenericUDF udf, ObjectInspector[] argsInit,
                                       Object arg1,
                                       Object expected,
                                       Object expectedType) throws HiveException {
        udf.initialize(argsInit);
        log.debug(udf.getUdfName());
        GenericUDF.DeferredObject[] args = {new GenericUDF.DeferredJavaObject(arg1)};
        Object result = udf.evaluate(args);
        log.debug(" > " + udf.getDisplayString(new String[]{arg1.toString()})
                + "\n          > result\n          > " + expected);

        notNull("The Result is NULL!!!", result, udf.getUdfName());
        notNull("The Expected Value is NULL!!!", expected, udf.getUdfName());
        notEqual(expected, result, udf.getUdfName());

        typeEqual(expectedType, result, udf.getUdfName());
    }
}
