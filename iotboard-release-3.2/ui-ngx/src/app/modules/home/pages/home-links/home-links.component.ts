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
  selector: 'tb-home-links',
  templateUrl: './home-links.component.html',
  styleUrls: ['./home-links.component.scss']
})
export class HomeLinksComponent implements OnInit {

  homeSections$ = this.menuService.homeSections();
  searchText = '';
  cols = 2;
  isChecked;
  isCheckedName;
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
  // checkboxData = [1, 2, 3];
  constructor(private menuService: MenuService,
    public breakpointObserver: BreakpointObserver,
    private _customService: CustomServiceService,
    private router: Router) {
  }

  ngOnInit() {
    this.updateColumnCount();
    this.breakpointObserver
      .observe([MediaBreakpoints.lg, MediaBreakpoints['gt-lg']])
      .subscribe((state: BreakpointState) => this.updateColumnCount());
  }

  private updateColumnCount() {
    this.cols = 2;
    if (this.breakpointObserver.isMatched(MediaBreakpoints.lg)) {
      this.cols = 3;
    }
    if (this.breakpointObserver.isMatched(MediaBreakpoints['gt-lg'])) {
      this.cols = 4;
    }
  }

  sectionColspan(section: HomeSection): number {
    if (this.breakpointObserver.isMatched(MediaBreakpoints['gt-sm'])) {
      let colspan = this.cols;
      if (section && section.places && section.places.length <= colspan) {
        colspan = section.places.length;
      }
      return colspan;
    } else {
      return 2;
    }
  }
  onChange(e) {
    console.log(e.target.name)
    switch (e.target.name) {
      case "1":
        this.searchKeyword = "nationalid";
        console.log("Inside case 1")
        break;
      case "2":
        this.searchKeyword = "passportid";
        console.log("Inside case 2")

        break;
      case "3":
        this.searchKeyword = "phone";
        console.log("Inside case 3")

        break;
      default:
        this.searchKeyword = "";
    }
    this.isChecked = !this.isChecked;
    this.isCheckedName = e.target.name;
  }
  searchbyGuest(val) {
    console.log("val", val)
    console.log("search keyword", this.searchKeyword)
    var self = this;
    // this._customService.getCustomers().subscribe(res => {
    //   // this.filteredCustomers = res.data[0]
    //   this.filteredCustomers = res.data.filter(function (data) {
    //     console.log("------------", self.searchKeyword)
    //     if (self.searchKeyword == 'nationalid'){
    //       return val == data.nationalid
    //     }else if(self.searchKeyword == 'passportid'){
    //       return val == data.passportid
    //     }else{
    //       return val == data.phone
    //     }
    //   })
    //   console.log("res", this.filteredCustomers)
    //   this.resultantObject = this.filteredCustomers[0]
    //   console.log("resultantObject", this.resultantObject)


    // })

  }

  showCustomer(){
    console.log("hi")
    this.router.navigateByUrl('/customers');
  }

  showRooms(){
    this.router.navigateByUrl('/assets')
  }

  showDevices(){
    this.router.navigateByUrl('/devices')
  }

  showDevProfile(){
    this.router.navigateByUrl('/deviceProfiles')
  }

  dashboard(){
    this.router.navigateByUrl('/room')
  }

  showCustomerList(){
    this.router.navigateByUrl('/guest')
  }
}
