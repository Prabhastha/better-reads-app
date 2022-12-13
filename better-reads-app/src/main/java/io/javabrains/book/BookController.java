package io.javabrains.book;

import io.javabrains.user_books.UserBookRepository;
import io.javabrains.user_books.UserBooks;
import io.javabrains.user_books.UserBooksPrimaryKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
public class BookController {

    private final String COVER_IMAGE_ROOT = "https://covers.openlibrary.org/b/id/";
    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserBookRepository userBookRepository;


    @GetMapping(value = "/books/{bookId}")
    public String getBook(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principal){
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            String coverImageUrl = "/images/no-image.png";
            if (book.getCoverId() != null & book.getCoverId().size() > 0){
                coverImageUrl = COVER_IMAGE_ROOT + book.getCoverId().get(0) + "-L.jpg";

            }
            model.addAttribute("coverImage", coverImageUrl);
            model.addAttribute("book", book);
            if(principal != null & principal.getAttribute("login") != null){
                String userId = principal.getAttribute("login");
                model.addAttribute("loginId", userId);
                UserBooksPrimaryKey key = new UserBooksPrimaryKey();
                key.setBookId(bookId);
                key.setUserId(userId);
                Optional<UserBooks> userBooks = userBookRepository.findById(key);
                if(userBooks.isPresent()){
                    model.addAttribute("userBooks",userBooks.get());
                }else{
                    model.addAttribute("userBooks", new UserBooks());
                }
            }
            return "book";
        }
        return "book-not-found";
    }
}
