package ch.gianlucafrei.nellygateway.services.login.drivers;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class UserModel {

    private String id;
    private HashMap<String, String> mappings = new HashMap<>();

    public UserModel(String id) {
        this.id = id;
    }

    public void set(String key, String value) {
        mappings.put(key, value);
    }

    public String get(String key) {
        return mappings.get(key);
    }
}