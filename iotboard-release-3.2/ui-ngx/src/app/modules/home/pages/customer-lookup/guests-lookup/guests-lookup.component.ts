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

import { Component, OnInit } from '@angular/core';
import { MenuService } from '@core/services/menu.service';
import { BreakpointObserver, BreakpointState } from '@angular/cdk/layout';
import { MediaBreakpoints } from '@shared/models/constants';
import { HomeSection } from '@core/services/menu.models';
import { CustomServiceService } from '@app/core/services/custom-service.service';
import {Router} from '@angular/router';

@Component({
  selector: 'tb-guests-lookup',
  templateUrl: './guests-lookup.component.html',
  styleUrls: ['./guests-lookup.component.scss']
})
export class GuestsLookupComponent implements OnInit {

  homeSections$ = this.menuService.homeSections();
  searchText = '';
  cols = 2;
  isChecked;
  isCheckedName;
  isShow:boolean = false;
  assetsDetails;
  checkboxData = [
    {
      seacrhBy: "Search by National Id",
      id: 1
    },
    {
      seacrhBy: "Search by Passort Number",
      id: 2
    },
    {
      seacrhBy: "Search by Contact Number",
      id: 3
    }
  ];
  filteredCustomers: any;
  searchKeyword: string;
  resultantObject: any;
  constructor(private menuService: MenuService,
    public breakpointObserver: BreakpointObserver,
    private _customService: CustomServiceService,
    private router: Router) {
  }

  ngOnInit(): void {
  }

  onChange(e) {
    console.log(e.target.name)
    switch (e.target.name) {
      case "1":
        this.searchKeyword = "nationalId";
        console.log("Inside case 1")
        break;
      case "2":
        this.searchKeyword = "passportId";
        console.log("Inside case 2")

        break;
      case "3":
        this.searchKeyword = "phoneNumber";
        console.log("Inside case 3")

        break;
      default:
        this.searchKeyword = "";
    }
    this.isChecked = !this.isChecked;
    this.isCheckedName = e.target.name;
  }

  searchbyGuest(val){
    console.log("searchkey",val)
    this._customService.getCustomer(this.searchKeyword,val).subscribe(
      data=>{
        console.log("data search",data.nationalid,data.passportid)
        console.log("result data",data.roomList.data)
        this.resultantObject = data;
        this.assetsDetails = data.roomList.data;
        this.isShow = false;
        // this.resultantObject != null ? this.isShow = false : this.isShow = true;
      },
      error => {
        if (error.status == 404) {
          this.isShow = true;
        }
      
      }
    )
  }

}
