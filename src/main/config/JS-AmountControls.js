/**
 * 
 */

var taxiRate = $doc.getElementById('rate');
taxiRate.addEventListener('change', function() {
	if(taxiRate.value!=null && parseFloat(taxiRate.value.replace(',',''))>150){
		$wnd.alert("Maximum rate for taxi is 150ksh/Km");
		taxiRate.value=0;
		taxiRate.defaultValue=0;
		clear('totalTaxiClaim_View');
		clear('totalTaxiClaim');
		$wnd.setFieldValue('totalTaxiClaim', '0');
		$wnd.setFieldValue('rate', '0');
	}
},false);

var mealRate = $doc.getElementById('amountPerPerson');
mealRate.addEventListener('change', function() {
	if(mealRate.value!=null && parseFloat(mealRate.value.replace(',',''))>2000){
		$wnd.alert("Maximum amount refundable for client entertainment is Ksh 2,000");
		mealRate.value=0;
		mealRate.defaultValue=0;
		clear('totalMealsClaim_View');
		clear('totalMealsClaim');
		$wnd.setFieldValue('totalMealsClaim', '0');
		$wnd.setFieldValue('amountPerPerson', '0');
	}
},false);


function clear(componentName){
	if($doc.getElementById(componentName)!=null){
		$doc.getElementById(componentName).value = '';
		$doc.getElementById(componentName).defaultValue = '';
	}else{
		$wnd.alert(componentName+' Does not exist!');
	}
	
	if($doc.getElementById(componentName+"_View")!=null){
		$doc.getElementById(componentName+"_View").innerHTML = '';
	}
}