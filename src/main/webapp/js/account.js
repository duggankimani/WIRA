//#/activateacc/Bqs9crpBS8P18l2L/default
var action;
var activationId;
var userId;
var activity;
$(document).ready(function(){
	bind();
	parseUrl();	
	switch(action){
		case "activateacc":
			activationRequest();
			break;
		case "forgot":
			forgotPassword();
			break;
	}
	
});

function bind(){
	
	var password = getElementByName('password');
	var passwordConfirm = getElementByName('passwordConfirm');
	
	$(password).change(function(){
		checkPassword(password, passwordConfirm);
	});
	$(passwordConfirm).change(function(){
		checkPassword(password,passwordConfirm);
	});
}

function getElementByName(name){
	var el = $('#loadingbox').find('input[name=\''+name+'\']').get(0);
	return el;
}

function getValue(name){
	var element = getElementByName(name);
	return $(element).val();
}

function forgotPassword(){
	$('#elHeading').text("Reset Password");
	
	showFormInputs(false);
	
	show("email",true);
	show("submit",true);
	
	var email=getElementByName("email");
	$(email).prop('disabled',false);
	var label = $(email).prev();
	showEl(label, true);
}

function show(name, isShow){
	var el=getElementByName(name);
	showEl(el,isShow);
}

function showEl(element, isShow){
	if(isShow){
		$(element).show();
	}else{
		$(element).hide();
	}
}

function checkPassword(password, confirm){
	$('#issues').hide();
	
	var passVal = $(password).val();
	var confirmVal = $(confirm).val();
	if(passVal==null || confirmVal==null || passVal=='' || confirmVal==''){
		return;
	}

	if(passVal!=confirmVal){
		$('#issues').show();
		$('#issues').html('Passwords do not match');
	}
}

function parseUrl(){
	var str = window.location+''; 
	var startIdx = str.indexOf("#")+2; //Ignore '#/'
	var endIdx = str.length;
	var params = str.substring(startIdx,endIdx);

	var tokens = params.split("/");
	tokens.forEach(function(token, idx){
		switch(idx){
			case 0:
				action=token;
				break;
			case 1:
				activationId=token;
				break;
			case 2:
				userId=token;
				break;
			case 3:
				activity=token;
				break;
		}
	});

}

function showFormInputs(isShow){
	
	$("#mainContent").find("form :input, label").each(function(){
		showEl(this,isShow);
	});
}

function activationRequest(){
	showFormInputs(true);
	
	var email=getElementByName("email");
	$(email).prop('disabled',true);
	
	if(activity=='reset'){
		$('#elHeading').text("Reset Password");
		var firstName = getElementByName('firstname');
		$(firstName).hide();
		$(firstName).prev().hide();
		
		var lastName = getElementByName('lastname');
		$(lastName).hide();
		$(lastName).prev().hide();
		
	}else{
		$('elHeading').html("Activate Account");
	}
	
	
	
	if(action==null || action!='activateacc' || activationId==null ||
			userId==null || activity==null || (activity!='reset' && activity!='default')){
		window.location='login.html';
		return;
	}
	
	
	var str = "action=GetUser&aid="+activationId+"&uid="+userId;
	//load user details
	$.ajax({
		type:"POST",
		url:"accounts",
		data:str
	})
	.done(function(data) {
		$.each(data,function(key,val){
			var input=$('#loadingbox').find('input[name=\''+key.toLowerCase()+'\']').get(0);
			if(input){
				$(input).val(val);
			}
		});
	})
	.fail(function() {
		$('#issues').show();
	})
	.always(function() {
		//$('#submit').show();
	});
}

function isValid(){
	var isValid = true;
	
	var error='';
	var email = getValue('email');
	if(isNullOrEmpty(email)){
		error=error+'<div>\'Email\' is required</div>';
		isValid=false;
	}
	
	if(action=='activateacc'){
		var firstname = getValue('firstname');
		if(isNullOrEmpty(email)){
			error=error+'<div>\'First Name\' is required</div>';
			isValid=false;
		}
		
		var lastname = getValue('lastname');
		if(isNullOrEmpty(email)){
			error=error+'<div>\'Last Name\' is required</div>';
			isValid=false;
		}
	
		var password = getValue('password');
		if(isNullOrEmpty(password)){
			error=error+'<div>\'Password\' is required</div>';
			isValid=false;
		}
		
		var passwordConfirm = getValue('passwordConfirm');
		if(isNullOrEmpty(passwordConfirm)){
			error=error+'<div>\'Confirm Password\' is required</div>';
			isValid=false;
		}
		
		if(isValid && passwordConfirm!=password){
			isValid = false;
			error=error+'<div>Passwords do not match</div>';
		}
	}
	
	if(!isValid){
		$('#issues').html(error);
	}
	
	return isValid;
}

function isNullOrEmpty(value){
	if(value==null || value==''){
		return true;
	}
	
	return false;
}

function doActivateAccount() {
	switch(action){
		case "activateacc":
			activate();
			break;
		case "forgot":
			sendResetEmail();
			break;
	}
}

function sendResetEmail(){
	var form = $('#mainContent form');
	// 'this' refers to the current submitted form
	var str = form.serialize()+"&action=sendmail";
	
	if(!isValid()){
		$('#issues').html("<div>'Email' is required</div>");
		$('#issues').show();
		return;
	}
	
	$('#issues').hide();
	$('#submit').hide();
	
	$.ajax({
		type:"POST",
		url:"accounts",
		data:str
	})
	.done(function() {
		alert('Account reset instructions have been sent to \''+getValue('email')+'.\'')
		window.location='login.html';
	})
	.fail(function() {
		$('#issues').html('Account not found');
		$('#issues').show();
	})
	.always(function() {
		$('#submit').show();
	});
}

function activate(){
	var form = $('#mainContent form');
	// 'this' refers to the current submitted form
	var str = form.serialize()+"&action=UpdatePassword&aid="+activationId+"&uid="+userId;;
	
	if(!isValid()){
		$('#issues').show();
		return;
	}
	
	$('#issues').hide();
	$('#submit').hide();
	
	$.ajax({
		type:"POST",
		url:"accounts",
		data:str
	})
	.done(function() {
		window.location='login.html';
	})
	.fail(function() {
		$('#issues').html('Password updated failed, please try again later.');
		$('#issues').show();
	})
	.always(function() {
		$('#submit').show();
	});
}