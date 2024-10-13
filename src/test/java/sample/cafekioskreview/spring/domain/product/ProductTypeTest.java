package sample.cafekioskreview.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductTypeTest {
    
    @DisplayName("상품 타입이 재고 관련 타입이 아닌지를 확인한다.")
    @Test
    void isNotContainsStockType() {
        // given
        ProductType handmade = ProductType.HANDMADE;
        
        // when
        boolean result = ProductType.containsStockType(handmade);
        
        // then
        assertThat(result).isFalse();
    }
    
    @DisplayName("상품 타입이 재고 관련 타입인지를 확인한다.")
    @Test
    void containsStockType() {
        // given
        ProductType handmade = ProductType.BAKERY;
        
        // when
        boolean result = ProductType.containsStockType(handmade);
        
        // then
        assertThat(result).isTrue();
    }
    
}
