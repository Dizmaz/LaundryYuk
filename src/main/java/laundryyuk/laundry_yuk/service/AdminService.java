package laundryyuk.laundry_yuk.service;

import java.util.List;

import laundryyuk.laundry_yuk.domain.*;
import laundryyuk.laundry_yuk.model.AdminDTO;
import laundryyuk.laundry_yuk.model.CustomerDTO;
import laundryyuk.laundry_yuk.repos.AdminRepository;
import laundryyuk.laundry_yuk.repos.ReportIncomeRepository;
import laundryyuk.laundry_yuk.repos.ReportOrderRepository;
import laundryyuk.laundry_yuk.repos.ReportReviewRepository;
import laundryyuk.laundry_yuk.util.NotFoundException;
import laundryyuk.laundry_yuk.util.PasswordUtil;
import laundryyuk.laundry_yuk.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final ReportIncomeRepository reportIncomeRepository;
    private final ReportOrderRepository reportOrderRepository;
    private final ReportReviewRepository reportReviewRepository;

    public AdminService(final AdminRepository adminRepository,
            final ReportIncomeRepository reportIncomeRepository,
            final ReportOrderRepository reportOrderRepository,
            final ReportReviewRepository reportReviewRepository) {
        this.adminRepository = adminRepository;
        this.reportIncomeRepository = reportIncomeRepository;
        this.reportOrderRepository = reportOrderRepository;
        this.reportReviewRepository = reportReviewRepository;
    }

    public List<AdminDTO> findAll() {
        final List<Admin> admins = adminRepository.findAll(Sort.by("id"));
        return admins.stream()
                .map(admin -> mapToDTO(admin, new AdminDTO()))
                .toList();
    }

    public AdminDTO get(final Long id) {
        return adminRepository.findById(id)
                .map(admin -> mapToDTO(admin, new AdminDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AdminDTO adminDTO) {
        final Admin admin = new Admin();
        mapToEntity(adminDTO, admin);
        return adminRepository.save(admin).getId();
    }

    public void update(final Long id, final AdminDTO adminDTO) {
        final Admin admin = adminRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(adminDTO, admin);
        adminRepository.save(admin);
    }

    public void delete(final Long id) {
        adminRepository.deleteById(id);
    }

    private AdminDTO mapToDTO(final Admin admin, final AdminDTO adminDTO) {
        adminDTO.setId(admin.getId());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setNama(admin.getNama());
        adminDTO.setRole(admin.getRole());
        return adminDTO;
    }

    private Admin mapToEntity(final AdminDTO adminDTO, final Admin admin) {
        admin.setEmail(adminDTO.getEmail());
        admin.setNama(adminDTO.getNama());
        admin.setRole(adminDTO.getRole());
        return admin;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Admin admin = adminRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ReportIncome adminReportIncome = reportIncomeRepository.findFirstByAdmin(admin);
        if (adminReportIncome != null) {
            referencedWarning.setKey("admin.reportIncome.admin.referenced");
            referencedWarning.addParam(adminReportIncome.getId());
            return referencedWarning;
        }
        final ReportOrder adminReportOrder = reportOrderRepository.findFirstByAdmin(admin);
        if (adminReportOrder != null) {
            referencedWarning.setKey("admin.reportOrder.admin.referenced");
            referencedWarning.addParam(adminReportOrder.getId());
            return referencedWarning;
        }
        final ReportReview adminReportReview = reportReviewRepository.findFirstByAdmin(admin);
        if (adminReportReview != null) {
            referencedWarning.setKey("admin.reportReview.admin.referenced");
            referencedWarning.addParam(adminReportReview.getId());
            return referencedWarning;
        }
        return null;
    }


    //========================================================================================
    public boolean emailExists(String email) {
        return adminRepository.findByEmail(email).isPresent();
    }

    public boolean authenticate(String email, String rawPassword) {
        String hashedPassword = PasswordUtil.hashPassword(rawPassword);
        return adminRepository.findByEmail(email)
                .map(admin -> {
                    System.out.println("Admin ditemukan: " + admin.getEmail());
//                    System.out.println("Password di database: " + admin.getPassword());
//                    System.out.println("Password yang di-hash: " + hashedPassword);
                    return admin.getPassword().equals(hashedPassword);
                })
                .orElseGet(() -> {
                    System.out.println("Admin dengan email " + email + " tidak ditemukan.");
                    return false;
                });
    }

}
