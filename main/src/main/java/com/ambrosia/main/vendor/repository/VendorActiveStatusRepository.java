package com.ambrosia.main.vendor.repository;

import com.ambrosia.main.vendor.entity.ActiveStatusValues;
import com.ambrosia.main.vendor.entity.VendorActiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Repository
public interface VendorActiveStatusRepository extends JpaRepository<VendorActiveStatus, Long> {
    Optional<VendorActiveStatus> findByActiveDate(Date date);

    @Transactional
    @Modifying
    @Query("UPDATE VendorActiveStatus v " +
            "SET v.appUserId = ?4, v.activeStatus = ?3, v.lastUpdated = ?2 WHERE v.activeDate = ?1")
    int updateStatus(Date date, LocalDateTime lastUpdated, ActiveStatusValues status, Long userId);
}
