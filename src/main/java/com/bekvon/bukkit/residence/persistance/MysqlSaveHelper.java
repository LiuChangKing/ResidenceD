package com.bekvon.bukkit.residence.persistance;

import com.liuchangking.dreamengine.service.MysqlManager;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class MysqlSaveHelper {
    private final String serverId;
    private final Yaml yaml;

    public MysqlSaveHelper(String serverId) {
        this.serverId = serverId;
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        options.setIndent(2);
        options.setAllowUnicode(true);
        options.setWidth(4096);
        this.yaml = new Yaml(options);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> loadWorld(String worldName) throws Exception {
        String sql = "SELECT data, flags, messages FROM residence_worlds WHERE server_id=? AND world_name=?";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serverId);
            ps.setString(2, worldName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> root = new LinkedHashMap<>();
                    String data = rs.getString("data");
                    String flags = rs.getString("flags");
                    String messages = rs.getString("messages");
                    if (data != null) {
                        root.put("Residences", yaml.load(data));
                    }
                    if (flags != null) {
                        root.put("Flags", yaml.load(flags));
                    }
                    if (messages != null) {
                        root.put("Messages", yaml.load(messages));
                    }
                    return root;
                }
            }
        }
        return null;
    }

    public void saveWorld(String worldName, Map<String, Object> root) throws Exception {
        String sql = "REPLACE INTO residence_worlds(server_id, world_name, data, flags, messages) VALUES(?,?,?,?,?)";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serverId);
            ps.setString(2, worldName);
            ps.setString(3, root.containsKey("Residences") ? yaml.dump(root.get("Residences")) : null);
            ps.setString(4, root.containsKey("Flags") ? yaml.dump(root.get("Flags")) : null);
            ps.setString(5, root.containsKey("Messages") ? yaml.dump(root.get("Messages")) : null);
            ps.executeUpdate();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> loadPermLists() throws Exception {
        String sql = "SELECT data FROM residence_permlists WHERE id=1";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String data = rs.getString(1);
                if (data != null) {
                    Map<String, Object> root = new LinkedHashMap<>();
                    root.put("PermissionLists", yaml.load(data));
                    return root;
                }
            }
        }
        return null;
    }

    public void savePermLists(Map<String, Object> root) throws Exception {
        String sql = "REPLACE INTO residence_permlists(id, data) VALUES(1, ?)";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, root == null ? null : yaml.dump(root.get("PermissionLists")));
            ps.executeUpdate();
        }
    }
}
