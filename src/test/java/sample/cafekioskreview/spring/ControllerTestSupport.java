package sample.cafekioskreview.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekioskreview.spring.api.controller.order.OrderController;
import sample.cafekioskreview.spring.api.controller.product.ProductController;
import sample.cafekioskreview.spring.api.service.order.OrderService;
import sample.cafekioskreview.spring.api.service.product.ProductService;

@WebMvcTest(controllers = {OrderController.class, ProductController.class})
public abstract class ControllerTestSupport {
    
    @Autowired
    protected ObjectMapper objectMapper;
    
    @Autowired
    protected MockMvc mockMvc;
    
    @MockBean
    protected OrderService orderService;
    
    @MockBean
    protected ProductService productService;
}
