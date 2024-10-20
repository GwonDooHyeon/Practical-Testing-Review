package sample.cafekioskreview.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekioskreview.spring.IntegrationTestSupport;
import sample.cafekioskreview.spring.domain.history.mail.MailSendHistory;
import sample.cafekioskreview.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafekioskreview.spring.domain.order.Order;
import sample.cafekioskreview.spring.domain.order.OrderRepository;
import sample.cafekioskreview.spring.domain.order.OrderStatus;
import sample.cafekioskreview.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekioskreview.spring.domain.product.Product;
import sample.cafekioskreview.spring.domain.product.ProductRepository;
import sample.cafekioskreview.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static sample.cafekioskreview.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekioskreview.spring.domain.product.ProductType.HANDMADE;

class OrderStatisticsServiceTest extends IntegrationTestSupport {
    
    @Autowired
    private OrderStatisticsService orderStatisticsService;
    
    @Autowired
    private OrderProductRepository orderProductRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;
    
    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }
    
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 0, 0);
        
        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);
        
        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);
        
        // stubbing
        when(mailSendClient.sendMail(anyString(), anyString(), anyString(), anyString())).thenReturn(true);
        
        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");
        
        // then
        assertThat(result).isTrue();
        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1);
        assertThat(histories)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
    }
    
    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }
    
    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}
