package udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.MapObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.json.JSONObject;

import java.util.Map;

@Description(
        name = "map2json",
        value = "Description: udf_map2json(mapValue), Translate MapObject to JSONObject.\n"
                + "Require: Map<String, String> map.\n"
                + "Return: String JSONObject.",
        extended = "Example:\n"
                + " > SELECT map2json(Map) as json;"
)
public class HiveUDFMap2Json extends GenericUDF {
    // 保存参数对象
    private MapObjectInspector mapOI = null;

    // 初始化检查传入参数是否符合要求
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        // 只要求一个参数, 否则抛出异常
        if (arguments.length != 1) {
            throw new UDFArgumentLengthException("map2json Just Need 1 Parameter!!!");
        }

        // 判断参数格式为Map对象, 不是则抛出异常
        if (!(arguments[0] instanceof MapObjectInspector)) {
            throw new UDFArgumentTypeException(0, "The Parameter Must Be Map!!!");
        }

        // 赋值参数对象
        this.mapOI = (MapObjectInspector) arguments[0];

        // 判断参数key格式为Map<String, String>对象, 不是则抛出异常
        if (!(this.mapOI.getMapKeyObjectInspector() instanceof StringObjectInspector)) {
            throw new UDFArgumentTypeException(0, "The Map Key Must Be String!!!");
        }
        // 判断参数value格式为Map<String, String>对象, 不是则抛出异常
        if (!(this.mapOI.getMapValueObjectInspector() instanceof StringObjectInspector)) {
            throw new UDFArgumentTypeException(0, "The Map Value Must Be String!!!");
        }

        // 声明方法返回格式为String
        return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        JSONObject result = new JSONObject();
        // 获取参数为Map对象
        Map<Object, Object> mapValue = (Map<Object, Object>) this.mapOI.getMap(arguments[0].get());
        // 限制内容
        if ((mapValue != null)
                && !mapValue.isEmpty()) {
            // Map对象解析
            for (Object key : mapValue.keySet()) {
                // 限制内容
                if ((key != null)
                        && !("").equals(key.toString())
                        && (mapValue.get(key) != null)
                        && !("").equals(mapValue.get(key))) {
                    // Map转Json
                    result.put(key.toString(), mapValue.get(key).toString());
                }
            }
        }
        // 返回结果为json格式的字符串
        return result.toString();
    }

    @Override
    public String getDisplayString(String[] children) {
        // 异常报错提示输出
        return String.format("map2json((map) %s)", children[0]);
    }
}
