var Google_Client_Status='';

$(document).ready(function(){
		bindEvents();
	
		$.ajax({
			type:"GET",
			url:"googleservlet",
			dataType: 'json'
		})
		.done(function(data) {
			Google_Client_Status = $(data)[0].GOOGLE_CLIENT_STATUS;
			if(Google_Client_Status!='ACTIVE'){
				$('#Google_Login').prop("disabled",true);
			}
			
		})
		.fail(function() {
			$('#Google_Login').prop("disabled",false);
		})
		.always(function() {
			
		});
});

function bindEvents(){
	$('#Google_Login').click(google_signin());
}

function loginWithCookie(){
	var str = "loginmethod=VIA_COOKIE"
	$.ajax({
		type:"POST",
		url:"auth",
		data:str
	})
	.done(function() {
		window.location='index.html';
	})
	.fail(function() {
	})
	.always(function() {
	});	
}

function doFormSubmit() {
	var form = $('#status > form');
	// 'this' refers to the current submitted form
	var str = form.serialize();
	
	$('#issues').hide();
	$('#submit').prop("disabled",true);
	
	$.ajax({
		type:"POST",
		url:"auth",
		data:str
	})
	.done(function() {
		window.location='index.html';
	})
	.fail(function() {
		$('#issues').show();
	})
	.always(function() {
		$('#submit').prop("disabled",false);
	});

};

function google_signin(){
	if(Google_Client_Status!='ACTIVE'){
		return;
	}

	$('#googleloginform').submit();
}
