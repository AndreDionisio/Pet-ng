import { Injectable } from '@angular/core';

import { Hero } from './hero';

import { HEROES } from './mock-heroes';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class HeroService {
private heroesUrl = 'http://localhost:8080/hello/All';  // URL to web api all
private singleUrl = 'http://localhost:8080/hello/pet';
private updateUrl = 'http://localhost:8080/hello/Update';
constructor(
  private http: HttpClient,
  private messageService: MessageService) { }
  //getHeroes(): Observable<Hero[]> {
    // TODO: send the message _after_ fetching the heroes
    //this.log('HeroService: fetched heroes');
    //return of(HEROES);
  //}
  /** GET heroes from the server */
  getHeroes (): Observable<Hero[]> {
    this.log('HeroService: ');
    return this.http.get<Hero[]>(this.heroesUrl).pipe(tap(_ => this.log('fetched heroes')),
                                                      catchError(this.handleError<Hero[]>('getHeroes', []))
                                                    );
  }
  /** GET hero by id. Will 404 if id not found */
  getHero(id: number): Observable<Hero> {
  this.log(`HeroService: fetched hero id=${id}`);
    const url = `${this.singleUrl}/${id}`;
    return this.http.get<Hero>(url).pipe(
      tap(_ => this.log(`fetched hero id=${id}`)),
      catchError(this.handleError<Hero>(`getHero id=${id}`))
    );
  }
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' ,'Access-Control-Allow-Origin':'*','Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS'})
  };
  /** PUT: update the hero on the server */
  updateHero (hero: Hero): Observable<any> {
    return this.http.put(this.updateUrl, hero,this.httpOptions).pipe(
      tap(_ => this.log(`updated hero id=${hero.id}`)),
      catchError(this.handleError<any>('updateHero'))
    );
  }
  //getHero(id: number): Observable<Hero> {
    // TODO: send the message _after_ fetching the hero
  //  this.log(`HeroService: fetched hero id=${id}`);
 //   return of(HEROES.find(hero => hero.id === id));
 // }
  /** Log a HeroService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`HeroService: ${message}`);
  }
  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
