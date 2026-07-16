package edu.cit.Macopia.StockManagement_System.common.repository;

import edu.cit.Macopia.StockManagement_System.common.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findByProduct_SkuOrderByTimestampDesc(String sku);
}
