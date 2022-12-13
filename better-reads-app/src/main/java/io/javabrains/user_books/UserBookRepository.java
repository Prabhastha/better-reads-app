package io.javabrains.user_books;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRepository extends CassandraRepository<UserBooks, UserBooksPrimaryKey> {
}
