package com.operations.StageOps.repository;

import com.operations.StageOps.model.LayoutConfiguration;
import com.operations.StageOps.model.Seating;
import com.operations.StageOps.model.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LayoutRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private LayoutRepository layoutRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        layoutRepository = new LayoutRepository(jdbcTemplate);
    }

    @Test
    void save_ValidLayout_InsertsData() {
        // Arrange
        LayoutConfiguration layout = new LayoutConfiguration(1, "Standard", 100, 10, "Theater");
        Section section = new Section(1, "Main", "Regular", 1);
        section.setSeats(List.of(new Seating("A1", 10, 1, false, false, "Main")));
        layout.setSections(List.of(section));

        // Act
        int layoutId = layoutRepository.save(layout);

        // Assert
        assertEquals(1, layoutId);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(1), eq("Standard"), eq(100), eq(10), eq("Theater"));
        verify(jdbcTemplate, atLeastOnce()).update(contains("sections"), anyString(), anyString(), anyInt());
        verify(jdbcTemplate, atLeastOnce()).update(contains("seating"), anyString(), anyString());
    }

    @Test
    void getAllLayouts_ReturnsListOfLayouts() {
        // Arrange
        when(jdbcTemplate.query(eq("SELECT * FROM layouts"), any(RowMapper.class)))
                .thenReturn(List.of(new LayoutConfiguration(1, "Standard", 100, 10, "Theater")));

        // Act
        List<LayoutConfiguration> layouts = layoutRepository.getAllLayouts();

        // Assert
        assertEquals(1, layouts.size());
        assertEquals("Standard", layouts.get(0).getLayoutName());
    }

    @Test
    void getLayoutById_ValidId_ReturnsLayout() {
        // Arrange
        int layoutId = 1;
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM layouts WHERE layout_id = ?"), any(Object[].class), any(RowMapper.class)))
                .thenReturn(new LayoutConfiguration(layoutId, "Standard", 100, 10, "Theater"));

        // Act
        LayoutConfiguration result = layoutRepository.getLayoutById(layoutId);

        // Assert
        assertNotNull(result);
        assertEquals(layoutId, result.getLayoutId());
        assertEquals("Standard", result.getLayoutName());
    }

    @Test
    void update_ValidLayout_ReturnsUpdatedLayout() {
        // Arrange
        LayoutConfiguration layout = new LayoutConfiguration(1, "Updated", 150, 10, "Classroom");
        when(jdbcTemplate.update(anyString(), any(), any(), any(), any(), anyInt())).thenReturn(1);
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM layouts WHERE layout_id = ?"), any(Object[].class), any(RowMapper.class)))
                .thenReturn(layout);

        // Act
        LayoutConfiguration updated = layoutRepository.update(layout);

        // Assert
        assertNotNull(updated);
        assertEquals("Updated", updated.getLayoutName());
    }

    @Test
    void delete_ValidLayoutId_DeletesLayout() {
        // Arrange
        when(jdbcTemplate.update(eq("DELETE FROM layouts WHERE layout_id = ?"), eq(1))).thenReturn(1);

        // Act
        int result = layoutRepository.delete(1);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void findLayoutsByRoomId_ReturnsMatchingLayouts() {
        // Arrange
        int roomId = 10;
        when(jdbcTemplate.query(eq("SELECT * FROM layouts WHERE room_id = ?"), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of(new LayoutConfiguration(1, "Standard", 100, roomId, "Banquet")));

        // Act
        List<LayoutConfiguration> layouts = layoutRepository.findLayoutsByRoomId(roomId);

        // Assert
        assertFalse(layouts.isEmpty());
        assertEquals(roomId, layouts.get(0).getRoomId());
    }
}
