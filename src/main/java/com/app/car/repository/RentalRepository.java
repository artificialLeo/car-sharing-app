package com.app.car.repository;

import com.app.car.model.Rental;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query(
            value = "SELECT * FROM rental r WHERE r.user_id "
                    + "= :userId AND r.actual_return_date IS NOT NULL = :notNull",
            nativeQuery = true
    )
    List<Rental> findByUserIdAndActualReturnDateIsNull(
            @Param("userId") Long userId,
            @Param("notNull") boolean notNull
    );

    List<Rental> findByCar_IdAndActualReturnDateIsNull(Long id);

    List<Rental> findByReturnDateBeforeAndActualReturnDateIsNull(LocalDate today);

    List<Rental> findByUserId(Long userId);
}
