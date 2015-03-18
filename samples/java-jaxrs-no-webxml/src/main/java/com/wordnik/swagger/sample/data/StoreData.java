/**
 *  Copyright 2015 Reverb Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.sample.data;

import com.wordnik.swagger.sample.model.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class StoreData {
  static List<Order> orders = new ArrayList<Order>();

  static {
    orders.add(createOrder(1, 1, 2, new Date(), "placed"));
    orders.add(createOrder(2, 1, 2, new Date(), "delivered"));
    orders.add(createOrder(3, 2, 2, new Date(), "placed"));
    orders.add(createOrder(4, 2, 2, new Date(), "delivered"));
    orders.add(createOrder(5, 3, 2, new Date(), "placed"));
    orders.add(createOrder(11, 3, 2, new Date(), "placed"));
    orders.add(createOrder(12, 3, 2, new Date(), "placed"));
    orders.add(createOrder(13, 3, 2, new Date(), "placed"));
    orders.add(createOrder(14, 3, 2, new Date(), "placed"));
    orders.add(createOrder(15, 3, 2, new Date(), "placed"));
  }

  public Order findOrderById(long orderId) {
    for (Order order : orders) {
      if (order.getId() == orderId) {
        return order;
      }
    }
    return null;
  }

  public Order placeOrder(Order order) {
    if (orders.size() > 0) {
      for (int i = orders.size() - 1; i >= 0; i--) {
        if (orders.get(i).getId() == order.getId()) {
          orders.remove(i);
        }
      }
    }
    orders.add(order);
    return order;
  }

  public void deleteOrder(long orderId) {
    if (orders.size() > 0) {
      for (int i = orders.size() - 1; i >= 0; i--) {
        if (orders.get(i).getId() == orderId) {
          orders.remove(i);
        }
      }
    }
  }

  private static Order createOrder(long id, long petId, int quantity,
      Date shipDate, String status) {
    Order order = new Order();
    order.setId(id);
    order.setPetId(petId);
    order.setQuantity(quantity);
    order.setShipDate(shipDate);
    order.setStatus(status);
    return order;
  }
}