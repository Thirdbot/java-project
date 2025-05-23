package com.mycompany.java.project.classes.utils;
import java.util.ArrayList;
import java.sql.SQLException;
import javax.swing.JTextField;
import com.mycompany.java.project.classes.User;
import com.mycompany.java.project.classes.Book;
import com.mycompany.java.project.classes.OrderBook;
import com.mycompany.java.project.classes.customs.exceptions.JBookException;
import com.mycompany.java.project.db.Authorization;
import com.mycompany.java.project.db.controllers.BookController;
import com.mycompany.java.project.db.controllers.UserController;
import static com.mycompany.java.project.classes.utils.Helper.getSingleQuotes;

public final class Validator {
    public static boolean isValidString(String value){
        if(value == null || value.isEmpty() || value.isBlank()) {
            return false;
        }
        return true;
    }

    public static boolean isInvalidString(String value){
        return !isValidString(value);
    }

    public static boolean isDuplicateUser(User user) throws SQLException, JBookException {
        return Authorization.isAuthorized(user);
    }

    public static boolean isDuplicateUser(int userId, String username, String email) throws SQLException, JBookException {
        UserController userController = new UserController();
        return userController.getUser("SELECT * FROM users WHERE (username = " + getSingleQuotes(username) + "OR email = " + getSingleQuotes(email) + ") AND user_id != " + userId) != null;
    }

    public static boolean isExistsBook(Book book) throws SQLException, JBookException {
        BookController bookController = new BookController();
        return bookController.getBooks("SELECT * FROM books WHERE (book_name = " + getSingleQuotes(book.getBookName()) + " OR isbn = " + getSingleQuotes(book.getIsbn()) + ") AND book_id != " + book.getBookId()).size() >= 1;
    }

    public static boolean isEmptyField(JTextField textField){
        return (textField.getText().isEmpty() || textField.getText().isBlank());
    }

    public static void validateOrderingBook(Book book, OrderBook orderBook, int quantity) throws JBookException{
        if(quantity <= 0){
            throw new JBookException("Invalid quantity!");
        }

        if(quantity > book.getRemain()){
            throw new JBookException("An error occurred. The number of books selected exceeds the number of items remaining!");
        }

        if(book.getRemain() <= 0){
            throw new JBookException("An error occurred. The number of books selected exceeds the number of items remaining!");
        }
    }

    public static boolean isValidPassword(String password) {
        return isValidString(password) && password.length() >= 8;
    }

    public static boolean isValidPassword(User user){
        return isValidPassword(user.getPassword());
    }

    public static boolean isDuplicateOrderBook(ArrayList<OrderBook> orderBooks, String bookName){
        for(Book orderBook : orderBooks){
            if(orderBook.getBookName().toLowerCase().equals(bookName.toLowerCase())){
                return true;
            }
        }
        return false;
    }
}