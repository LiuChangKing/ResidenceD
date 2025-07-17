package com.bekvon.bukkit.residence.persistance;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.bekvon.bukkit.residence.protection.CuboidArea;
import com.liuchangking.dreamengine.service.MysqlManager;
import org.bukkit.util.Vector;
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
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> resMap = new LinkedHashMap<>();
        String sql = "SELECT res_name, data FROM residences WHERE server_id=? AND world_name=?";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serverId);
            ps.setString(2, worldName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("res_name");
                    String data = rs.getString("data");
                    if (data != null) {
                        resMap.put(name, yaml.load(data));
                    }
                }
            }
        }
        root.put("Residences", resMap);
        return root;
    }

    /**
     * Legacy save format used when exporting worlds from YAML.
     * This method only stores the serialized data blob and does not
     * include extended fields like owner UUID or coordinates.
     * It should not be used after {@link #saveResidence} has been
     * called for a residence, otherwise those fields will be cleared.
     */
    @SuppressWarnings("unchecked")
    public void saveWorld(String worldName, Map<String, Object> root) throws Exception {
        if (root == null || !root.containsKey("Residences")) {
            return;
        }
        Map<String, Object> resMap = (Map<String, Object>) root.get("Residences");
        String sql = "REPLACE INTO residences(server_id, world_name, res_name, data) VALUES(?,?,?,?)";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Map.Entry<String, Object> entry : resMap.entrySet()) {
                ps.setString(1, serverId);
                ps.setString(2, worldName);
                ps.setString(3, entry.getKey());
                ps.setString(4, yaml.dump(entry.getValue()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void saveResidence(ClaimedResidence residence) throws Exception {
        int[] bounds = getBounds(residence);
        String sql = "REPLACE INTO residences(server_id, world_name, res_name, owner_uuid, min_x, min_y, min_z, max_x, max_y, max_z, data) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serverId);
            ps.setString(2, residence.getWorld());
            ps.setString(3, residence.getResidenceName());
            ps.setString(4, residence.getOwnerUUID() == null ? null : residence.getOwnerUUID().toString());
            ps.setInt(5, bounds[0]);
            ps.setInt(6, bounds[1]);
            ps.setInt(7, bounds[2]);
            ps.setInt(8, bounds[3]);
            ps.setInt(9, bounds[4]);
            ps.setInt(10, bounds[5]);
            ps.setString(11, yaml.dump(residence.save()));
            ps.executeUpdate();
        }
    }

    private int[] getBounds(ClaimedResidence res) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (CuboidArea area : res.getAreaArray()) {
            Vector low = area.getLowVector();
            Vector high = area.getHighVector();
            minX = Math.min(minX, low.getBlockX());
            minY = Math.min(minY, low.getBlockY());
            minZ = Math.min(minZ, low.getBlockZ());
            maxX = Math.max(maxX, high.getBlockX());
            maxY = Math.max(maxY, high.getBlockY());
            maxZ = Math.max(maxZ, high.getBlockZ());
        }
        return new int[] {minX, minY, minZ, maxX, maxY, maxZ};
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
