package theRetrospect.relics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RelicInfoRepository {
    Map<String, RelicInfo> relicInfoMap;

    public RelicInfoRepository(InputStream jsonStream) {
        assert jsonStream != null;

        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<Map<String, RelicInfo>>() {
        }.getType();

        relicInfoMap = gson.fromJson(new InputStreamReader(jsonStream, StandardCharsets.UTF_8), type);
    }

    public RelicInfo getRelicInfo(String relicId) {
        return relicInfoMap.get(relicId);
    }
}
