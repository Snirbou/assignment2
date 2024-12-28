package bgu.spl.mics.application.objects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LiDarDataBase is a singleton class responsible for managing LiDAR data.
 * It provides access to cloud point data and other relevant information for tracked objects.
 */
public class LiDarDataBase {

    // Thread-safe SingletonHolder for Lazy Initialization
    private static class SingletonHolder {
        private static final LiDarDataBase INSTANCE = new LiDarDataBase();
    }

    private final ConcurrentHashMap<Integer, ArrayList<StampedCloudPoints>> cloudPoints;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private LiDarDataBase() {
        cloudPoints = new ConcurrentHashMap<>();
    }

    /**
     * Returns the singleton instance of LiDarDataBase.
     *
     * @return The singleton instance of LiDarDataBase.
     */
    public static LiDarDataBase getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Returns the singleton instance of LiDarDataBase, with optional data loading.
     *
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */
    public static synchronized LiDarDataBase getInstance(String filePath) {
        LiDarDataBase instance = SingletonHolder.INSTANCE;
        instance.loadData(filePath);
        for(StampedCloudPoints cp : instance.getCloudPointsByTime(4))
            System.out.println(cp.getID());
        return instance;
    }

    /**
     * Loads LiDAR data from a JSON file.
     *
     * @param filePath The path to the LiDAR data file.
     */
//    public void loadData(String filePath) {
//        try (FileReader reader = new FileReader(filePath)) {
//            Gson gson = new Gson();
//            List<StampedCloudPoints> data = gson.fromJson(reader, new TypeToken<List<StampedCloudPoints>>() {}.getType());
//
//            for (StampedCloudPoints points : data) {
//                cloudPoints.computeIfAbsent(points.getTime(), k -> new ArrayList<>()).add(points);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to load LiDAR data from " + filePath);
//        }
//    }
//    public void loadData(String path) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
//            Gson gson = new Gson();
//            StringBuilder jsonBuilder = new StringBuilder();
//
//            // Read the JSON file line-by-line
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonBuilder.append(line.trim());
//            }
//
//            // Parse the full JSON string into a JsonArray
//            JsonArray jsonArray = gson.fromJson(jsonBuilder.toString(), JsonArray.class);
//            // Process each JSON object in the array
//            for (JsonElement element : jsonArray) {
//                JsonObject jsonObject = element.getAsJsonObject();
//                // Extract components
//                int time = jsonObject.get("time").getAsInt();
//                String id = jsonObject.get("id").getAsString();
//                JsonArray cloudPointsArray = jsonObject.get("cloudPoints").getAsJsonArray();
//                ArrayList<CloudPoint> cloudPointslist = new ArrayList<>();
//                for (JsonElement cPoint : cloudPointsArray) {
//                    JsonArray point = cPoint.getAsJsonArray();
//                    double x = point.get(0).getAsDouble();
//                    double y = point.get(1).getAsDouble();
//                    cloudPointslist.add(new CloudPoint(x, y));
//                }
//
//                StampedCloudPoints temp = new StampedCloudPoints(id, time, cloudPointslist);
//                cloudPoints.get(time).add(temp);
//                cloudPointslist.clear();
//                if (cloudPoints.size() == 13)
//                    cloudPoints.toString();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
public void loadData(String path) {
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
        Gson gson = new Gson();
        StringBuilder jsonBuilder = new StringBuilder();

        // Read the JSON file line-by-line
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line.trim());
        }

        // Parse the full JSON string into a JsonArray
        JsonArray jsonArray = gson.fromJson(jsonBuilder.toString(), JsonArray.class);

        // Process each JSON object in the array
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            int time = jsonObject.get("time").getAsInt();
            String id = jsonObject.get("id").getAsString();
            JsonArray cloudPointsArray = jsonObject.get("cloudPoints").getAsJsonArray();

            ArrayList<CloudPoint> cloudPointsList = new ArrayList<>();

            for (JsonElement cPoint : cloudPointsArray) {
                JsonArray point = cPoint.getAsJsonArray();
                double x = point.get(0).getAsDouble();
                double y = point.get(1).getAsDouble();
                cloudPointsList.add(new CloudPoint(x, y));
            }

            StampedCloudPoints temp = new StampedCloudPoints(id, time, cloudPointsList);
            cloudPoints.computeIfAbsent(time, k -> new ArrayList<>()).add(temp);
        }
    } catch (IOException e) {
        throw new RuntimeException("Failed to load LiDAR data from " + path, e);
    }
}

    /**
     * Retrieves cloud points by time tick.
     *
     * @param tick The time tick to query.
     * @return List of cloud points at the given tick.
     */
    public ArrayList<StampedCloudPoints> getCloudPointsByTime(int tick) {
        return cloudPoints.getOrDefault(tick, new ArrayList<>());
    }

}
