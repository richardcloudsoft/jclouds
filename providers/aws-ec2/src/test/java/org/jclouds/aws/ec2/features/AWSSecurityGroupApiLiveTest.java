/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.aws.ec2.features;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Set;

import org.jclouds.ec2.domain.SecurityGroup;
import org.jclouds.ec2.features.SecurityGroupApiLiveTest;
import org.jclouds.ec2.util.IpPermissions;
import org.jclouds.net.domain.IpPermission;
import org.jclouds.net.domain.IpProtocol;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMultimap;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", singleThreaded = true)
public class AWSSecurityGroupApiLiveTest extends SecurityGroupApiLiveTest {
   public AWSSecurityGroupApiLiveTest() {
      provider = "aws-ec2";
   }

   @Test
   void testAuthorizeSecurityGroupIngressIpPermission() throws InterruptedException {
      final String group1Name = PREFIX + "ingress11";
      String group2Name = PREFIX + "ingress12";
      cleanupAndSleep(group2Name);
      cleanupAndSleep(group1Name);
      try {
         String group1Id = AWSSecurityGroupApi.class.cast(client).createSecurityGroupInRegionAndReturnId(null,
               group1Name, group1Name);
         String group2Id = AWSSecurityGroupApi.class.cast(client).createSecurityGroupInRegionAndReturnId(null,
               group2Name, group2Name);
         Thread.sleep(100);// eventual consistent
         ensureGroupsExist(group1Name, group2Name);
         AWSSecurityGroupApi.class.cast(client).authorizeSecurityGroupIngressInRegion(null, group1Id,
               IpPermissions.permit(IpProtocol.TCP).port(80));
         assertEventually(new GroupHasPermission(client, group1Name, new TCPPort80AllIPs()));
         Set<SecurityGroup> oneResult = client.describeSecurityGroupsInRegion(null, group1Name);
         assertNotNull(oneResult);
         assertEquals(oneResult.size(), 1);
         final SecurityGroup group = oneResult.iterator().next();
         assertEquals(group.getName(), group1Name);
         IpPermissions group2CanHttpGroup1 = IpPermissions.permit(IpProtocol.TCP).port(80)
               .originatingFromSecurityGroupId(group1Id);
         AWSSecurityGroupApi.class.cast(client).authorizeSecurityGroupIngressInRegion(null, group2Id,
               group2CanHttpGroup1);
         assertEventually(new GroupHasPermission(client, group2Name, new Predicate<IpPermission>() {
            @Override
            public boolean apply(IpPermission arg0) {
               return arg0.getTenantIdGroupNamePairs().equals(ImmutableMultimap.of(group.getOwnerId(), group1Name))
                     && arg0.getFromPort() == 80 && arg0.getToPort() == 80 && arg0.getIpProtocol() == IpProtocol.TCP;
            }
         }));

         AWSSecurityGroupApi.class.cast(client).revokeSecurityGroupIngressInRegion(null, group2Id,
               group2CanHttpGroup1);
         assertEventually(new GroupHasNoPermissions(client, group2Name));
      } finally {
         client.deleteSecurityGroupInRegion(null, group2Name);
         client.deleteSecurityGroupInRegion(null, group1Name);
      }
   }
}
