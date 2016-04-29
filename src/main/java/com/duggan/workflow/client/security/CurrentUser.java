/*
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.duggan.workflow.client.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.duggan.workflow.shared.model.CurrentUserDto;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.PermissionPOJO;

public class CurrentUser {
	private Boolean loggedIn;
	private HTUser userDto;

	public CurrentUser() {
		loggedIn = false;
	}

	public void fromCurrentUserDto(CurrentUserDto currentUserDto) {
		setLoggedIn(currentUserDto.isLoggedIn());
		setUser(currentUserDto.getUser());
	}

	public void reset() {
		setLoggedIn(false);
		setUser(null);
	}

	public Boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public HTUser getUser() {
		return userDto;
	}

	public void setUser(HTUser userDto) {
		this.userDto = userDto;
	}

	public boolean hasPermissions(String... requiredPermissions) {

		List<String> permissions = new ArrayList<String>();
		if (userDto != null) {
			if(userDto.getPermissions()!=null)
			for (PermissionPOJO p : userDto.getPermissions()) {
				permissions.add(p.getName().name());
			}
		}
		
		if(requiredPermissions!=null){
			return permissions.containsAll(Arrays.asList(requiredPermissions));
		}
			

		return false;
	}

}
