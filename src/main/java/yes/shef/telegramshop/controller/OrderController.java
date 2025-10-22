package yes.shef.telegramshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import yes.shef.telegramshop.dto.OrderDto;
import yes.shef.telegramshop.entity.Order;
import yes.shef.telegramshop.mapper.OrderMapper;
import yes.shef.telegramshop.service.OrderService;

import java.util.List;

/**
 * {@link Order} management via HTTP server, CRUD operations.
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @PostMapping("/")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderService.createOrder(orderDto);
        OrderDto createdOrder = orderMapper.orderToOrderDto(order);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{orderId}")
                        .buildAndExpand(createdOrder.getId())
                        .toUri())
                .body(createdOrder);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtoList = orderService.getAllOrders()
                .stream()
                .map(orderMapper::orderToOrderDto)
                .toList();
        return ResponseEntity.ok(orderDtoList);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrderById(Long.parseLong(orderId));
        OrderDto orderDto = orderMapper.orderToOrderDto(order);
        return ResponseEntity.ok(orderDto);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderDto> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrderById(Long.parseLong(orderId));
        return ResponseEntity.noContent().build();
    }
}
