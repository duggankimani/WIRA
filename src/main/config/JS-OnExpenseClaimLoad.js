/**
 * 
 */

var selectExpense = $doc.getElementById('selectExpense');
showFieldsForExpenseType(selectExpense.value);

selectExpense.addEventListener('change', function() {
	var newValue = selectExpense.value;
	selectExpense.defaultValue=newValue;

	clear('noOfStaff');
	clear('amountPerPerson');
	clear('noOfDays');
	clear('amountPerDay');
	clear('route');
	clear('mileage');
	clear('fromOrigin');
	clear('toDestination');
	clear('kms');
	clear('rate');
	clear('totalPerDiemClaim');
	clear('totalTaxiClaim');
	clear('totalMealsClaim');
	
	reset('noOfStaff');
	reset('amountPerPerson');
	reset('noOfDays');
	reset('amountPerDay');
	reset('kms');
	reset('rate');
	reset('totalPerDiemClaim');
	reset('totalTaxiClaim');
	reset('totalMealsClaim');
	showFieldsForExpenseType(newValue);
},false);
		
function show(fieldName, show) {
	var field= $doc.getElementById(fieldName);
	if(field!=null){
		if (show == true) {
			field.style.display = 'block';
		} else {
			field.style.display = 'none';
		}
	}else{
		$wnd.alert(fieldName+' cannot be shown - does not exist!');
	}
	
}

function showFieldsForExpenseType(fieldName){
	show('noOfStaff_Field', false);
	show('amountPerPerson_Field', false);
	show('noOfDays_Field', false);
	show('amountPerDay_Field', false);
	show('route_Field', false);
	show('mileage_Field', false);
	show('fromOrigin_Field', false);
	show('toDestination_Field', false);
	show('kms_Field', false);
	show('rate_Field', false);
	show('totalMealsClaim_Field',false);
	show('totalPerDiemClaim_Field',false);
	show('totalTaxiClaim_Field',false);
	
	
	if (selectExpense.value == 'Meals') {
		show('totalMealsClaim_Field',true);
		show('noOfStaff_Field', true);
		show('amountPerPerson_Field', true);
	} else if (selectExpense.value == 'Per diem') {
		show('totalPerDiemClaim_Field',true);
		show('noOfDays_Field', true);
		show('amountPerDay_Field', true);
	} else if (selectExpense.value == 'Taxi') {
		show('totalTaxiClaim_Field',true);
		show('route_Field', true);
		show('mileage_Field', true);
		show('fromOrigin_Field', true);
		show('toDestination_Field', true);
		show('kms_Field', true);
		show('rate_Field', true);
	}
}

function reset(componentName){
	$wnd.setFieldValue(componentName, '0');
}

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