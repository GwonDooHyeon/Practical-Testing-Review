package sample.cafekioskreview.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekioskreview.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekioskreview.spring.api.service.product.response.ProductResponse;
import sample.cafekioskreview.spring.domain.product.Product;
import sample.cafekioskreview.spring.domain.product.ProductRepository;
import sample.cafekioskreview.spring.domain.product.ProductSellingStatus;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        String nextProductNumber = createNextProductNumber();
        
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);
        
        return ProductResponse.of(savedProduct);
    }
    
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        
        return products.stream()
            .map(ProductResponse::of)
            .toList();
    }
    
    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }
        
        return "%03d".formatted(Integer.parseInt(latestProductNumber) + 1);
    }
    
}
