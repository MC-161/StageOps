package com.operations.StageOps.repository;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.model.Section;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for handling CRUD operations related to layout configurations, sections, and seating in the database.
 * It provides methods to save, retrieve, update, and delete layouts and their associated sections and seats.
 */
@Repository
public class LayoutRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for the LayoutRepository.
     *
     * @param jdbcTemplate the JdbcTemplate object used to interact with the database.
     */
    public LayoutRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new layout configuration along with associated sections and seats in the database.
     *
     * @param layout the LayoutConfiguration object containing layout data, sections, and associated seats.
     * @return the layout ID of the saved layout.
     */
    public int save(LayoutConfiguration layout) {
        // Insert the layout data into the 'layouts' table.
        String sql = "INSERT INTO layouts (layout_id, layout_name, max_capacity, room_id, layout_type) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, layout.getLayoutId(), layout.getLayoutName(), layout.getMaxCapacity(), layout.getRoomId(), layout.getLayoutType());

        // Insert sections into the 'sections' table.
        for (Section section : layout.getSections()) {
            String sectionSql = "INSERT INTO sections (section_name, section_type, layout_id) VALUES (?, ?, ?)";
            jdbcTemplate.update(sectionSql, section.getSectionName(), section.getSectionType(), layout.getLayoutId());

            // Associate seats with sections by updating the 'seating' table with the correct section name.
            for (Seating seat : section.getSeats()) {
                String updateSeatSql = "UPDATE seating SET section_name = ? WHERE seat_id = ?";
                jdbcTemplate.update(updateSeatSql, seat.getSectionName(), seat.getSeatId());
            }
        }

        return layout.getLayoutId(); // Return the layout ID of the saved layout.
    }

    /**
     * Retrieves all layouts with their associated sections from the database.
     *
     * @return a list of all LayoutConfiguration objects, each containing associated sections.
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
            layout.setSections(findSectionsByLayoutId(layoutId)); // Fetch sections for this layout.
            return layout;
        });
    }

    /**
     * Retrieves a layout configuration by its ID, including associated sections.
     *
     * @param layoutId the ID of the layout to be retrieved.
     * @return the LayoutConfiguration object corresponding to the provided layout ID.
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
            layout.setSections(findSectionsByLayoutId(layoutId)); // Fetch sections for this layout.
            return layout;
        });
    }

    /**
     * Updates an existing layout configuration in the database along with its sections and seats.
     *
     * @param layout the LayoutConfiguration object containing updated layout data.
     * @return the updated LayoutConfiguration object with its sections and seats.
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
            updatedLayout.setSections(findSectionsByLayoutId(layout.getLayoutId())); // Fetch sections for this updated layout.
            return updatedLayout;
        }
        return null; // Return null if no rows were updated.
    }

    /**
     * Deletes a layout configuration from the database by its ID.
     *
     * @param layoutId the ID of the layout to be deleted.
     * @return the number of rows affected by the delete operation.
     */
    public int delete(int layoutId) {
        String sql = "DELETE FROM layouts WHERE layout_id = ?";
        return jdbcTemplate.update(sql, layoutId); // Return the number of rows affected by the delete operation.
    }

    /**
     * Retrieves all layouts for a specific room, including associated sections.
     *
     * @param roomId the ID of the room for which layouts are to be retrieved.
     * @return a list of LayoutConfiguration objects for the specified room.
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
            layout.setSections(findSectionsByLayoutId(layoutId)); // Fetch sections for this layout.
            return layout;
        });
    }

    /**
     * Retrieves all sections associated with a specific layout, including seats for each section.
     *
     * @param layoutId the ID of the layout for which sections are to be retrieved.
     * @return a list of Section objects, each containing associated seating information.
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

            // Fetch seats associated with this section and set them in the section object.
            section.setSeats(findSeatsBySectionId(sectionName));
            return section;
        });
    }

    /**
     * Retrieves all seats for a specific section.
     *
     * @param sectionName the name of the section for which seats are to be retrieved.
     * @return a list of Seating objects for the specified section.
     */
    public List<Seating> findSeatsBySectionId(String sectionName) {
        String sql = "SELECT * FROM seating WHERE section_name = ?";
        return jdbcTemplate.query(sql, new Object[]{sectionName}, (rs, rowNum) -> {
            Seating seat = new Seating(
                    rs.getString("seat_id"),
                    rs.getInt("room_id"),
                    rs.getInt("seat_number"),
                    rs.getBoolean("is_reserved"),
                    rs.getBoolean("is_accessible"),
                    rs.getBoolean("is_restricted"),
                    sectionName
            );
            return seat; // Return the seat object.
        });
    }
}
