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

package com.wira.commons.client.security;

import java.util.ArrayList;

import com.wira.commons.client.util.ArrayUtil;
import com.wira.commons.shared.models.CurrentUserDto;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.PermissionPOJO;

public class CurrentUser {
	private Boolean loggedIn;
	private HTUser userDto;
	ArrayList<String> permissions = new ArrayList<String>();

	public CurrentUser() {
		loggedIn = false;
	}

	public void fromCurrentUserDto(CurrentUserDto currentUserDto) {
		setLoggedIn(currentUserDto.isLoggedIn());
		userDto = currentUserDto.getUser();
		
		for (PermissionPOJO p : userDto.getPermissions()) {
			permissions.add(p.getName().name());
		}
	}

	public void reset() {
		setLoggedIn(false);
		
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

	public boolean hasPermissions(String... requiredPermissions) {
		if(requiredPermissions!=null){
			return permissions.containsAll(ArrayUtil.asList(requiredPermissions));
		}

		return false;
	}

	public void clear() {
		permissions.clear();
		userDto=null;
		loggedIn=false;
	}

}
