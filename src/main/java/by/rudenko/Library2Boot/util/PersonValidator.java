package by.rudenko.Library2Boot.util;

import by.rudenko.Library2Boot.models.Person;
import by.rudenko.Library2Boot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

  private final PeopleService peopleService;

  @Autowired
  public PersonValidator(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Person.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Person person = (Person) target;
    if (peopleService.findByFullName(person.getFullName()).isPresent()) {
      errors.rejectValue("fullName", "", "This full name already exists.");
    }
  }
}
