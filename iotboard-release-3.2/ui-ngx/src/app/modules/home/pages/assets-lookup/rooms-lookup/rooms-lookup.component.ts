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
import { AssetService } from '@app/core/http/asset.service';
import {Router} from '@angular/router';

@Component({
  selector: 'tb-rooms-lookup',
  templateUrl: './rooms-lookup.component.html',
  styleUrls: ['./rooms-lookup.component.scss']
})
export class RoomsLookupComponent implements OnInit {
  searchText = '';
  roomDetails;
  isFound:boolean = false;
  avaliableRoom;
  totalRoom;
  assignedRoom;
  constructor(private menuService: MenuService,
    public breakpointObserver: BreakpointObserver,
    private _customService: CustomServiceService,
    private _assetService :AssetService,
    private router: Router) {
  }

  ngOnInit(): void {
    this._customService.getTotalCount().subscribe(
      data=>{
        console.log("total rooms",data)
        this.avaliableRoom = data.available;
        this.totalRoom = data.total;
        this.assignedRoom = data.assigned;
      }
    )
  }
  searchbyRoom(val){
    var pageSize = 100;
    var page = 0;
    this._customService.getRoomDetails(val,pageSize,page).subscribe(
      data=>{
        console.log("total rooms",data.data)
        this.roomDetails = data.data;
        this.roomDetails.length != 0  ? this.isFound = false : this.isFound = true;
      }
    )
  }

}
