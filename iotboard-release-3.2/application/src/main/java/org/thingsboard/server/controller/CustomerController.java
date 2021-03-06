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
package org.thingsboard.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.audit.ActionType;
import org.thingsboard.server.common.data.exception.ThingsboardErrorCode;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.CustomerId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.page.PageData;
import org.thingsboard.server.common.data.page.PageLink;
import org.thingsboard.server.queue.util.TbCoreComponent;
import org.thingsboard.server.service.security.permission.Operation;
import org.thingsboard.server.service.security.permission.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@TbCoreComponent
@RequestMapping("/api")
public class CustomerController extends BaseController {


    public static final String CUSTOMER_ID = "customerId";
    public static final String IS_PUBLIC = "isPublic";

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public Customer getCustomerById(@PathVariable(CUSTOMER_ID) String strCustomerId) throws ThingsboardException {
        checkParameter(CUSTOMER_ID, strCustomerId);
        try {
            CustomerId customerId = new CustomerId(toUUID(strCustomerId));
            return checkCustomerId(customerId, Operation.READ);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customer/{customerId}/shortInfo", method = RequestMethod.GET)
    @ResponseBody
    public JsonNode getShortCustomerInfoById(@PathVariable(CUSTOMER_ID) String strCustomerId) throws ThingsboardException {
        checkParameter(CUSTOMER_ID, strCustomerId);
        try {
            CustomerId customerId = new CustomerId(toUUID(strCustomerId));
            Customer customer = checkCustomerId(customerId, Operation.READ);
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode infoObject = objectMapper.createObjectNode();
            infoObject.put("title", customer.getTitle());
            infoObject.put(IS_PUBLIC, customer.isPublic());
            return infoObject;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customer/{customerId}/title", method = RequestMethod.GET, produces = "application/text")
    @ResponseBody
    public String getCustomerTitleById(@PathVariable(CUSTOMER_ID) String strCustomerId) throws ThingsboardException {
        checkParameter(CUSTOMER_ID, strCustomerId);
        try {
            CustomerId customerId = new CustomerId(toUUID(strCustomerId));
            Customer customer = checkCustomerId(customerId, Operation.READ);
            return customer.getTitle();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    @ResponseBody
    public Customer saveCustomer(@RequestBody Customer customer) throws ThingsboardException {

        try {
            customer.setTenantId(getCurrentUser().getTenantId());
            checkEntity(customer.getId(), customer, Resource.CUSTOMER);
            Customer savedCustomer = checkNotNull(customerService.saveCustomer(customer));
            logEntityAction(savedCustomer.getId(), savedCustomer,
                    savedCustomer.getId(),
                    customer.getId() == null ? ActionType.ADDED : ActionType.UPDATED, null);
            return savedCustomer;

        } catch (Exception e) {

            logEntityAction(emptyId(EntityType.CUSTOMER), customer,
                    null, customer.getId() == null ? ActionType.ADDED : ActionType.UPDATED, e);

            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCustomer(@PathVariable(CUSTOMER_ID) String strCustomerId) throws ThingsboardException {
        checkParameter(CUSTOMER_ID, strCustomerId);
        try {
            CustomerId customerId = new CustomerId(toUUID(strCustomerId));
            Customer customer = checkCustomerId(customerId, Operation.DELETE);
            customerService.deleteCustomer(getTenantId(), customerId);

            logEntityAction(customerId, customer,
                    customer.getId(),
                    ActionType.DELETED, null, strCustomerId);

        } catch (Exception e) {
            logEntityAction(emptyId(EntityType.CUSTOMER),
                    null,
                    null,
                    ActionType.DELETED, e, strCustomerId);
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customers", params = {"pageSize", "page"}, method = RequestMethod.GET)
    @ResponseBody
    public PageData<Customer> getCustomers(@RequestParam int pageSize,
                                           @RequestParam int page,
                                           @RequestParam(required = false) String textSearch,
                                           @RequestParam(required = false) String sortProperty,
                                           @RequestParam(required = false) String sortOrder) throws ThingsboardException {
        try {
            PageLink pageLink = createPageLink(pageSize, page, textSearch, sortProperty, sortOrder);
            TenantId tenantId = getCurrentUser().getTenantId();
            return checkNotNull(customerService.findCustomersByTenantId(tenantId, pageLink));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/customers", params = {"customerTitle"}, method = RequestMethod.GET)
    @ResponseBody
    public Customer getTenantCustomer(
            @RequestParam String customerTitle) throws ThingsboardException {
        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            return checkNotNull(customerService.findCustomerByTenantIdAndTitle(tenantId, customerTitle));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /*
    * API For Get Customer
    * Params - NationalID, PassportID, PhoneNumber
    * Response - Customer
    * */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customerByFilter", method = RequestMethod.GET)
    @ResponseBody
    public List<Optional<Customer>> getCustomerByFilter(@RequestParam(required = false) String nationalid,
                                                        @RequestParam(required = false) String passportId,
                                                        @RequestParam(required = false) String phoneNumber) throws ThingsboardException {
        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            List<Optional<Customer>> data = new ArrayList<>();

            if (nationalid != null && !(nationalid.trim().isEmpty())) {
                data.add(customerService.findCustomerByTenantIdAndNationalId(tenantId, nationalid));
                return data;
            }
            if (passportId != null && !(passportId.trim().isEmpty())) {
                data.add(customerService.findCustomerByTenantIdAndPassportId(tenantId, passportId));
                return data;
            }
            if (phoneNumber != null && !(phoneNumber.trim().isEmpty())) {
                data.add(customerService.findCustomerByTenantIdAndPhoneNumber(tenantId, phoneNumber));
                return data;
            }
            return data;
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    /*
    * API For Guest Lookup
    * Params - NationalID, PassportID, PhoneNumber
    * Response - Customer with assigned Room List
    * */
    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customer/guestLookup/", method = RequestMethod.GET)
    @ResponseBody
    public Customer guestLookup(@RequestParam(required = false) String nationalId,
                                          @RequestParam(required = false) String passportId,
                                          @RequestParam(required = false) String phoneNumber) throws ThingsboardException {
        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            Optional<Customer> customer  = Optional.empty();
            if (nationalId != null && !(nationalId.trim().isEmpty())) {
                customer = customerService.findCustomerByTenantIdAndNationalId(tenantId, nationalId);
            }
            if (passportId != null && !(passportId.trim().isEmpty())) {
                customer = customerService.findCustomerByTenantIdAndPassportId(tenantId, passportId);
            }
            if (phoneNumber != null && !(phoneNumber.trim().isEmpty())) {
                customer = customerService.findCustomerByTenantIdAndPhoneNumber(tenantId, phoneNumber);
            }

            PageLink pageLink = createPageLink(100, 0, null, null, null);
            Customer response ;
            if (customer.isPresent()){
                response = customer.get();
                response.setRoomList(checkNotNull(assetService.findAssetsByTenantIdAndCustomerId(tenantId, customer.get().getId(), pageLink)));
                return response;
            }
            else {
                throw new ThingsboardException("No Customer Found", ThingsboardErrorCode.ITEM_NOT_FOUND);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }
}
