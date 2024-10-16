package sample.cafekioskreview.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekioskreview.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekioskreview.spring.domain.product.ProductType.HANDMADE;

//@SpringBootTest
@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {
    
    @Autowired
    private ProductRepository productRepository;
    
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        
        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));
        
        // then
        assertThat(products).hasSize(2);
        assertThat(products)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }
    
    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        
        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));
        
        // then
        assertThat(products).hasSize(2);
        assertThat(products)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }
    
    @DisplayName("마지막으로 저장한 상품의 상품 번호를 읽어온다.")
    @Test
    void findLatestProductNumber() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        
        // when
        String latestProductNumber = productRepository.findLatestProductNumber();
        
        // then
        assertThat(latestProductNumber).isEqualTo("003");
    }
    
    @DisplayName("마지막으로 저장한 상품의 상품 번호를 읽어오는 경우, 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {
        // when
        String latestProductNumber = productRepository.findLatestProductNumber();
        
        // then
        assertThat(latestProductNumber).isNull();
    }
    
    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
    
}
