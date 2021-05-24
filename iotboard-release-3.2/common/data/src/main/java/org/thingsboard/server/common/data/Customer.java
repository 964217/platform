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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.TenantId;

import org.thingsboard.server.common.data.page.PageData;

import java.util.Calendar;
import java.util.TimeZone;

public class Customer extends ContactBased<CustomerId> implements HasTenantId {

    private static final long serialVersionUID = -1599722990298929275L;

    private String title;
    private TenantId tenantId;
    private long startTimeMs;
    private long endTimeMs;
    private String nationalid;
    private String passportid;
    private String phonenumber;
    private String status;
    private String hours;

    //  For CustomerRoom
    private PageData roomList;

    public PageData getRoomList() {
        return roomList;
    }

    public void setRoomList(PageData roomList) {
        this.roomList = roomList;
    }

    public Customer() {
        super();
    }

    public Customer(CustomerId id) {
        super(id);
    }

    public Customer(Customer customer) {
        super(customer);
        this.tenantId = customer.getTenantId();
        this.title = customer.getTitle();
        this.startTimeMs = customer.getStartTimeMs();
        this.endTimeMs = customer.getEndTimeMs();
        this.nationalid = customer.getNational_ID();
        this.passportid = customer.getPassport_ID();
        this.phonenumber = customer.getPhone_number();
        this.status = customer.getStatus();
        this.hours = customer.getHours();
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public void setStartTimeMs(long startTimeMs) {
        this.startTimeMs = startTimeMs;
    }

    public long getEndTimeMs() {
        return endTimeMs;
    }

    public void setEndTimeMs(long endTimeMs) {
        this.endTimeMs = endTimeMs;
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

    public String getPhone_number() {
        return phonenumber;
    }

    public void setPhone_number(String phone_number) {
        this.phonenumber = phone_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(long start, long end) {
        this.status = getRoomStatus(start, end);
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    @JsonIgnore
    public boolean isPublic() {
        if (getAdditionalInfo() != null && getAdditionalInfo().has("isPublic")) {
            return getAdditionalInfo().get("isPublic").asBoolean();
        }

        return false;
    }

    @JsonIgnore
    public ShortCustomerInfo toShortCustomerInfo() {
        return new ShortCustomerInfo(id, title, isPublic());
    }

    @Override
    @JsonProperty(access = Access.READ_ONLY)
    public String getName() {
        return title;
    }

    @Override
    public String getSearchText() {
        return getTitle();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (tenantId == null) {
            if (other.tenantId != null)
                return false;
        } else if (!tenantId.equals(other.tenantId))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Customer [title=");
        builder.append(title);
        builder.append(", tenantId=");
        builder.append(tenantId);
        builder.append(", additionalInfo=");
        builder.append(getAdditionalInfo());
        builder.append(", country=");
        builder.append(country);
        builder.append(", state=");
        builder.append(state);
        builder.append(", city=");
        builder.append(city);
        builder.append(", address=");
        builder.append(address);
        builder.append(", address2=");
        builder.append(address2);
        builder.append(", zip=");
        builder.append(zip);

        builder.append(", email=");
        builder.append(email);
        builder.append(", createdTime=");
        builder.append(createdTime);
        builder.append(", id=");
        builder.append(id);

        builder.append(", startTimeMs=");
        builder.append(startTimeMs);
        builder.append(",endTimeMs=");
        builder.append(endTimeMs);
        builder.append(", nationalid=");
        builder.append(nationalid);
        builder.append(", passportid=");
        builder.append(passportid);
        builder.append(", phoneNumber=");
        builder.append(phonenumber);
        builder.append(", status=");
        builder.append(status);
        builder.append(", hours=");
        builder.append(hours);
        builder.append("]");
        return builder.toString();
    }

    // Get RoomStatus based on start and end time
    private static String getRoomStatus(long start, long end) {
        if (start == 0) {
            return "false";
        }
        if (end == 0) {
            return "false";
        }

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long current = cal.getTimeInMillis();
        return String.valueOf(current >= start && current <= end);
    }
}
