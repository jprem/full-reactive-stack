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

  constructor(private http: HttpClient) {}

  getQuotes(page?: number, size?: number): Observable<Array<Quote>> {
    this.quotes = [];
    let url = this.url;
    if (page != null) {
      url = this.urlPaged + '?page=' + page + '&size=' + size;
    }
    return this.http.get<Array<Quote>>(url);
  }

  deleteQuote(page?: number, size?: number, id?: number): Observable<Array<Quote>> {
    this.quotes = [];
    let url = this.url;
    if (page != null) {
      url = this.urlDelete + '?page=' + page + '&size=' + size + '&id=' + id;
    }
    return this.http.get<Array<Quote>>(url);
  }

}
