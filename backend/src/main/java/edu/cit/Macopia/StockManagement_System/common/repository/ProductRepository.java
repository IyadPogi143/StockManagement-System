package edu.cit.Macopia.StockManagement_System.common.repository;

import edu.cit.Macopia.StockManagement_System.common.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsBySku(String sku);
}
