package sample.cafekioskreview.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

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
        ProductType bakery = ProductType.BAKERY;
        
        // when
        boolean result = ProductType.containsStockType(bakery);
        
        // then
        assertThat(result).isTrue();
    }
    
    @DisplayName("상품 타입이 재고 관련 타입인지를 확인한다.")
    @CsvSource({
        "BAKERY, true",
        "BOTTLE, true",
        "HANDMADE, false",
    })
    @ParameterizedTest
    void containsStockType1(ProductType productType, boolean expected) {
        // when
        boolean result = ProductType.containsStockType(productType);
        
        // then
        assertThat(result).isEqualTo(expected);
    }
    
    private static Stream<Arguments> provideProductTypesForCheckingStockType() {
        return Stream.of(
            Arguments.of(ProductType.HANDMADE, false),
            Arguments.of(ProductType.BAKERY, true),
            Arguments.of(ProductType.BOTTLE, true)
        );
    }
    
    @DisplayName("상품 타입이 재고 관련 타입인지를 확인한다.")
    @MethodSource("provideProductTypesForCheckingStockType")
    @ParameterizedTest
    void containsStockType2(ProductType productType, boolean expected) {
        // when
        boolean result = ProductType.containsStockType(productType);
        
        // then
        assertThat(result).isEqualTo(expected);
    }
    
    
}
