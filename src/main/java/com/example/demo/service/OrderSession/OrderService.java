package com.example.demo.service.OrderSession;

import com.example.demo.model.Order_Session;
import com.example.demo.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService extends IService<Order_Session> {
    public List<Order_Session> findAllBySellernameContains(String sellername);

    public void deleteOrder_SessionsByUsername(String username);

}
