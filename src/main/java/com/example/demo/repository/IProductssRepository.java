package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IProductssRepository extends PagingAndSortingRepository<Product,Long> {
    Page<Product> findBySellername(String seller, Pageable page);

    List<Product> findBySellername(String seller);
}
