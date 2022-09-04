package by.rudenko.Library2Boot.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "person")
public class Person {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotEmpty(message = "Name should not be empty")
  @Size(min = 2, max = 100, message = "Name should be between 2 and 100")
  @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+", message = "Your name should be int this format: Name Surname")
  @Column(name = "full_name")
  private String fullName;

  @Min(value = 1900, message = "Year of birth should be greater than 1900")
  @Column(name = "year_of_birth")
  private int yearOfBirth;

  @OneToMany(mappedBy = "owner")
  private List<Book> books;

  public Person(int id, String fullName, int yearOfBirth) {
    this.fullName = fullName;
    this.yearOfBirth = yearOfBirth;
  }

  public Person() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public int getYearOfBirth() {
    return yearOfBirth;
  }

  public void setYearOfBirth(int yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  public List<Book> getBooks() {
    return books;
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }
}
