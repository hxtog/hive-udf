package udf;

import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.Text;
import org.json.JSONObject;
import udf.junit.UDFJunitTestFrameWork;

import java.util.HashMap;
import java.util.Map;

public class HiveUDFMap2JsonTest extends UDFJunitTestFrameWork {
    public void testHiveUDFMap2Json01() throws Exception {
        // 定义参数为Map<String, String>格式
        ObjectInspector arg1 = ObjectInspectorFactory.getStandardMapObjectInspector(
                PrimitiveObjectInspectorFactory.javaStringObjectInspector,
                PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        // 创建udf的Junit测试对象
        HiveUDFMap2Json udf = new HiveUDFMap2Json();

        // 生成初始化方法传参校验对象
        ObjectInspector[] argsInit = {arg1};

        // 定义Map<String, String>参数并赋值
        Map<String, String> value1 = new HashMap<>();
        value1.put("key", "value");

        // 定义JsonObject对象并赋值
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value");

        // 定义结果值
        String result = jsonObject.toString();

        // 定义结果类型
        Object resultType = String.class;

        // 执行JunitTestCase判断
        runAndVerify(udf, argsInit, value1, result, resultType);
    }

    public void testHiveUDFMap2Json02() throws Exception {
        // 定义参数为Map<String, String>格式
        ObjectInspector arg1 = ObjectInspectorFactory.getStandardMapObjectInspector(
                PrimitiveObjectInspectorFactory.javaStringObjectInspector,
                PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        // 创建udf的Junit测试对象
        HiveUDFMap2Json udf = new HiveUDFMap2Json();

        // 生成初始化方法传参校验对象
        ObjectInspector[] argsInit = {arg1};

        // 定义Map<Text, Text>参数并赋值(Hive字符为文本对象)
        Map<Text, Text> value1 = new HashMap<>();
        value1.put(new Text("key"), new Text("value"));

        // 定义JsonObject对象并赋值
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value");

        // 定义结果值
        String result = jsonObject.toString();

        // 定义结果类型
        Object resultType = String.class;

        // 执行JunitTestCase判断
        runAndVerify(udf, argsInit, value1, result, resultType);
    }
}
