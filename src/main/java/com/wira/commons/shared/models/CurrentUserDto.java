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

package com.wira.commons.shared.models;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CurrentUserDto implements Serializable,IsSerializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Boolean loggedIn;
    private HTUser user;

    public CurrentUserDto() {
    }

    public CurrentUserDto(
            Boolean loggedIn,
            HTUser user) {
        this.loggedIn = loggedIn;
        this.user = user;
    }

    public Boolean isLoggedIn() {
        return loggedIn;
    }

    public HTUser getUser() {
        return user;
    }

    @Override
    public String toString() {
        String s = " { CurrentUserDto ";
        s += "loggedIn=" + loggedIn + " ";
        s += "user=" + user + " ";
        s += " CurrentUserDto }";
        return s;
    }

	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void setUser(HTUser user) {
		this.user = user;
	}
}
