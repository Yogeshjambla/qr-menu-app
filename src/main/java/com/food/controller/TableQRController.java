package com.food.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.entity.TableQR;
import com.food.repository.TableQRRepository;
import com.food.service.QRCodeService;

@RestController
@RequestMapping("/api/tables")
public class TableQRController {
	private final TableQRRepository tableQRRepository;
	private final QRCodeService qrCodeService;

	@Autowired
    public TableQRController(TableQRRepository tableQRRepository, QRCodeService qrCodeService) {
        this.tableQRRepository = tableQRRepository;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    public List<TableQR> getTables() {
        return tableQRRepository.findAll();
    }

    @PostMapping("/admin")
    public ResponseEntity<TableQR> createTable(@RequestBody Map<String, String> tableData) {
        TableQR table = new TableQR();
        table.setTableName(tableData.get("tableName"));
        
        TableQR savedTable = tableQRRepository.save(table);
        
        String qrCodeUrl = qrCodeService.generateMenuQRCode(savedTable.getId());
        savedTable.setQrCode(qrCodeUrl);
        
        TableQR updatedTable = tableQRRepository.save(savedTable);
        
        return ResponseEntity.ok(updatedTable);
    }

    @PostMapping("/admin/generateQR/{id}")
    public ResponseEntity<Map<String, String>> generateQr(@PathVariable Long id) {
        TableQR table = tableQRRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Table not found"));
        
        String qrCodeUrl = qrCodeService.generateMenuQRCode(id);
        table.setQrCode(qrCodeUrl);
        tableQRRepository.save(table);
        
        Map<String, String> response = new HashMap<>();
        response.put("qrCodeUrl", qrCodeUrl);
        response.put("message", "QR code generated successfully");
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableQRRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
