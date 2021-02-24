package com.example.demo.service.OrderSession;

import com.example.demo.model.Order_Session;
import com.example.demo.model.Product;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OrderServiceImpl implements OrderService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    OrderRepository orderRepository;


    @Override
    public Iterable<Order_Session> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Order_Session> findByid(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order_Session save(Order_Session order_session) {
        return orderRepository.save(order_session);
    }

    @Override
    public Order_Session remove(Long id) {
        Order_Session orderSession = orderRepository.findById(id).get();
        if(orderSession != null){
            orderRepository.delete(orderSession);
        }
        return orderSession;
    }

    @Override
    public List<Order_Session> findAllBySellernameContains(String sellername) {
        TypedQuery<Order_Session> query = em.createQuery("select c from Order_Session c where c.sellername = :sellername", Order_Session.class);
        query.setParameter("sellername",sellername);
        try {

            return query.getResultList();
        }catch (NoResultException e){
            return null;
        }

    }

    @Override
    public void deleteOrder_SessionsByUsername(String username) {
        TypedQuery<Order_Session> query = em.createQuery("select c from Order_Session c where c.username = :username", Order_Session.class);
        query.setParameter("username",username);
        for(Order_Session order : query.getResultList()){
           orderRepository.delete(order);
        }



    }

    @Override
    public List<Product> findAllBySellernameContain(String sellername) {
        return null;
    }
}
