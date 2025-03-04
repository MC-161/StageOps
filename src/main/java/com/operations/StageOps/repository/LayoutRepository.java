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

    public LayoutRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new layout (includes sections)
    public int save(LayoutConfiguration layout) {
        // Insert layout data into the 'layouts' table
        String sql = "INSERT INTO layouts (layout_id, layout_name, max_capacity, room_id, layout_type) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, layout.getLayoutId(), layout.getLayoutName(), layout.getMaxCapacity(), layout.getRoomId(), layout.getLayoutType());

        // Insert sections into the 'sections' table
        for (Section section : layout.getSections()) {
            String sectionSql = "INSERT INTO sections (section_name, section_type, layout_id) VALUES (?, ?, ?)";
            jdbcTemplate.update(sectionSql, section.getSectionName(), section.getSectionType(), layout.getLayoutId());

            // Here we are not inserting seats again; just associating existing seats with the sections
            for (Seating seat : section.getSeats()) {
                // Update the seat's section_name to the new section
                String updateSeatSql = "UPDATE seating SET section_name = ? WHERE seat_id = ?";
                jdbcTemplate.update(updateSeatSql, seat.getSectionName(), seat.getSeatId());
            }
        }

        return layout.getLayoutId(); // Return the layout ID
    }

    // Get all layouts with sections
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

    // Get layout by ID with sections
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

    // Update layout (with sections)
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
            updatedLayout.setSections(findSectionsByLayoutId(layout.getLayoutId())); // Fetch sections for this updated layout
            return updatedLayout;
        }
        return null;
    }

    // Delete a layout
    public int delete(int layoutId) {
        String sql = "DELETE FROM layouts WHERE layout_id = ?";
        return jdbcTemplate.update(sql, layoutId);
    }

    // Fetch layouts for a given room_id with sections
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

    // Fetch sections by layout_id
    // Fetch sections by layout_id and include seats for each section
    private List<Section> findSectionsByLayoutId(int layoutId) {
        String sql = "SELECT * FROM sections WHERE layout_id = ?";
        return jdbcTemplate.query(sql, new Object[]{layoutId}, (rs, rowNum) -> {
            int sectionId = rs.getInt("section_id");
            String section_name = rs.getString("section_name");
            Section section = new Section(
                    sectionId,
                    section_name,
                    rs.getString("section_type"),
                    layoutId
            );

            // Now fetch the seats for this section
            section.setSeats(findSeatsBySectionId(section_name)); // Set the seats for this section

            return section;
        });
    }

    // Fetch seats by section_id
    // Fetch seats by section_id
    public List<Seating> findSeatsBySectionId(String section_name) {
        String sql = "SELECT * FROM seating WHERE section_name = ?";
        return jdbcTemplate.query(sql, new Object[]{section_name}, (rs, rowNum) -> {
            Seating seat = new Seating(
                    rs.getString("seat_id"),
                    rs.getInt("room_id"),
                    rs.getInt("seat_number"),
                    rs.getBoolean("is_reserved"),
                    rs.getBoolean("is_accessible"),
                    rs.getBoolean("is_restricted"),
                    section_name
            );
            return seat;
        });
    }


}
