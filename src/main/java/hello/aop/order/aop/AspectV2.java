package hello.aop.order.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class AspectV2 {

    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {} // Pointcut 시그니처

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
    /**
     * @Pointcut 에 포인트컷 표현식을 사용한다.
     *
     * 메서드 이름과 파라미터를 합쳐서 포인트컷 시그니처(signature)라 한다.
     *
     * 메서드의 반환 타입은 void 여야 한다. (= allOrder()의 반환 타입은 void )
     *
     * 코드 내용은 비워둔다.
     *
     * 포인트컷 시그니처는 allOrder() 이다. 이름 그대로 주문과 관련된 모든 기능을 대상으로 하는 포인트컷이다.
     * --> 표현식이 아닌 이름을 부여해서 사용이 가능하다.
     *
     * @Around 어드바이스에서는 포인트컷을 직접 지정해도 되지만, 포인트컷 시그니처를 사용해도 된다. 여기서는 @Around("allOrder()") 를 사용한다.
     *
     * private , public 같은 접근 제어자는 내부에서만 사용하면 private 을 사용해도 되지만, 다른 애스팩트에서 참고하려면 public 을 사용해야 한다
     * --> "public void allOrder()" 이런식으로도 사용 가능하다.
     * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현2 - 포인트컷 분리" 2:25초
     */
}