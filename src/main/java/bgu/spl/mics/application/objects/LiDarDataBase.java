package bgu.spl.mics.application.objects;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    private static LiDarDataBase INSTANCE = null;
    private ConcurrentHashMap<Integer, List<StampedCloudPoints>> cloudPoints;

    /**
     * Returns the singleton instance of LiDarDataBase.
     *
     * @param filePath The path to the LiDAR data file.
     * @return The singleton instance of LiDarDataBase.
     */
    public static synchronized LiDarDataBase getInstance(String filePath) {
        if (INSTANCE == null) {
            INSTANCE = new LiDarDataBase();
            INSTANCE.loadData(filePath);
        }
        return INSTANCE;
    }

    public static LiDarDataBase getInstance()
    {
        return INSTANCE;
    }

    /**
     * Private constructor to enforce singleton pattern.
     */
    private LiDarDataBase() {
        cloudPoints = new ConcurrentHashMap<>();
    }


    /**
     * Loads LiDAR data from a JSON file.
     *
     * @param filePath The path to the LiDAR data file.
     */
    private void loadData(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            List<StampedCloudPoints> data = gson.fromJson(reader, new TypeToken<List<StampedCloudPoints>>() {}.getType());

            for (StampedCloudPoints points : data) {
                cloudPoints.computeIfAbsent(points.getTime(), k -> new ArrayList<>()).add(points);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load LiDAR data from " + filePath);
        }
    }

    public List<StampedCloudPoints> getCloudPointsByTime(int tick) {
        return cloudPoints.getOrDefault(tick, new ArrayList<>());
    }


}
