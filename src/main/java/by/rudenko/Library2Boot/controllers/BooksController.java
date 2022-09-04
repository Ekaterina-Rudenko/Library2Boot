package by.rudenko.Library2Boot.controllers;

import by.rudenko.Library2Boot.models.Book;
import by.rudenko.Library2Boot.models.Person;
import by.rudenko.Library2Boot.services.BookService;
import by.rudenko.Library2Boot.services.PeopleService;
import by.rudenko.Library2Boot.util.BookValidator;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BooksController {

  private final BookService bookService;
  private final PeopleService peopleService;
  private final BookValidator bookValidator;

  @Autowired
  public BooksController(BookService bookService, PeopleService peopleService,
      BookValidator bookValidator) {
    this.bookService = bookService;
    this.peopleService = peopleService;
    this.bookValidator = bookValidator;
  }

  @GetMapping
  public String index(Model model,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
      @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
    if (page == null || booksPerPage == null) {
      model.addAttribute("books", bookService.index(sortByYear));
    } else {
      model.addAttribute("books", bookService.indexWithPagination(page, booksPerPage, sortByYear));
    }
    return "/books/index";
  }

  @GetMapping("/{id}")
  public String show(@PathVariable("id") int id, Model model,
      @ModelAttribute("person") Person person) {
    model.addAttribute("book", bookService.findById(id));
    Person owner = bookService.getOwner(id);
    if (owner != null) {
      model.addAttribute("owner", owner);
    } else {
      model.addAttribute("people", peopleService.index());
    }
    return "/books/show";
  }

  @GetMapping("/new")
  public String newBook(@ModelAttribute("book") Book book) {
    return "/books/new";
  }

  @PostMapping()
  public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
    bookValidator.validate(book, bindingResult);
    if (bindingResult.hasErrors()) {
      return "books/new";
    }
    bookService.save(book);
    return "redirect:/books";
  }

  @GetMapping("/{id}/edit")
  public String edit(Model model, @PathVariable("id") int id) {
    model.addAttribute("book", bookService.findById(id));
    return "books/edit";
  }

  @PatchMapping("/{id}")
  public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
      @PathVariable("id") int id) {
    bookValidator.validate(book, bindingResult);
    if (bindingResult.hasErrors()) {
      return "/books/edit";
    }
    bookService.update(id, book);
    return "redirect:/books";
  }

  @PatchMapping("/{id}/assign")
  public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
    bookService.assign(id, person);
    return "redirect:/books/" + id;
  }

  @PatchMapping("/{id}/release")
  public String release(@PathVariable("id") int id) {
    bookService.release(id);
    return "redirect:/books/" + id;
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") int id) {
    bookService.delete(id);
    return "redirect:/books";
  }

  @GetMapping("/search")
  public String searchPage() {
    return "books/search";
  }

  @PostMapping("/search")
  public String makeSearch(Model model,
      @RequestParam(value = "query") String query) {
    model.addAttribute("books", bookService.searchByPartTitle(query));
    return "books/search";
  }
}
