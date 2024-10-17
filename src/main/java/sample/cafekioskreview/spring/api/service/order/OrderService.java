package sample.cafekioskreview.spring.api.service.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekioskreview.spring.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekioskreview.spring.api.service.order.response.OrderResponse;
import sample.cafekioskreview.spring.domain.order.Order;
import sample.cafekioskreview.spring.domain.order.OrderRepository;
import sample.cafekioskreview.spring.domain.product.Product;
import sample.cafekioskreview.spring.domain.product.ProductRepository;
import sample.cafekioskreview.spring.domain.product.ProductType;
import sample.cafekioskreview.spring.domain.stock.Stock;
import sample.cafekioskreview.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    
    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = findProductsBy(productNumbers);
        
        deductStockQuantities(products);
        
        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }
    
    private void deductStockQuantities(List<Product> products) {
        // 재고 차감 체크가 필요한 상품 filter
        List<String> stockProductNumbers = extractStockProductNumbers(products);
        
        // 재고 엔티티 조회
        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
        
        // 상품별 counting
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);
        
        // 재고 차감 시도
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();
            
            if (stock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }
    }
    
    private Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
            .collect(Collectors.groupingBy(productNumber -> productNumber, Collectors.counting()));
    }
    
    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        return stocks.stream()
            .collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
    }
    
    private List<String> extractStockProductNumbers(List<Product> products) {
        return products.stream()
            .filter(product -> ProductType.containsStockType(product.getType()))
            .map(Product::getProductNumber)
            .toList();
    }
    
    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductNumber, product -> product));
        
        return productNumbers.stream()
            .map(productMap::get)
            .toList();
    }
    
}
