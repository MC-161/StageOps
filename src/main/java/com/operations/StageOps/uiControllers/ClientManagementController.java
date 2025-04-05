package com.operations.StageOps.uiControllers;

import com.operations.StageOps.model.*;
import com.operations.StageOps.util.PdfGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientManagementController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField telephoneField;
    @FXML
    private Button addClientButton;
    @FXML
    private TableView<ClientDTO> clientsTable;
    @FXML
    private TableColumn<ClientDTO, Integer> clientIdColumn;
    @FXML
    private TableColumn<ClientDTO, String> nameColumn;
    @FXML
    private TableColumn<ClientDTO, String> addressColumn;
    @FXML
    private TableColumn<ClientDTO, String> emailColumn;
    @FXML
    private TableColumn<ClientDTO, String> telephoneColumn;
    @FXML
    private TextField contractClientIdField;
    @FXML
    private TextField contractStatusField;
    @FXML
    private TableView<ContractDTO> contractsTable;
    @FXML
    private TableColumn<ContractDTO, Integer> contractIdColumn;
    @FXML
    private TableColumn<ContractDTO, Integer> contractClientIdColumn;
    @FXML
    private TableColumn<ContractDTO, String> contractStatusColumn;
    @FXML
    private TableColumn<ContractDTO, Integer> contractEventIdColumn;
    @FXML
    private TableColumn<ContractDTO, String> contractVenueAddressColumn;

    @FXML
    private TextField invoiceClientIdField;
    @FXML
    private TextField invoiceStatusField;
    @FXML
    private TableView<InvoiceDTO> invoicesTable;
    @FXML
    private TableColumn<InvoiceDTO, Integer> invoiceIdColumn;
    @FXML
    private TableColumn<InvoiceDTO, Integer> invoiceClientIdColumn;
    @FXML
    private TableColumn<InvoiceDTO, String> invoiceStatusColumn;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private static final String BASE_URL = "http://localhost:8080/api/clients";
    private static final String CONTRACT_URL = "http://localhost:8080/api/contracts";
    private static final String INVOICE_URL = "http://localhost:8080/api/invoices";

    /**
     * Initializes the controller and populates the client table.
     */
    @FXML
    public void initialize() {
        // Set up table columns using the ClientDTO properties
        clientIdColumn.setCellValueFactory(cellData -> cellData.getValue().clientIdProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        telephoneColumn.setCellValueFactory(cellData -> cellData.getValue().telephoneNumberProperty());

        // Contract columns
        contractIdColumn.setCellValueFactory(cellData -> cellData.getValue().contractIdProperty().asObject());
        contractClientIdColumn.setCellValueFactory(cellData -> cellData.getValue().clientIdProperty().asObject());
        contractStatusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        contractEventIdColumn.setCellValueFactory(cellData -> cellData.getValue().eventIdProperty().asObject());
        contractVenueAddressColumn.setCellValueFactory(cellData -> cellData.getValue().venueAddressProperty());

        // Invoice columns
        invoiceIdColumn.setCellValueFactory(cellData -> cellData.getValue().invoiceIdProperty().asObject());
        invoiceClientIdColumn.setCellValueFactory(cellData -> cellData.getValue().clientIdProperty().asObject());
        invoiceStatusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        loadClients();

        // Load contracts/invoices when a client is selected
        clientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                loadContractsByClient(newSel.getClientId());
                loadInvoicesByClient(newSel.getClientId());
            }
        });

        // Set up context menu for invoices table
        setupInvoiceContextMenu();

        // Set up context menu for contracts table
        setupContractContextMenu();
    }

    /**
     * Sets up context menu for invoices to generate individual PDFs
     */
    private void setupInvoiceContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem generatePdfItem = new MenuItem("Generate PDF");
        generatePdfItem.setOnAction(event -> {
            InvoiceDTO selectedInvoice = invoicesTable.getSelectionModel().getSelectedItem();
            if (selectedInvoice != null) {
                generateSingleInvoicePdf(selectedInvoice);
            }
        });
        contextMenu.getItems().add(generatePdfItem);

        // Enable context menu on right-click
        invoicesTable.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY && invoicesTable.getSelectionModel().getSelectedItem() != null) {
                contextMenu.show(invoicesTable, event.getScreenX(), event.getScreenY());
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Handle double-click
                InvoiceDTO selectedInvoice = invoicesTable.getSelectionModel().getSelectedItem();
                if (selectedInvoice != null) {
                    generateSingleInvoicePdf(selectedInvoice);
                }
                event.consume();
            } else {
                contextMenu.hide();
            }
        });

        // Add keyboard shortcut (Enter key)
        invoicesTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                InvoiceDTO selectedInvoice = invoicesTable.getSelectionModel().getSelectedItem();
                if (selectedInvoice != null) {
                    generateSingleInvoicePdf(selectedInvoice);
                }
                event.consume();
            }
        });
    }

    /**
     * Sets up context menu for contracts to generate individual PDFs
     */
    private void setupContractContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem generatePdfItem = new MenuItem("Generate PDF");
        generatePdfItem.setOnAction(event -> {
            ContractDTO selectedContract = contractsTable.getSelectionModel().getSelectedItem();
            if (selectedContract != null) {
                generateSingleContractPdf(selectedContract);
            }
        });
        contextMenu.getItems().add(generatePdfItem);

        // Enable context menu on right-click
        contractsTable.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY && contractsTable.getSelectionModel().getSelectedItem() != null) {
                contextMenu.show(contractsTable, event.getScreenX(), event.getScreenY());
            } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Handle double-click
                ContractDTO selectedContract = contractsTable.getSelectionModel().getSelectedItem();
                if (selectedContract != null) {
                    generateSingleContractPdf(selectedContract);
                }
                event.consume();
            } else {
                contextMenu.hide();
            }
        });

        // Add keyboard shortcut (Enter key)
        contractsTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                ContractDTO selectedContract = contractsTable.getSelectionModel().getSelectedItem();
                if (selectedContract != null) {
                    generateSingleContractPdf(selectedContract);
                }
                event.consume();
            }
        });
    }

    /**
     * Handles adding a new client by sending the data to the API.
     */
    @FXML
    private void handleAddClient(ActionEvent event) {
        String name = nameField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();

        if (name.isEmpty() || address.isEmpty() || email.isEmpty() || telephone.isEmpty()) {
            showErrorMessage("All fields must be filled.");
            return;
        }

        Client newClient = new Client(0, name, address, email, telephone);  // ID is auto-generated by the API
        sendCreateRequest(newClient);
    }

    /**
     * Send a request to create a new client via API.
     */
    private void sendCreateRequest(Client client) {
        try {
            String requestBody = objectMapper.writeValueAsString(client);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Reload the client table after successful creation
                loadClients();
            } else {
                showErrorMessage("Error creating Client!");
            }
        } catch (IOException | InterruptedException e) {
            showErrorMessage("Error communicating with server!");
        }
    }

    private void loadContractsByClient(int clientId) {
        try {
            // Construct the URL to fetch the contracts by client ID
            String url = CONTRACT_URL + "/client/" + clientId;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200) {
                // Deserialize the response into a generic list (ignoring extra fields)
                List<Map<String, Object>> contractsMap = objectMapper.readValue(response.body(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));

                // Convert the list of maps into ContractDTO objects
                List<ContractDTO> dtos = contractsMap.stream()
                        .map(map -> {
                            // Filter out any unwanted fields (like "endTime")
                            map.remove("endTime");

                            // Handle numeric fields correctly
                            BigDecimal totalAmount = getBigDecimalValue(map.get("totalAmount"));
                            BigDecimal initialAmount = getBigDecimalValue(map.get("initialAmount"));
                            BigDecimal finalAmount = getBigDecimalValue(map.get("finalAmount"));
                            BigDecimal depositAmount = getBigDecimalValue(map.get("depositAmount"));

                            // Now create ContractDTO from map
                            return new ContractDTO(
                                    (Integer) map.get("contractId"),
                                    (Integer) map.get("clientId"),
                                    (Integer) map.get("eventId"),
                                    (String) map.get("terms"),
                                    (String) map.get("status"),
                                    ZonedDateTime.parse((String) map.get("contractDate")),
                                    (String) map.get("venueAddress"),
                                    (String) map.get("eventDescription"),
                                    LocalDateTime.parse((String) map.get("startDateTime")),
                                    LocalDateTime.parse((String) map.get("endDateTime")),
                                    totalAmount,
                                    initialAmount,
                                    finalAmount,
                                    LocalDate.parse((String) map.get("finalPaymentDate")),
                                    (Integer) map.get("gracePeriod"),
                                    (String) map.get("lateFee"),
                                    depositAmount,
                                    (Integer) map.get("depositReturnDays"),
                                    (Integer) map.get("cancellationNoticeDays"),
                                    (String) map.get("cancellationFee"),
                                    (String) map.get("ownerName"),
                                    (String) map.get("renterName")
                            );
                        })
                        .collect(Collectors.toList());

                // Set the items for the contracts table view
                contractsTable.setItems(FXCollections.observableArrayList(dtos));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            showErrorMessage("Error loading contracts for client.");
        }
    }

    // Helper method to safely convert a value to BigDecimal
    private BigDecimal getBigDecimalValue(Object value) {
        if (value instanceof String) {
            return new BigDecimal((String) value);
        } else if (value instanceof Number) {
            return new BigDecimal(((Number) value).doubleValue());
        } else {
            return BigDecimal.ZERO; // Or handle it based on your requirements
        }
    }



    private void loadInvoicesByClient(int clientId) {
        try {
            String url = INVOICE_URL + "/client/" + clientId;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Invoice> invoices = objectMapper.readValue(response.body(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Invoice.class));

                List<InvoiceDTO> dtos = invoices.stream()
                        .map(i -> new InvoiceDTO(
                                i.getInvoiceId(),
                                i.getBookingId(),  // Add bookingId here
                                i.getClientId(),
                                i.getTotalAmount(),  // Add totalAmount here
                                i.getStatus(),
                                i.getIssueDate(),  // Add issueDate here
                                i.getDueDate()  // Add dueDate here
                        ))
                        .collect(Collectors.toList());

                invoicesTable.setItems(FXCollections.observableArrayList(dtos));
            }
        } catch (Exception e) {
            showErrorMessage("Error loading invoices for client." + e.getMessage());
        }
    }

    /**
     * Load all clients from the API and convert them to ClientDTO.
     */
    private void loadClients() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Client> clients = objectMapper.readValue(response.body(),
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Client.class));

                // Convert Client models to ClientDTO
                List<ClientDTO> clientDTOs = clients.stream()
                        .map(client -> new ClientDTO(client.getClientId(), client.getName(),
                                client.getAddress(), client.getEmail(), client.getTelephoneNumber()))
                        .collect(Collectors.toList());

                clientsTable.setItems(FXCollections.observableArrayList(clientDTOs));
            }
        } catch (IOException | InterruptedException e) {
            showErrorMessage("Error loading clients!" + e.getMessage());
        }
    }

    /**
     * Handler for generate selected invoice button
     */
    @FXML
    private void handleGenerateSelectedInvoice(ActionEvent event) {
        InvoiceDTO selectedInvoice = invoicesTable.getSelectionModel().getSelectedItem();
        if (selectedInvoice == null) {
            showErrorMessage("Please select an invoice first.");
            return;
        }

        generateSingleInvoicePdf(selectedInvoice);
    }

    /**
     * Handler for generate selected contract button
     */
    @FXML
    private void handleGenerateSelectedContract(ActionEvent event) {
        ContractDTO selectedContract = contractsTable.getSelectionModel().getSelectedItem();
        if (selectedContract == null) {
            showErrorMessage("Please select a contract first.");
            return;
        }

        generateSingleContractPdf(selectedContract);
    }

    /**
     * Generates PDF for a single invoice
     */
    private void generateSingleInvoicePdf(InvoiceDTO invoiceDTO) {
        ClientDTO selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showErrorMessage("Client information not available.");
            return;
        }

        try {
            // Fetch complete invoice data from API if needed
            String url = INVOICE_URL + "/" + invoiceDTO.getInvoiceId();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Invoice invoice = objectMapper.readValue(response.body(), Invoice.class);

                // Let user choose where to save the PDF
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Invoice PDF");
                fileChooser.setInitialFileName("invoice_" + invoice.getInvoiceId() + ".pdf");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );
                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    // Generate PDF with the complete invoice data
                    PdfGenerator.generateSingleInvoicePdf(invoice, selectedClient.getName(),selectedClient.getAddress() , file.getAbsolutePath());

                    // Ask user if they want to open the PDF
                    if (showConfirmationDialog("PDF Generated",
                            "PDF has been generated successfully.")) {
                    }
                }
            } else {
                showErrorMessage("Failed to fetch invoice details.");
            }
        } catch (Exception e) {
            showErrorMessage("Failed to generate PDF: " + e.getMessage());
        }
    }

    /**
     * Generates PDF for a single contract
     */
    private void generateSingleContractPdf(ContractDTO contractDTO) {
        ClientDTO selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showErrorMessage("Client information not available.");
            return;
        }

        try {
            // Fetch complete contract data from API if needed
            String url = CONTRACT_URL + "/" + contractDTO.getContractId();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Contract contract = objectMapper.readValue(response.body(), Contract.class);

                // Let user choose where to save the PDF
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Contract PDF");
                fileChooser.setInitialFileName("contract_" + contract.getContractId() + ".pdf");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );
                File file = fileChooser.showSaveDialog(null);

                if (file != null) {
                    // Generate PDF with the complete contract data
                    PdfGenerator.generateSingleContractPdf(contract,"StageOps Lancaster", selectedClient.getName(), file.getAbsolutePath());

                    // Ask user if they want to open the PDF
                    if (showConfirmationDialog("PDF Generated",
                            "PDF has been generated successfully.")) {
                    }
                }
            } else {
                showErrorMessage("Failed to fetch contract details.");
            }
        } catch (Exception e) {
            showErrorMessage("Failed to generate PDF: " + e.getMessage());
        }
    }



    /**
     * Shows an error dialog with the given message.
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog and returns the user's choice.
     */
    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}