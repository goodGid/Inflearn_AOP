package hello.aop;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import hello.aop.order.OrderRepository;
import hello.aop.order.OrderService;
import hello.aop.order.aop.AspectV3;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
/**
 * @Aspect 는 애스펙트라는 표식이지 컴포넌트 스캔이 되는 것은 아니다.
 * 따라서 AspectV1 를 AOP로 사용하려면 스프링 빈으로 등록해야 한다.
 *
 * 스프링 빈으로 등록하기위해
 * Import를 하게 되면 AspectV1.class가 빈으로 등록된다.
 * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현1 - 시작" 5:10초
 */
//@Import(AspectV1.class)
//@Import(AspectV2.class)
@Import(AspectV3.class)
public class AopTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void aopInfo() {
        log.info("isAopProxy, orderService={}",
                 AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository={}",
                 AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    @Test
    void exception() {
        assertThatThrownBy(() -> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class);
    }
}