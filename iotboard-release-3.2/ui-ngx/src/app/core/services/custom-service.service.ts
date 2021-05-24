///
/// Copyright © 2016-2021 The Thingsboard Authors
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { PageData } from '@shared/models/page/page-data';
import { Customer } from '@shared/models/customer.model';
import { defaultHttpOptionsFromConfig, RequestConfig } from '../http/http-utils';

@Injectable({
  providedIn: 'root'
})
export class CustomServiceService {
  // baseUrl='http://localhost:4200/api'
  // baseUrl='http://localhost:8080/api/'
  // baseUrl='http://13.58.211.197/api/'
  // private search ="customerByFilter";
  constructor(private http: HttpClient,
    private route:ActivatedRoute) {
      console.log("route",route);
   }

  //  getCustomers(): Observable<any> {
  //   var header = new HttpHeaders().set("X-Authorization", "Bearer " + localStorage.getItem('jwt_token'));
  //   return this.http.get<any>(
  //     // `http://13.58.211.197/api/customers?pageSize=10&page=0&sortProperty=createdTime&sortOrder=DESC`,{headers:header}
  //     `http://localhost/api/customers?pageSize=10&page=0&sortProperty=createdTime&sortOrder=DESC`,{headers:header}
  //   );
  // }

  
  getCustomer(title,id, config?: RequestConfig): Observable<any>{
    // var url = this.baseUrl + this.search + "?" + title + "=" + id;
    // return this.http.get<PageData<Customer>>(`/api/customers${pageLink.toQuery()}`,
    //   defaultHttpOptionsFromConfig(config));
    return this.http.get<PageData<Customer>>(`/api/customer/guestLookup/?`+ title + "=" + id,
      defaultHttpOptionsFromConfig(config));
    // var header = new HttpHeaders().set("X-Authorization", "Bearer " + localStorage.getItem('jwt_token'));
    // return this.http.get<any>(url,{headers:header})

  }

  getRoomDetails(textSearch,pageSize,page, config?: RequestConfig): Observable<any>{
    return this.http.get<PageData<any>>(`/api/tenant/assetInfos?textSearch`+ "=" + textSearch 
    + "&pageSize=" + pageSize + "&page=" + page,
    defaultHttpOptionsFromConfig(config));
  }

  getTotalCount(config?: RequestConfig): Observable<any>{
    return this.http.get<PageData<Customer>>(`/api/tenant/asset/count`,
      defaultHttpOptionsFromConfig(config));
  }
}
