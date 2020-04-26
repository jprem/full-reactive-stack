package com.thepracticaldeveloper.reactiveweb.controller;

import com.thepracticaldeveloper.reactiveweb.domain.Quote;
import com.thepracticaldeveloper.reactiveweb.repository.QuoteMongoBlockingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class QuoteBlockingController {

    private static final int DELAY_PER_ITEM_MS = 100;

    private QuoteMongoBlockingRepository quoteMongoBlockingRepository;

    public QuoteBlockingController(final QuoteMongoBlockingRepository quoteMongoBlockingRepository) {
        this.quoteMongoBlockingRepository = quoteMongoBlockingRepository;
    }

    @GetMapping("/quotes-blocking")
    public Iterable<Quote> getQuotesBlocking() throws Exception {
        Thread.sleep(DELAY_PER_ITEM_MS * quoteMongoBlockingRepository.count());
        return quoteMongoBlockingRepository.findAll();
    }

    @GetMapping("/quotes-blocking-paged")
    public Iterable<Quote> getQuotesBlocking(final @RequestParam(name = "page") int page,
                                             final @RequestParam(name = "size") int size) throws Exception {
        Thread.sleep(DELAY_PER_ITEM_MS * size);
        return quoteMongoBlockingRepository.retrieveAllQuotesPaged(PageRequest.of(page, size));
    }

    // Below combines the delete and get operation to refresh the page
    // the other alternative is to split the delete to seperate endpoint mentioned below and front-end calls the delete first
    // and calls existing get endpoint to refresh the page
    @GetMapping("/quotes-delete")
    public Iterable<Quote> deleteQuotesBlocking(final @RequestParam(name = "page") int page,
                                                final @RequestParam(name = "size") int size,
                                                final @RequestParam(name = "id") int id) throws Exception {
        Thread.sleep(DELAY_PER_ITEM_MS * size);
        Optional<Quote> quote = quoteMongoBlockingRepository.findById(String.valueOf(id));
        if (quote.isPresent()) {
            quoteMongoBlockingRepository.delete(quote.get());
        }
        return quoteMongoBlockingRepository.retrieveAllQuotesPaged(PageRequest.of(page, size));
    }

    @DeleteMapping("/quote/{id}")
    public ResponseEntity<String> deleteQuote(@PathVariable String id) {
        Optional<Quote> quote = quoteMongoBlockingRepository.findById(id);
        if (!quote.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        quoteMongoBlockingRepository.delete(quote.get());
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
