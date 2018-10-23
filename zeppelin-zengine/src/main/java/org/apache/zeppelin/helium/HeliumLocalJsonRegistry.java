package org.apache.zeppelin.helium;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * author CliffLeopard
 * time   2018/10/23:10:57
 * email  gaoguanling@growingio.com
 *
 * 取消从网络加载 helium_registry.json  改为从本地配置
 */
public class HeliumLocalJsonRegistry extends HeliumLocalRegistry {

    public HeliumLocalJsonRegistry(String name, String uri) {
        super(name, uri);
    }
    @Override
    public List<HeliumPackage> getAll() throws IOException {
        List<HeliumPackage> result = new LinkedList<>();
        File file = new File(uri());
        if (file.exists()) {
            JsonReader reader = new JsonReader(new StringReader(FileUtils.readFileToString(file)));
            reader.setLenient(true);
            List<Map<String, Map<String, HeliumPackage>>> packages = gson.fromJson(
                    reader,
                    new TypeToken<List<Map<String, Map<String, HeliumPackage>>>>() {
                    }.getType());
            reader.close();

            for (Map<String, Map<String, HeliumPackage>> pkg : packages) {
                for (Map<String, HeliumPackage> versions : pkg.values()) {
                    result.addAll(versions.values());
                }
            }
        }
        return result;
    }

}
