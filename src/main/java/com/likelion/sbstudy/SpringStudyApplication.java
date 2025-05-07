package hello.springstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA가 알아서 엔티티가 생성, 수정될 때의 시간을 자동으로 넣어줌
@SpringBootApplication
public class SpringStudyApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringStudyApplication.class, args);
  }

}
