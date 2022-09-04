package by.rudenko.Library2Boot.services;

import by.rudenko.Library2Boot.models.Book;
import by.rudenko.Library2Boot.models.Person;
import by.rudenko.Library2Boot.repositories.BooksRepository;
import by.rudenko.Library2Boot.repositories.PeopleRepository;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

  private final PeopleRepository peopleRepository;
  private final BooksRepository booksRepository;

  @Autowired
  public PeopleService(PeopleRepository peopleRepository,
      BooksRepository booksRepository) {
    this.peopleRepository = peopleRepository;
    this.booksRepository = booksRepository;
  }

  public List<Person> index() {
    return peopleRepository.findAll();
  }

  public Person findById(int id) {
    Optional<Person> foundedPerson = peopleRepository.findById(id);
    return foundedPerson.orElse(null);
  }

  public Optional<Person> findByFullName(String fullName) {
    List<Person> foundedPeople = peopleRepository.findByFullName(fullName);
    return foundedPeople.stream().findAny();
  }

  @Transactional
  public void save(Person person) {
    peopleRepository.save(person);
  }

  @Transactional
  public void update(int id, Person updatedPerson) {
    updatedPerson.setId(id);
    peopleRepository.save(updatedPerson);
  }

  @Transactional
  public void delete(int id) {
    peopleRepository.deleteById(id);
  }

  public List<Book> getBooksByPersonId(int id) {
    Optional<Person> person = peopleRepository.findById(id);
    if (person.isPresent()) {
      Hibernate.initialize(person.get().getBooks());

      person.get().getBooks().forEach(book -> {
        long diffInMillies = Math.abs(new Date().getTime() - book.getDate().getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff > 10) {
          book.setExpired(true);
        }
      });
      return person.get().getBooks();
    } else {
      return Collections.emptyList();
    }
  }

}
