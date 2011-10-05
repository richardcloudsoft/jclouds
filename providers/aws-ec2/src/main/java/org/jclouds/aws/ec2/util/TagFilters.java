/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.aws.ec2.util;

import static com.google.common.base.Preconditions.*;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;

/**
 * @author grkvlt@apache.org
 */
public class TagFilters {
    public static enum FilterName {
        KEY,
        RESOURCE_ID,
        RESOURCE_TYPE,
        VALUE;

        public String value() {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
        }

        @Override
        public String toString() {
            return value();
        }

        public static FilterName fromValue(String name) {
            try {
                return valueOf(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, checkNotNull(name, "name")));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public static enum ResourceType {
        CUSTOMER_GATEWAY,
        DHCP_OPTIONS,
        IMAGE,
        INSTANCE,
        INTERNET_GATEWAY,
        NETWORK_ACL,
        RESERVED_INSTANCES,
        ROUTE_TABLE,
        SECURITY_GROUP,
        SNAPSHOT,
        SPOT_INSTANCES_REQUEST,
        SUBNET,
        VOLUME,
        VPC,
        VPN_CONNECTION,
        VPN_GATEWAY;

        public String value() {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
        }

        @Override
        public String toString() {
            return value();
        }

        public static ResourceType fromValue(String name) {
            try {
                return valueOf(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, checkNotNull(name, "name")));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    protected final ImmutableSetMultimap.Builder<FilterName, Object> map;

    protected TagFilters() {
        map = ImmutableSetMultimap.<FilterName, Object>builder();
    }

    public static TagFilters filters() {
        return new TagFilters();
    }

    public Multimap<FilterName, Object> build() {
        return map.build();
    }

    public TagFilters resourceId(String resourceId) {
        map.put(FilterName.RESOURCE_ID, resourceId);
        return this;
    }

    public TagFilters key(String key) {
        map.put(FilterName.KEY, key);
        return this;
    }

    public TagFilters keys(String...keys) {
        map.putAll(FilterName.KEY, ImmutableSet.<String>copyOf(keys));
        return this;
    }

    public TagFilters keys(Iterable<String> keys) {
        map.putAll(FilterName.KEY, ImmutableSet.<String>copyOf(keys));
        return this;
    }

    public TagFilters value(String value) {
        map.put(FilterName.VALUE, value);
        return this;
    }

    public TagFilters values(String...values) {
        map.putAll(FilterName.VALUE, ImmutableSet.<String>copyOf(values));
        return this;
    }

    public TagFilters values(Iterable<String> values) {
        map.putAll(FilterName.VALUE, ImmutableSet.<String>copyOf(values));
        return this;
    }

    public TagFilters keyContains(String key) {
        return key(String.format("*%s*", key));
    }

    public TagFilters valueContains(String value) {
        return value(String.format("*%s*", value));
    }

    public TagFilters resourceIdContains(String value) {
        return resourceId(String.format("*%s*", value));
    }

    public TagFilters keyStartsWith(String key) {
        return key(String.format("%s*", key));
    }

    public TagFilters valueStartsWith(String value) {
        return value(String.format("%s*", value));
    }

    public TagFilters resourceIdStartsWith(String value) {
        return resourceId(String.format("%s*", value));
    }

    public TagFilters keyEndsWith(String key) {
        return key(String.format("*%s", key));
    }

    public TagFilters valueEndsWith(String value) {
        return value(String.format("*%s", value));
    }

    public TagFilters resourceIdEndsWith(String value) {
        return resourceId(String.format("*%s", value));
    }

    public TagFilters keyValuePair(String key, String value) {
        return key(key).value(value);
    }

    public TagFilters keyValueSet(String key, Iterable<String> values) {
        return key(key).values(values);
    }

    public TagFilters keyValueSet(String key, String...values) {
        return key(key).values(values);
    }

    public TagFilters anyKey() {
        return key("*");
    }

    public TagFilters anyValue() {
        return value("*");
    }

    public TagFilters anyResourceId() {
        return resourceId("*");
    }

    public TagFilters anyResourceType() {
        map.put(FilterName.RESOURCE_TYPE, "*");
        return this;
    }

    public TagFilters customerGateway() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.CUSTOMER_GATEWAY);
        return this;
    }

    public TagFilters dhcpOptions() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.DHCP_OPTIONS);
        return this;
    }

    public TagFilters image() {
         map.put(FilterName.RESOURCE_TYPE, ResourceType.IMAGE);
        return this;
    }

    public TagFilters instance() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.INSTANCE);
        return this;
    }

    public TagFilters internetGateway() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.INTERNET_GATEWAY);
        return this;
    }

    public TagFilters networkAcl() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.NETWORK_ACL);
        return this;
    }

    public TagFilters reservedInstance() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.RESERVED_INSTANCES);
        return this;
    }

    public TagFilters routeTable() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.ROUTE_TABLE);
        return this;
    }

    public TagFilters securityGroup() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.SECURITY_GROUP);
        return this;
    }

    public TagFilters snapshot() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.SNAPSHOT);
        return this;
    }

    public TagFilters instancesRequest() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.SPOT_INSTANCES_REQUEST);
        return this;
    }

    public TagFilters subnet() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.SUBNET);
        return this;
    }

    public TagFilters volume() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.VOLUME);
        return this;
    }

    public TagFilters vpc() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.VPC);
        return this;
    }

    public TagFilters vpnConnection() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.VPN_CONNECTION);
        return this;
    }

    public TagFilters vpnGateway() {
        map.put(FilterName.RESOURCE_TYPE, ResourceType.VPN_GATEWAY);
        return this;
    }
}
