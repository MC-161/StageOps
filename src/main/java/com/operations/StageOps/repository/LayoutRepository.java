package com.operations.StageOps.repository;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.model.Section;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LayoutRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor to initialize the LayoutRepository with JdbcTemplate.
     *
     * @param jdbcTemplate The JdbcTemplate instance used for database operations.
     */
    public LayoutRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a new layout configuration along with its sections and associated seats.
     *
     * @param layout The layout configuration object to be saved.
     * @return The ID of the saved layout.
     */
    public int save(LayoutConfiguration layout) {
        // Insert layout data into the 'layouts' table
        String sql = "INSERT INTO layouts (layout_id, layout_name, max_capacity, room_id, layout_type) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, layout.getLayoutId(), layout.getLayoutName(), layout.getMaxCapacity(), layout.getRoomId(), layout.getLayoutType());

        // Insert sections into the 'sections' table
        for (Section section : layout.getSections()) {
            String sectionSql = "INSERT INTO sections (section_name, section_type, layout_id) VALUES (?, ?, ?)";
            jdbcTemplate.update(sectionSql, section.getSectionName(), section.getSectionType(), layout.getLayoutId());

            // Associating existing seats with sections by updating the 'seating' table
            for (Seating seat : section.getSeats()) {
                String updateSeatSql = "UPDATE seating SET section_name = ? WHERE seat_id = ?";
                jdbcTemplate.update(updateSeatSql, seat.getSectionName(), seat.getSeatId());
            }
        }

        // Insert into room_layouts table to associate the room with the layout
        String roomLayoutSql = "INSERT INTO room_layouts (room_id, layout_id) VALUES (?, ?)";
        jdbcTemplate.update(roomLayoutSql, layout.getRoomId(), layout.getLayoutId());

        return layout.getLayoutId(); // Return the layout ID
    }

    /**
     * Get all layouts from the database, including their sections and associated seats.
     *
     * @return A list of all layout configurations.
     */
    public List<LayoutConfiguration> getAllLayouts() {
        String sql = "SELECT * FROM layouts";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            int layoutId = rs.getInt("layout_id");
            LayoutConfiguration layout = new LayoutConfiguration(
                    layoutId,
                    rs.getString("layout_name"),
                    rs.getInt("max_capacity"),
                    rs.getInt("room_id"),
                    rs.getString("layout_type")
            );
            layout.setSections(findSectionsByLayoutId(layoutId)); // Fetch sections for this layout
            return layout;
        });
    }

    /**
     * Get a specific layout by its ID, including its sections and associated seats.
     *
     * @param layoutId The ID of the layout to retrieve.
     * @return The layout configuration with its sections and seats.
     */
    public LayoutConfiguration getLayoutById(int layoutId) {
        String sql = "SELECT * FROM layouts WHERE layout_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{layoutId}, (rs, rowNum) -> {
            LayoutConfiguration layout = new LayoutConfiguration(
                    rs.getInt("layout_id"),
                    rs.getString("layout_name"),
                    rs.getInt("max_capacity"),
                    rs.getInt("room_id"),
                    rs.getString("layout_type")
            );
            layout.setSections(findSectionsByLayoutId(layoutId)); // Fetch sections for this layout
            return layout;
        });
    }

    /**
     * Update an existing layout configuration and its associated sections and seats.
     *
     * @param layout The updated layout configuration.
     * @return The updated layout configuration, including sections and seats.
     */
    public LayoutConfiguration update(LayoutConfiguration layout) {
        String sql = "UPDATE layouts SET layout_name = ?, max_capacity = ?, room_id = ?, layout_type = ? WHERE layout_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, layout.getLayoutName(), layout.getMaxCapacity(), layout.getRoomId(), layout.getLayoutType(), layout.getLayoutId());

        if (rowsAffected > 0) {
            String selectSql = "SELECT * FROM layouts WHERE layout_id = ?";
            LayoutConfiguration updatedLayout = jdbcTemplate.queryForObject(selectSql, new Object[]{layout.getLayoutId()},
                    (rs, rowNum) -> new LayoutConfiguration(
                            rs.getInt("layout_id"),
                            rs.getString("layout_name"),
                            rs.getInt("max_capacity"),
                            rs.getInt("room_id"),
                            rs.getString("layout_type")
                    ));
            updatedLayout.setSections(findSectionsByLayoutId(layout.getLayoutId())); // Fetch sections for the updated layout
            return updatedLayout;
        }
        return null;
    }

    /**
     * Delete a layout from the database.
     *
     * @param layoutId The ID of the layout to delete.
     * @return The number of rows affected by the delete operation.
     */
    public int delete(int layoutId) {
        String sql = "DELETE FROM layouts WHERE layout_id = ?";
        return jdbcTemplate.update(sql, layoutId);
    }

    /**
     * Fetch layouts for a specific room, including sections and associated seats.
     *
     * @param roomId The ID of the room to fetch layouts for.
     * @return A list of layout configurations for the specified room.
     */
    public List<LayoutConfiguration> findLayoutsByRoomId(int roomId) {
        String sql = "SELECT * FROM layouts WHERE room_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roomId}, (rs, rowNum) -> {
            int layoutId = rs.getInt("layout_id");
            LayoutConfiguration layout = new LayoutConfiguration(
                    layoutId,
                    rs.getString("layout_name"),
                    rs.getInt("max_capacity"),
                    rs.getInt("room_id"),
                    rs.getString("layout_type")
            );
            layout.setSections(findSectionsByLayoutId(layoutId)); // Fetch sections for this layout
            return layout;
        });
    }

    /**
     * Fetch sections associated with a specific layout by its ID.
     *
     * @param layoutId The ID of the layout to fetch sections for.
     * @return A list of sections belonging to the layout.
     */
    private List<Section> findSectionsByLayoutId(int layoutId) {
        String sql = "SELECT * FROM sections WHERE layout_id = ?";
        return jdbcTemplate.query(sql, new Object[]{layoutId}, (rs, rowNum) -> {
            int sectionId = rs.getInt("section_id");
            String sectionName = rs.getString("section_name");
            Section section = new Section(
                    sectionId,
                    sectionName,
                    rs.getString("section_type"),
                    layoutId
            );

            // Fetch the seats for the section
            section.setSeats(findSeatsBySectionId(sectionName)); // Set the seats for this section

            return section;
        });
    }

    /**
     * Fetch seats associated with a specific section by its name.
     *
     * @param sectionName The name of the section to fetch seats for.
     * @return A list of seats belonging to the section.
     */
    public List<Seating> findSeatsBySectionId(String sectionName) {
        String sql = "SELECT * FROM seating WHERE section_name = ?";
        return jdbcTemplate.query(sql, new Object[]{sectionName}, (rs, rowNum) -> {
            Seating seat = new Seating(
                    rs.getString("seat_id"),
                    rs.getInt("room_id"),
                    rs.getInt("seat_number"),
                    rs.getBoolean("is_accessible"),
                    rs.getBoolean("is_restricted"),
                    sectionName
            );
            return seat;
        });
    }
}
