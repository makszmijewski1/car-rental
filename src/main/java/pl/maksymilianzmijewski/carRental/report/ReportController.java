package pl.maksymilianzmijewski.carRental.report;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.maksymilianzmijewski.carRental.report.dto.FleetStatusCount;
import pl.maksymilianzmijewski.carRental.report.dto.RevenueReport;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/fleet-status")
    public List<FleetStatusCount> fleetStatus() {
        return reportService.getFleetStatus();
    }

    @GetMapping("/revenue")
    public RevenueReport revenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.getRevenue(from, to);
    }
}
