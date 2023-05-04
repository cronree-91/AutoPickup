package us.thezircon.play.autopickup.utils;


import com.google.gson.Gson;
import org.bukkit.Material;

import java.util.List;

public class BoxData {

    public BoxType type;
    public List<Material> mats;

    public String serialize() {
        return new Gson().toJson(this);
    }

    public static BoxData deserialize(String data) {
        return new Gson().fromJson(data, BoxData.class);
    }

}
