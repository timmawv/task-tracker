import { Injectable } from '@angular/core';
import { HttpHeaders, HttpParams } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class BaseService {

  constructor() { }

  public getStandardHeaders(): any {
    return new HttpHeaders()
      .set('Content-Type','application/json')
      .set('Accept', 'application/json')
  }

  public getStandardParameters(): any {
    return new HttpParams({
    })
  }
}
