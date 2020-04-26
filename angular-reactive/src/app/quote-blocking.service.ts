import { Injectable } from '@angular/core';

import { Quote } from './quote';

import {Observable} from 'rxjs/Observable';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class QuoteBlockingService {

  quotes: Quote[] = [];
  url: string = 'http://localhost:8080/quotes-blocking';
  urlPaged: string = 'http://localhost:8080/quotes-blocking-paged';
  urlDelete: string = 'http://localhost:8080/quotes-delete';
  urlDeleteById: string = 'http://localhost:8080/quote/{id}';

  constructor(private http: HttpClient) {}

  getQuotes(page?: number, size?: number): Observable<Array<Quote>> {
    this.quotes = [];
    let url = this.url;
    if (page != null) {
      url = this.urlPaged + '?page=' + page + '&size=' + size;
    }
    return this.http.get<Array<Quote>>(url);
  }

  // Call the delete and get endpoint
  /*deleteQuote(page?: number, size?: number, id?: number): Observable<Array<Quote>> {
    this.quotes = [];
    let url = this.url;
    if (page != null) {
      url = this.urlDelete + '?page=' + page + '&size=' + size + '&id=' + id;
    }
    return this.http.get<Array<Quote>>(url);
  }*/

  // Call the delete endpoint
  deleteQuote(id?: number): Observable<{}> {
    this.quotes = [];
    let url = this.url;
    if (id != null) {
      url = this.urlDeleteById.replace('{id}', id.toString());
    }
    return this.http.delete(url);
  }
}
