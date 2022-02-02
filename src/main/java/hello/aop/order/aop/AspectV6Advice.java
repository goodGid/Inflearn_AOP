package hello.aop.order.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class AspectV6Advice {

    /**
     * 사실 @Around 를 제외한 나머지 어드바이스들은
     * @Around 가 할 수 있는 일의 일부만 제공할 뿐이다.
     * 따라서 @Around 어드바이스만 사용해도 필요한 기능을 모두 수행할 수 있다.
     * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현6 - 어드바이스 종류" 11:40초
     */
    @Around("hello.aop.order.aop.PointCuts.orderAndService()")
    /**
     * ProceedingJoinPoint는 org.aspectj.lang.JoinPoint의 하위 타입이다.
     * = ProceedingJoinPoint extends JoinPoint { ... }
     *
     * ## JoinPoint 인터페이스의 주요 기능
     * getArgs() : 메서드 인수를 반환합니다.
     * getThis() : 프록시 객체를 반환합니다.
     * getTarget() : 대상 객체를 반환합니다.
     * getSignature() : 조언되는 메서드에 대한 설명을 반환합니다.
     * toString() : 조언되는 방법에 대한 유용한 설명을 인쇄합니다.
     *
     * ## ProceedingJoinPoint 인터페이스의 주요 기능
     * proceed() : 다음 어드바이스나 타켓을 호출한다.
     */
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            //@Before
            log.info("[around][트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            //@AfterReturning
            log.info("[around][트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            //@AfterThrowing
            log.info("[around][트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            //@After
            log.info("[around][리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    /**
     * @Around 는 ProceedingJoinPoint.proceed() 를 호출해야 다음 대상이 호출된다.
     * 만약 호출하지 않으면 다음 대상이 호출되지 않는다.
     * 반면에 @Before 는 ProceedingJoinPoint.proceed() 자체를 사용하지 않는다.
     * 메서드 종료시 자동으로 다음 타켓이 호출된다.
     * 물론 예외가 발생하면 다음 코드가 호출되지는 않는다.
     * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현6 - 어드바이스 종류" 14:10초
     */
    @Before("hello.aop.order.aop.PointCuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) { // ProceedingJoinPoint가 아니라 JoinPoint이다.
        log.info("[before] {}", joinPoint.getSignature());
        // Object result = joinPoint.proceed(); 가 자동으로 호출된다.
    }

    /**
     * @Around 와 다르게 반환되는 객체를 변경할 수는 없다.
     * 반환 객체를 변경하려면 @Around 를 사용해야한다.
     */
    @AfterReturning(value = "hello.aop.order.aop.PointCuts.orderAndService()",
            returning = "result")
    public void doReturn(JoinPoint joinPoint,
                         Object result) { // "Object result"에는 "returning"에 적은 변수명(= "result")이랑 매칭되어서 결과 값이 들어온다.
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    /**
     * ## "Object result" 값에 대하여
     * ( value 값을 orderAndService() -> allOrder()로 변경 )
     *
     * public String save(String itemId) { ... }
     * save 메소드의 반환 타입이 String 이다.
     * 그러면 "String extends Object" 이므로
     * public void doReturn(JoinPoint joinPoint, Object result) 로 받을 수 있다.
     *
     * 만약
     * public void doReturn(JoinPoint joinPoint, String result) 로 변경하게 되면
     * 해당 메소드는 동작한다.
     *
     * 만약
     * public void doReturn(JoinPoint joinPoint, Integer result) 로 변경하게 되면
     * 해당 메소드는 동작하지 않는다.
     */
    @AfterReturning(value = "hello.aop.order.aop.PointCuts.allOrder()",
            returning = "result")
    public void doReturn2(JoinPoint joinPoint,
                          String result) { // "Object result"에는 "returning"에 적은 변수명(= "result")이랑 매칭되어서 결과 값이 들어온다.
        log.info("[return2] {} return2={}", joinPoint.getSignature(), result);
    }

    /**
     * @AfterReturning와 마찬가지로
     * 실제 메소드가 Return하는 타입의 부모 타입으로 받을 수 있다.
     *
     * public String save(String itemId) { ... } 에서
     * IllegalStateException를 Return 하므로 Exception 타입으로 받을 수 있다.
     */
    @AfterThrowing(value = "hello.aop.order.aop.PointCuts.orderAndService()",
            throwing = "ex")
    public void doThrowing(JoinPoint joinPoint,
                           Exception ex) { // "Exception ex"에는 "throwing"에 적은 변수명(= "ex")이랑 매칭되어서 결과 값이 들어온다.
        log.info("[ex] {} message={}", joinPoint.getSignature(),
                 ex.getMessage());
    }

    @After("hello.aop.order.aop.PointCuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }

}