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

import { Component, Input, DoCheck } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { COUNTRIES } from '@home/models/contact.models';
import moment from 'moment';
import { Observable } from 'rxjs';
@Component({
  selector: 'tb-contact',
  templateUrl: './contact.component.html'
})
export class ContactComponent implements DoCheck{
  maxStartTimeMs: Observable<number | null>;
  minEndTimeMs: Observable<number | null>;
  startTimeMs: any ;
  endTimeMs:any;
  @Input()
  parentForm: FormGroup;

  @Input() isEdit: boolean;

  countries = COUNTRIES;
  currentdate:boolean;
  currentdatevalue:boolean;
  status:any;
  date;
  startDate;
  endDate;
  hours;
  constructor(){
  }
  ngDoCheck(){
    console.log("startTimeMs====", this.startTimeMs)
    const usersSelected = this.getDateTimeFromTimestamp(this.startTimeMs);
    console.log("usersSelected", usersSelected);


    var ts = Math.round((new Date()).getTime());
    const currentDate = this.getDateTimeFromTimestamp(ts);

    console.log("ts", ts)
    console.log("currentDate", currentDate)
    this.status = currentDate < usersSelected ? this.currentdatevalue = false : this.currentdatevalue = true;
    console.log("status",this.status)
    console.log("currentdatevalue",this.currentdatevalue)

    var date1:any = this.getDateTimeFromTimestamp(this.startTimeMs);
    var date2:any = this.getDateTimeFromTimestamp(this.endTimeMs);
    console.log("date 1 and date2",date1,date2)

    var sdate:any = new Date(date1);
    var edate:any = new Date(date2);

    let millisec = edate - sdate;
    let totalhrs = Math.round(millisec / 36e5);
    let diffMins = Math.round(((millisec % 86400000) % 3600000) / 60000);

    this.hours = totalhrs + ":" + diffMins;
    console.log("thr",this.hours)
    console.log("difference in min",diffMins)

    // let milliseconds = date2 - date1;
    // console.log("milisec",milliseconds)
    // this.hours = milliseconds / 36e5;
    // let diffMins = Math.round(((milliseconds % 86400000) % 3600000) / 60000);
    
    console.log("total hrs diff",this.hours)


  }
  getDateTimeFromTimestamp(unixTimeStamp) {
    let date = new Date(unixTimeStamp);
    // return ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear() + 'T' + ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
    return date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2) + 'T' + ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
}
  selectedDate(val){
    console.log("selected date",val);
    var dateNow = new Date().toISOString();
    let date = moment.utc(dateNow).local();
    let newDate = date.format('YYYY-MM-DDTHH:mm');
    console.log("current date",newDate)
    this.status = newDate < val ? this.currentdate = false : this.currentdate = true;
    console.log("status",this.status)
  }

}
