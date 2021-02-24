package com.example.demo.service.product;

import com.example.demo.model.Order_Session;
import com.example.demo.model.Product;

import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    ProductRepository productRepository;


    @Override
    public Page<Product> findAllByNameContaining(String name, Pageable pageable) {
        return productRepository.findAllByNameContaining(name, pageable);
    }

    @Override
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Order_Session> findByid(Long id) {
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {

        return productRepository.save(product);
    }

    @Override
    public Product remove(Long id) {
        Product product = productRepository.findById(id).get();
        if(product != null){
            productRepository.delete(product);
        }
        return product;
    }

    @Override
    public List<Order_Session> findAllBySellernameContains(String sellername) {
        return null;
    }

    @Override
    public List<Product> findAllBySellernameContain(String sellername) {
        TypedQuery<Product> query = em.createQuery("select c from Product c where c.sellername = :sellername", Product.class);
        query.setParameter("sellername",sellername);
        try {

            return query.getResultList();
        }catch (NoResultException e){
            return null;
        }

    }


}

