작성 중... 브런치로 깔끔하게 정리해서 다시 발행 예정
아래 글은 대충 쓴글


# 스프링 웹플럭스
이번 주말 스터디 주제는, 스프링 웹플럭스 이다. 참고로 지난 카카오 세미나에 참석하지 못한 것을 매우 안타깝게 생각한다. 높은 경쟁률으로 인해서, 참석을 못했는데 개인적으로는 너무 가고 싶은 세미나 중 하나였다. 2012년인가? 당시 DevOn 이라는 세미나를 참석한 기억이 있고, 어쿠스틱 밴드가 나와서 공연을 했던 기억이 있다ㅎㅎ 아무튼 세션 중에 토비님의 발표를 못들어서 너무 아쉽다. 필자는 최근에 스프링 리액티브에 조금씩 관심을 갖기 시작했다. 2년전에 스프링캠프에서 토비님의 강의를 들었을 때도 별 관심이 없었는데, 최근에 서비스를 운영하면서 조금씩 리액티브 프로그래밍의 필요성을 느끼고 있다. 그런 관심으로 이번 주말에는 스프링 리액티브 기초 과정을 공부해본다. 

#### 참고 레퍼런스
https://www.kotlindevelopment.com/kotlin-webflux/


## 스프링 리액티브
일단, 필자가 아는 수준은 세미나를 통해서 두 번 정도 들어본 정도이다. 따로 코딩을 해본적은 이번이 처음이다. 스프링 리액티브는 스프링 MVC 와 함께 사용할 수 있는데 왜 등장했을까? 일단, MVC 는 서블릿 컨테이너 기반으로 동작하고 톰캣, Jetty 같은 애플리케이션서버에서 동작하는데 서블릿API는 서버와 클라이언트 간의 웹서비스 통신을 매우 엄격(Strict)하게 동작한다. (This API is quite strict on how communication goes from client to server and does not leave much room for flexibility. ) 하지만 웹플럭스는 서블릿API에 의존적이지 않다. 

#### 기존 클라이언트-서버 아키텍처
HTTP 요청을 하기 전에 클라이언트와 서버 모두 연결을 설정하기 위해 소켓(포트)에 바인딩한다.  클라이언트가 연결 요청을 보내는 동안 서버가 소켓에서 수신 대기한다. 연결이 설정되면 클라이언트가 데이터 전송을 시작하고 서버가 데이터를 처리하고 응답을 보낸다. 그런 다음 연결을 닫을 수 있다. 대부분의 시간은 연결이 설정되고 데이터가 수신되기를 기다리는 데 사용된다. 

그림1 - 동기 아키텍처



> 필자는 해당 아키텍처를 개선하기 위해, 주로 메시지패턴을 활용했었다.

동기vs비동기를 나눠서 동기 요청은 HTTP 통신을 유지하고, 비동기 요청은 AMQP와 같은 메시지 통신 프로토콜을 사용한다. 메시지 패턴은 부하 분산 및 확장성 등 많은 장점이 있지만, 시스템이 복잡해지고, 일반적인 API 연동 방식보다는 구현이 어려운 편이다. 어쩃든 스프링 리액티브를 활용하면, 메시지패턴을 구현하지 않더라도 비동기 기반 아키텍처를 구축할 수 있을 것 같다.  

#### non-blocking IO
Non-Blocking IO 는 비동기 이벤트-기반으로 동작한다. 스프링 웹플럭스에서 나오면서 동시에 Netty 를 지원하게 되는데, Netty는 비동기 이벤트-기반의 고성능 네트워크 애플리케이션 프레임워크이다. 

그림2 - Non blocking 아키텍처

Non-Blocking 방식은 클라이언트의 커넥션에 웹서버Thread를 바인딩하지 않는다. 대신에, 개별 버퍼를 사용해서 알림을 주고 받는다. Non-Blocking IO 는 여러 연결을 하나의 Thread 로 처리할 수 있다.  


#### Thread Model 간략 비교

**Tomcat**
 - 1 Request = 1 Thread 
 - 너무 많은 요청이 들어올수록 메모리를 많이 소모한다.

**Node.js** 
 - All Request = 1 Thread
 - Node.js 는 필자가 자세히는 모르지만, 

**Netty**
 - Many Request = 1 Thread
 - 유연한 모델이다. 

잘 알지도 못하면서 글을 쓰니깐, 어렵다. 코드를 먼저 작성해서 이해해보자. 

## 스프링 부트 웹플럭스 샘플 코드 작성
아주 간단한 스프링 웹플럭스를 코드를 작성한다. 

그림 - 클래스 다이어그램

- Person 
- ProgrammerRepository
- ProgrammerUseCase 
- ProgrammerController
- ProgrammerProvider

#### 디펜던시 추가
스프링 부트 스타터 웹플럭스 를 디펜던시 추가한다. 
````groovy
dependencies {  
   compile('org.springframework.boot:spring-boot-starter-webflux')
   //생략...
````

#### 컨트롤러
컨트롤러는 기존의 MVC 방식과 유사하다. 물론, 기존 방식이 아니라 펑셔널하게 작성할 수 도 있는데, 조금 이후에 다시 설명하겠다. 웹플럭서는 Mono 와 Flux 로 데이터를 응답하는데 Mono 는 단일데이터이고, Flux 는 리스트이다. 

````java
@RestController  
@RequiredArgsConstructor  
public class ProgrammerController {

	private final ProgrammerUseCase programmerUseCase;

	@GetMapping("/programmers/{name}")  
	public Mono<Person> hello(@PathVariable("name") String name) {  
	    return Mono.just(programmerUseCase.findByName(name));  
	}  
	  
	@GetMapping("/programmers")  
	public Flux<Person> all() {  
	    return... 생략 
	}
}
````

#### Functional
@EnableWebFlux 를 사용하면 펑셔널하게 엔드포인트를 정의할 수 있다. 

````java
ProgrammerFunctionalConfiguration.java
@Configuration  
@EnableWebFlux  
public class ProgrammerFunctionalConfiguration {  
  
    @Bean  
  public RouterFunction<ServerResponse> routes(ProgrammerFunctionalHandler handler) {  
        return RouterFunctions.route(GET("/personfunctional"), handler::findByName);  
  }  
}

ProgrammerFunctionalHandler.java
@Component  
@RequiredArgsConstructor  
public class ProgrammerFunctionalHandler {  
  
	private final ProgrammerUseCase programmerUseCase;  
  
	 public Mono<ServerResponse> findByName(ServerRequest request) {  
	        Mono<Person> helloworldMono = Mono.just(programmerUseCase.findByName("eddy"));  
	 return ServerResponse.ok().body(helloworldMono, Person.class);  
  }  
}
````

#### 테스트코드 작성
테스트 코드 작성은 아직 자세히 모르겠다. 일단 아래와 같이 해봤는데, @WebFluxTest 의 용도는 잘 몰라서 일단 주석처리했다. 추가하면 오류가 난다. 아마 @SpringBootTest 랑 꼬이거나 뭔가 필자가 잘못한게 있을 것이다. 

````java

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebFluxTest //해당 어노테이션의 용도를 아직은 잘 모르겠다. 
public class ProgrammerControllerTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProgrammerRepository programmerRepository;

    @BeforeEach
    @DisplayName("테스트 데이터 초기화")
    public void init(){
        //Autowired 를 안한다면 아래와 같이 작성해야 한다. 
        //webTestClient = WebTestClient.bindToApplicationContext(applicationContext).configureClient().build();
        programmerRepository.put(new Person("eddy", 1981));
    }

    @Test
    @DisplayName("이름 검색 응답 테스트")
    public void programmerResonseTest(){

        Person person = webTestClient.get().uri("/programmers/eddy").exchange()
                .expectStatus().isOk()
                .expectBody(Person.class)
                .returnResult().getResponseBody();
                
        assertEquals(person.getName(), "eddy");
        assertEquals(person.getBorn().intValue(), 1981);
    }
}
````


