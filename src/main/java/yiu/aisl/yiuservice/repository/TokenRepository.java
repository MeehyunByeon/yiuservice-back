package yiu.aisl.yiuservice.repository;

import org.springframework.data.repository.CrudRepository;
import yiu.aisl.yiuservice.domain.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {
}
