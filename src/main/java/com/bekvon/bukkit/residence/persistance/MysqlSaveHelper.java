package com.bekvon.bukkit.residence.persistance;

import com.bekvon.bukkit.residence.protection.ClaimedResidence;
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
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> resMap = new LinkedHashMap<>();
        String sql = "SELECT res_name, owner_uuid, owner_name, leave_message, tp_loc, enter_message, " +
                "player_flags, area_flags, created_on, areas FROM residences WHERE server_id=? AND world_name=?";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serverId);
            ps.setString(2, worldName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("res_name");
                    Map<String, Object> data = new LinkedHashMap<>();
                    String leave = rs.getString("leave_message");
                    if (leave != null)
                        data.put("LeaveMessage", leave);
                    String enter = rs.getString("enter_message");
                    if (enter != null)
                        data.put("EnterMessage", enter);
                    String tploc = rs.getString("tp_loc");
                    if (tploc != null)
                        data.put("TPLoc", yaml.load(tploc));
                    Map<String, Object> perms = new LinkedHashMap<>();
                    String ownerUuid = rs.getString("owner_uuid");
                    if (ownerUuid != null)
                        perms.put("OwnerUUID", ownerUuid);
                    String ownerName = rs.getString("owner_name");
                    if (ownerName != null)
                        perms.put("OwnerLastKnownName", ownerName);
                    String pflags = rs.getString("player_flags");
                    if (pflags != null)
                        perms.put("PlayerFlags", yaml.load(pflags));
                    String aflags = rs.getString("area_flags");
                    if (aflags != null)
                        perms.put("AreaFlags", yaml.load(aflags));
                    data.put("Permissions", perms);
                    long created = rs.getLong("created_on");
                    if (!rs.wasNull())
                        data.put("CreatedOn", created);
                    String areas = rs.getString("areas");
                    if (areas != null)
                        data.put("Areas", yaml.load(areas));
                    resMap.put(name, data);
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
        String sql = "REPLACE INTO residences(server_id, world_name, res_name, owner_uuid, owner_name, " +
                "leave_message, tp_loc, enter_message, player_flags, area_flags, created_on, areas) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Map.Entry<String, Object> entry : resMap.entrySet()) {
                String resName = entry.getKey();
                // Skip entries without a valid residence name so we don't
                // create blank records in the database.
                if (resName == null || resName.isEmpty()) {
                    continue;
                }
                Map<String, Object> data = (Map<String, Object>) entry.getValue();
                Map<String, Object> perms = (Map<String, Object>) data.get("Permissions");

                ps.setString(1, serverId);
                ps.setString(2, worldName);
                ps.setString(3, resName);
                ps.setString(4, perms == null ? null : (String) perms.get("OwnerUUID"));
                ps.setString(5, perms == null ? null : (String) perms.get("OwnerLastKnownName"));
                ps.setString(6, (String) data.get("LeaveMessage"));
                ps.setString(7, data.get("TPLoc") == null ? null : yaml.dump(data.get("TPLoc")));
                ps.setString(8, (String) data.get("EnterMessage"));
                ps.setString(9, perms == null || perms.get("PlayerFlags") == null ? null : yaml.dump(perms.get("PlayerFlags")));
                ps.setString(10, perms == null || perms.get("AreaFlags") == null ? null : yaml.dump(perms.get("AreaFlags")));
                ps.setObject(11, data.get("CreatedOn"));
                ps.setString(12, data.get("Areas") == null ? null : yaml.dump(data.get("Areas")));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void saveResidence(ClaimedResidence residence) throws Exception {
        // Construction may trigger database writes before the name or world is set.
        // Skip those calls to avoid inserting rows with NULL values.
        if (residence == null || residence.getResidenceName() == null || residence.getResidenceName().isEmpty()
                || residence.getWorld() == null || residence.getWorld().isEmpty()) {
            return;
        }
        String sql = "REPLACE INTO residences(server_id, world_name, res_name, owner_uuid, owner_name, " +
                "leave_message, tp_loc, enter_message, player_flags, area_flags, created_on, areas) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serverId);
            ps.setString(2, residence.getWorld());
            ps.setString(3, residence.getResidenceName());
            Map<String, Object> data = residence.save();
            Map<String, Object> perms = (Map<String, Object>) data.get("Permissions");
            ps.setString(4, perms == null ? null : (String) perms.get("OwnerUUID"));
            ps.setString(5, perms == null ? null : (String) perms.get("OwnerLastKnownName"));
            ps.setString(6, (String) data.get("LeaveMessage"));
            ps.setString(7, data.get("TPLoc") == null ? null : yaml.dump(data.get("TPLoc")));
            ps.setString(8, (String) data.get("EnterMessage"));
            ps.setString(9, perms == null || perms.get("PlayerFlags") == null ? null : yaml.dump(perms.get("PlayerFlags")));
            ps.setString(10, perms == null || perms.get("AreaFlags") == null ? null : yaml.dump(perms.get("AreaFlags")));
            ps.setObject(11, data.get("CreatedOn"));
            ps.setString(12, data.get("Areas") == null ? null : yaml.dump(data.get("Areas")));
            ps.executeUpdate();
        }
    }

    public void deleteResidence(String resName) throws Exception {
        String sql = "DELETE FROM residences WHERE server_id=? AND res_name=?";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serverId);
            ps.setString(2, resName);
            ps.executeUpdate();
        }
    }

    public void renameResidence(String oldName, String newName) throws Exception {
        String sql = "UPDATE residences SET res_name=? WHERE server_id=? AND res_name=?";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setString(2, serverId);
            ps.setString(3, oldName);
            ps.executeUpdate();
        }
    }

    public java.util.List<ClaimedResidence> loadResidencesByOwner(java.util.UUID ownerUUID, com.bekvon.bukkit.residence.Residence plugin) throws Exception {
        java.util.List<ClaimedResidence> list = new java.util.ArrayList<>();
        String sql = "SELECT world_name, res_name, owner_uuid, owner_name, leave_message, tp_loc, enter_message, " +
                "player_flags, area_flags, created_on, areas FROM residences WHERE owner_uuid=?";
        try (Connection conn = MysqlManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ownerUUID.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String world = rs.getString("world_name");
                    String resName = rs.getString("res_name");
                    Map<String, Object> root = new LinkedHashMap<>();
                    String leave = rs.getString("leave_message");
                    if (leave != null)
                        root.put("LeaveMessage", leave);
                    String enter = rs.getString("enter_message");
                    if (enter != null)
                        root.put("EnterMessage", enter);
                    String tploc = rs.getString("tp_loc");
                    if (tploc != null)
                        root.put("TPLoc", yaml.load(tploc));
                    Map<String, Object> perms = new LinkedHashMap<>();
                    String ownerName = rs.getString("owner_name");
                    if (ownerName != null)
                        perms.put("OwnerLastKnownName", ownerName);
                    String ownerId = rs.getString("owner_uuid");
                    if (ownerId != null)
                        perms.put("OwnerUUID", ownerId);
                    String pflags = rs.getString("player_flags");
                    if (pflags != null)
                        perms.put("PlayerFlags", yaml.load(pflags));
                    String aflags = rs.getString("area_flags");
                    if (aflags != null)
                        perms.put("AreaFlags", yaml.load(aflags));
                    root.put("Permissions", perms);
                    long created = rs.getLong("created_on");
                    if (!rs.wasNull())
                        root.put("CreatedOn", created);
                    String areas = rs.getString("areas");
                    if (areas != null)
                        root.put("Areas", yaml.load(areas));
                    try {
                        ClaimedResidence res = ClaimedResidence.load(world, root, null, plugin);
                        if (res != null && res.getResidenceName() == null) {
                            res.setName(resName);
                        }
                        list.add(res);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
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
