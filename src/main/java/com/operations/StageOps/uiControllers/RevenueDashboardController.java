package com.operations.StageOps.uiControllers;

import com.operations.StageOps.model.RevenueTrackingDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RevenueDashboardController {

    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;
    @FXML private TableView<RevenueTrackingDTO> revenueTable;
    @FXML private TableColumn<RevenueTrackingDTO, Integer> colRevenueId, colRoomId, colEventId, colBookingId;
    @FXML private TableColumn<RevenueTrackingDTO, Double> colTotalRevenue, colTicketSales, colVenueHire;
    @FXML private TableColumn<RevenueTrackingDTO, LocalDate> colBookingDate;
    @FXML private TableColumn<RevenueTrackingDTO, String> colStatus;
    @FXML private Button btnGenerateReport;
    @FXML private ComboBox<String> timePeriodComboBox;

    private final ObservableList<RevenueTrackingDTO> revenueData = FXCollections.observableArrayList();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();  // Use Jackson

    public void initialize() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  // Ignore unknown properties
        loadCharts("Monthly"); // Default chart to Monthly
        fetchRevenueData("Monthly");
        initializeTableColumns();

        // Setup time period ComboBox
        timePeriodComboBox.setValue("Monthly");
        timePeriodComboBox.setOnAction(event -> {
            String selectedPeriod = timePeriodComboBox.getValue();
            loadCharts(selectedPeriod);
            fetchRevenueData(selectedPeriod);
        });


        // Generate report button action
        btnGenerateReport.setOnAction(event -> generateCSVReport());
    }

    private void loadCharts(String period) {
        try {
            // Determine the appropriate API URL based on the selected time period
            String apiUrl = "http://localhost:8080/api/revenue";
            switch (period) {
                case "Daily":
                    apiUrl += "/daily/3/2025"; // Example: specific month (3) and year (2025)
                    break;
                case "Yearly":
                    apiUrl += "/lifetime"; // Example: specific year
                    break;
                case "Monthly":
                default:
                    apiUrl += "/monthly/2025"; // Example: specific year (2025)
                    break;
            }

            // Send HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            // Send the request and get the response body as a string
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Deserialize the response into a list of revenue data
            String responseBody = response.body();
            List<Map<String, Object>> revenueDataFromApi = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {});

            // Create a series for the bar chart (Revenue by selected period)
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(period + " Revenue");

            // For Monthly
            if (period.equals("Monthly")) {
                for (Map<String, Object> entry : revenueDataFromApi) {
                    int month = (int) entry.get("month");
                    double totalRevenue = (double) entry.get("total_revenue");
                    series.getData().add(new XYChart.Data<>(String.valueOf(month), totalRevenue));
                }
            }
            // For Daily
            else if (period.equals("Daily")) {
                for (Map<String, Object> entry : revenueDataFromApi) {
                    int day = (int) entry.get("day");
                    double totalRevenue = (double) entry.get("total_revenue");
                    series.getData().add(new XYChart.Data<>(String.valueOf(day), totalRevenue));
                }
            } else if (period.equals("Yearly")) {
                // For Yearly
                for (Map<String, Object> entry : revenueDataFromApi) {
                    int year = (int) entry.get("year");
                    double totalRevenue = (double) entry.get("total_revenue");
                    series.getData().add(new XYChart.Data<>(String.valueOf(year), totalRevenue));
                }
            }

            // Clear previous chart data and add the new series
            barChart.getData().clear();
            barChart.getData().add(series);

            // Update Pie Chart with separate data based on period
            updatePieChart(revenueDataFromApi, period);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePieChart(List<Map<String, Object>> revenueDataFromApi, String period) {
        // Prepare data for the pie chart (Revenue distribution per month or day)
        double totalRevenue = 0;
        for (Map<String, Object> entry : revenueDataFromApi) {
            totalRevenue += (double) entry.get("total_revenue");
        }

        // Create PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if (period.equals("Monthly")) {
            // Pie chart for monthly data
            for (Map<String, Object> entry : revenueDataFromApi) {
                int month = (int) entry.get("month");
                double monthRevenue = (double) entry.get("total_revenue");

                // Calculate percentage of total revenue
                double percentage = (monthRevenue / totalRevenue) * 100;
                pieChartData.add(new PieChart.Data("Month " + month, percentage));
            }
        } else if (period.equals("Daily")) {
            // Pie chart for daily data
            for (Map<String, Object> entry : revenueDataFromApi) {
                int day = (int) entry.get("day");
                double dayRevenue = (double) entry.get("total_revenue");

                // Calculate percentage of total revenue
                double percentage = (dayRevenue / totalRevenue) * 100;
                pieChartData.add(new PieChart.Data("Day " + day, percentage));
            }
        } else if (period.equals("Yearly")) {
            // Pie chart for yearly data
            for (Map<String, Object> entry : revenueDataFromApi) {
                int year = (int) entry.get("year");
                double yearRevenue = (double) entry.get("total_revenue");

                // Calculate percentage of total revenue
                double percentage = (yearRevenue / totalRevenue) * 100;
                pieChartData.add(new PieChart.Data("Year " + year, percentage));
            }

        }

        // Clear previous data and add new data
        pieChart.getData().clear();
        pieChart.getData().addAll(pieChartData);
    }

    private void fetchRevenueData(String period) {
        try {
            // Fetch data based on the selected period (Daily, Monthly, or Yearly)
            String apiUrl = "http://localhost:8080/api/revenue/entries/";
            switch (period) {
                case "Daily":
                    apiUrl += "monthly/2025/3";  // Adjust date format as needed
                    break;
                case "Yearly":
                    apiUrl += "lifetime";      // Adjust year if needed
                    break;
                case "Monthly":
                default:
                    apiUrl += "yearly/2025";  // Adjust month as needed
                    break;
            }

            // Send HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Handle the response body
            String responseBody = response.body();

            List<RevenueTrackingDTO> revenueDataFromApi;
            if (responseBody.startsWith("[")) {
                // Response is an array, directly deserialize into an array of RevenueTrackingDTO
                revenueDataFromApi = Arrays.asList(objectMapper.readValue(responseBody, RevenueTrackingDTO[].class));
            } else {
                // Handle if the response is wrapped in an object (e.g., { "data": [...] })
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
                if (responseMap.containsKey("")) {
                    List<RevenueTrackingDTO> revenues = Arrays.asList(
                            objectMapper.readValue(objectMapper.writeValueAsString(responseMap.get("data")), RevenueTrackingDTO[].class)
                    );
                    revenueDataFromApi = revenues;
                } else {
                    System.err.println("");
                    return;
                }
            }

            // Set data for the table
            revenueData.setAll(revenueDataFromApi);
            revenueTable.setItems(revenueData);


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching revenue data: " + e.getMessage());
        }
    }

    private void initializeTableColumns() {
        colRevenueId.setCellValueFactory(cellData -> cellData.getValue().revenueIdProperty().asObject());
        colRoomId.setCellValueFactory(cellData -> cellData.getValue().roomIdProperty().asObject());
        colEventId.setCellValueFactory(cellData -> cellData.getValue().eventIdProperty().asObject());
        colBookingId.setCellValueFactory(cellData -> cellData.getValue().bookingIdProperty().asObject());
        colTotalRevenue.setCellValueFactory(cellData -> cellData.getValue().totalRevenueProperty().asObject());
        colTicketSales.setCellValueFactory(cellData -> cellData.getValue().ticketSalesProperty().asObject());
        colVenueHire.setCellValueFactory(cellData -> cellData.getValue().venueHireProperty().asObject());
        colBookingDate.setCellValueFactory(cellData -> cellData.getValue().bookingDateProperty());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    }

    private void generateReport() {
        // Show a simple alert that the report was generated successfully
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report");
        alert.setHeaderText("Revenue Report Generated");
        alert.setContentText("The revenue report has been successfully generated.");
        alert.showAndWait();
    }

    private void generateCSVReport() {
        try {
            // Specify the file path and name
            File file = new File("revenue_report.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            // Write the CSV headers
            writer.write("Revenue ID,Room ID,Event ID,Booking ID,Total Revenue,Ticket Sales,Venue Hire,Booking Date,Status\n");

            // Write the data rows
            for (RevenueTrackingDTO revenue : revenueData) {
                writer.write(revenue.getRevenueId() + "," +
                        revenue.getRoomId() + "," +
                        revenue.getEventId() + "," +
                        revenue.getBookingId() + "," +
                        revenue.getTotalRevenue() + "," +
                        revenue.getTicketSales() + "," +
                        revenue.getVenueHire() + "," +
                        revenue.getBookingDate() + "," +
                        revenue.getStatus() + "\n");
            }

            writer.close();
            showConfirmation("CSV Report Generated", "The revenue report has been successfully generated as CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
