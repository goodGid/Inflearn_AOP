package hello.aop.order.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class AspectV1 {

    /**
     * @Around 애노테이션의 값인 execution(* hello.aop.order..*(..)) 는 포인트컷이 된다.
     * @Around 애노테이션의 메서드인 doLog 는 어드바이스( Advice )가 된다.
     * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현1 - 시작" 2:30초
     */

    /**
     * execution(* hello.aop.order..*(..)) 는
     * hello.aop.order 패키지와 그 하위 패키지( .. )를 지정하는 AspectJ 포인트컷 표현식이다.
     * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현1 - 시작" 2:40초
     */
    @Around("execution(* hello.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
    /**
     * 이제 OrderService , OrderRepository 의 모든 메서드는 AOP 적용의 대상이 된다.
     * 참고로 스프링은 프록시 방식의 AOP를 사용하므로 프록시를 통하는 메서드만 적용 대상이 된다.
     * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현1 - 시작" 2:50초
     */
}

/**
 * ## 참고 1
 * 스프링 AOP는 AspectJ의 문법을 차용하고, 프록시 방식의 AOP를 제공한다.
 * AspectJ를 직접 사용하는 것이 아니다.
 * 스프링 AOP를 사용할 때는 @Aspect 애노테이션을 주로 사용하는데,
 * 이 애노테이션도 AspectJ가 제공하는 애노테이션이다.
 * --> 스프링이 AspectJ를 가져다 사용하는거지 그 자체를 사용하는 건 아니다.
 * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현1 - 시작" 3:00초
 *
 * ## 참고 2
 * @Aspect 를 포함한 org.aspectj 패키지 관련 기능은 aspectjweaver.jar 라이브러리가 제공하는 기능이다.
 * 앞서 build.gradle 에 spring-boot-starter-aop 를 포함했는데,
 * 이렇게 하면 스프링의 AOP 관련 기능과 함께 aspectjweaver.jar 도 함께 사용할 수 있게 의존 관계에 포함된다.
 * 그런데 스프링에서는 AspectJ가 제공하는 애노테이션이나 관련 인터페이스만 사용하는 것이고,
 * 실제 AspectJ가 제공하는 컴파일, 로드타임 위버 등을 사용하는 것은 아니다.
 * 스프링은 지금까지 우리가 학습한 것 처럼 프록시 방식의 AOP를 사용한다.
 * --> 스프링은 [컴파일, 로드타임 위버]는 아니고 "프록시 방식"의 AOP를 사용한다.
 * ref : "스프링 핵심 원리 - 고급편" -> "스프링 AOP 구현" -> "스프링 AOP 구현1 - 시작" 3:50초
 */

