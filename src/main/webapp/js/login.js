var count = 0;
$(document).ready(function(){

	var authCookie = Cookies.get('AUTHCOOKIEID');
	if(authCookie && count<1){
		count=count+1;
		loginWithCookie();
	}
});

function loginWithCookie(){
	var str = "loginmethod=VIA_COOKIE"
	$.ajax({
		type:"POST",
		url:"auth",
		data:str
	})
	.done(function() {
		window.location='GWTHT.html';
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
	$('#submit').hide();
	
	$.ajax({
		type:"POST",
		url:"auth",
		data:str
	})
	.done(function() {
		window.location='GWTHT.html';
	})
	.fail(function() {
		$('#issues').show();
	})
	.always(function() {
		$('#submit').show();
	});

};
