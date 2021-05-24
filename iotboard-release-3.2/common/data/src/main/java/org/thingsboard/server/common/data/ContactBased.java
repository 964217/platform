/**
 * Copyright © 2016-2021 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.common.data;

import lombok.EqualsAndHashCode;
import org.thingsboard.server.common.data.id.UUIDBased;

@EqualsAndHashCode(callSuper = true)
public abstract class ContactBased<I extends UUIDBased> extends SearchTextBasedWithAdditionalInfo<I> implements HasName {
    
    private static final long serialVersionUID = 5047448057830660988L;
    
    protected String country;
    protected String state;
    protected String city;
    protected String address;
    protected String address2;
    protected String zip;
    protected String phone;
   protected String startTimeMs;
   protected String endTimeMs;
    protected String email;
    protected String nationalid;
    protected String passportid;
    
    public ContactBased() {
        super();
    }

    public ContactBased(I id) {
        super(id);
    }
    
    public ContactBased(ContactBased<I> contact) {
        super(contact);
        this.country = contact.getCountry();
        this.state = contact.getState();
        this.city = contact.getCity();
        this.address = contact.getAddress();
        this.address2 = contact.getAddress2();
        this.zip = contact.getZip();
        this.startTimeMs=contact.getstartTimeMs();
        this.endTimeMs=contact.getendTimeMs();
        this.email = contact.getEmail();
        this.nationalid=contact.getNational_ID();
        this.passportid=contact.getPassport_ID();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

   public String getstartTimeMs()
   {
       return startTimeMs;
   }

   public void setstartTimeMs( String startTimeMs)
   {
       this.startTimeMs=startTimeMs;
   }
    public String getendTimeMs()
   {
       return endTimeMs;
   }

   public void setendTimeMs( String endTimeMs)
   {
       this.endTimeMs=endTimeMs;
   }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
     public String getNational_ID() {
        return nationalid;
    }

    public void setNational_ID(String nationalid) {
        this.nationalid = nationalid;
    }

    public String getPassport_ID() {
        return passportid;
    }

    public void setPassport_ID(String passportid) {
        this.passportid = passportid;
    }

}
