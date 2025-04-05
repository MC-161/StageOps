package com.operations.StageOps.service;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.model.Section;
import com.operations.StageOps.repository.LayoutRepository;
import com.operations.StageOps.repository.SeatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Service class responsible for generating and saving room layouts, including seating arrangements.
 * Supports various room types (e.g., theatre, presentation, boardroom) and creates sections based on room type.
 */
@Service
public class LayoutCreationService {

    private final LayoutRepository layoutRepository;
    private final SeatingRepository seatingRepository;
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor for LayoutCreationService.
     *
     * @param jdbcTemplate    The JdbcTemplate used for database interaction.
     * @param layoutRepository The repository for managing layout configurations.
     * @param seatingRepository The repository for managing seating arrangements.
     */
    @Autowired
    public LayoutCreationService(JdbcTemplate jdbcTemplate, LayoutRepository layoutRepository, SeatingRepository seatingRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.layoutRepository = layoutRepository;
        this.seatingRepository = seatingRepository;
    }

    /**
     * Generate a layout configuration for a meeting room
     * @param roomId ID of the room for which the layout is being created
     */
    public void createLayoutForRoom(int roomId) {
        // Step 1: Fetch seats already created for the room
        List<Seating> seats = seatingRepository.findByRoomId(roomId);  // Assuming there's a method to fetch seats by room ID

        // Step 2: Generate sections from existing seats (based on some predefined logic or distribution)
        List<Section> sections = createSectionsForRoom(roomId, "presentation", seats);

        // Step 3: Create layout configuration
        LayoutConfiguration layout = new LayoutConfiguration(
                2, "presentation" + roomId, seats.size(), roomId, "presentation");
        layout.setSections(sections);

        // Step 4: Save the layout configuration
        layoutRepository.save(layout);
    }

    /**
     * Create sections for a room based on existing seats
     * @param roomId The room ID
     * @param roomType The type of room (e.g., theatre, dinner, meeting)
     * @param seats The list of seats already created in the room
     * @return The list of sections with assigned seats
     */
    private List<Section> createSectionsForRoom(int roomId, String roomType, List<Seating> seats) {
        List<Section> sections = new ArrayList<>();
        int sectionCount;
        String sectionType;

        switch (roomType.toLowerCase()) {
            case "theatre":
                sectionCount = 10;  // Divide into 10 sections (e.g., rows)
                sectionType = "Row";
                break;
            case "dinner":
                sectionCount = 10; // Assume 10 tables
                sectionType = "Table";
                break;
            case "meeting":
                sectionCount = 4;  // Assume 4 clusters
                sectionType = "Table";
                break;
            case "presentation":
                sectionCount = 2;  // Assume 2 sections for presentation
                sectionType = "Table";
                break;
            case "Boardroom":
                sectionCount = 1;  // Assume 1 section for boardroom
                sectionType = "Table";
                break;
            case "classroom":
                sectionCount = 5;  // Assume 5 sections for classroom
                sectionType = "Table";
                break;
            default:
                throw new IllegalArgumentException("Unknown room type: " + roomType);
        }

        int seatsPerSection = seats.size() / sectionCount;
        int seatIndex = 0;

        for (int i = 0; i < sectionCount; i++) {
            String sectionName = sectionType + " " + (char) ('A' + i);
            Section section = new Section(sectionName, sectionType);  // Create a fresh Section for each layout

            List<Seating> sectionSeats = new ArrayList<>();
            for (int j = 0; j < seatsPerSection && seatIndex < seats.size(); j++, seatIndex++) {
                Seating seat = seats.get(seatIndex);
                if (seat.getRoomId() == roomId) {
                    seat.setSectionName(sectionName);  // Assign this seat to the current section
                    seatingRepository.update(seat); // Save the updated seat with section information
                    sectionSeats.add(seat);
                }
            }

            section.setSeats(sectionSeats);  // Set the list of seats for this section
            sections.add(section);  // Add the new section to the layout
        }

        return sections;
    }


    private List<Section> createSectionsForMainHall(String eventType, int roomId, List<Seating> seats) {
        List<Section> sections = new ArrayList<>();

        // Define the seating sections
        Section stallsSection = new Section("Stalls", "Row");  // Create a new Section for Stalls
        Section balconySection = new Section("Balcony", "Row");  // Create a new Section for Balcony

        List<Seating> stallsSeats = new ArrayList<>();
        List<Seating> balconySeats = new ArrayList<>();

        int stallsCapacity = 285;  // Stalls section capacity
        int totalCapacity = 370;   // Full capacity (stalls + balcony)

        // Assign seats based on event type
        for (Seating seat : seats) {
            if (seat.getRoomId() == roomId) {
                // Ensure new sections are being created for the specific event type and not shared
                if (eventType.equalsIgnoreCase("film") && stallsSeats.size() < stallsCapacity) {
                    seat.setSectionName("Stalls");  // Assign to the Stalls section
                    stallsSeats.add(seat);  // Add to the Stalls seats list
                } else if (eventType.equalsIgnoreCase("live show")) {
                    // For live show, assign seats to either the Stalls or the Balcony
                    if (stallsSeats.size() < stallsCapacity) {
                        seat.setSectionName("Stalls");
                        stallsSeats.add(seat);
                    } else if (balconySeats.size() < totalCapacity - stallsCapacity) {
                        seat.setSectionName("Balcony");
                        balconySeats.add(seat);
                    }
                }
            }
        }

        // Set the seats for each section
        stallsSection.setSeats(stallsSeats);
        sections.add(stallsSection);  // Add the new Stalls section

        // If it's a "live show", add the Balcony section as well
        if (eventType.equalsIgnoreCase("live show")) {
            balconySection.setSeats(balconySeats);
            sections.add(balconySection);  // Add the new Balcony section
        }

        // Return the list of new sections created
        return sections;
    }



}
