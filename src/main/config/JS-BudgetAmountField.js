
var totalClaim= 0.0;
var selectExpense = $doc.getElementById('selectExpense');

//Taxi
var taxiRate = $doc.getElementById('rate');
taxiRate.addEventListener('change', highlightBudget);

var kms = $doc.getElementById('kms');
kms.addEventListener('change', highlightBudget);

//Meals
var mealRate = $doc.getElementById('amountPerPerson');
mealRate.addEventListener('change', highlightBudget);

var noOfStaff = $doc.getElementById('noOfStaff');
noOfStaff.addEventListener('change', highlightBudget);

//Per Diem
var noOfDays = $doc.getElementById('noOfDays');
noOfDays.addEventListener('change', highlightBudget);

var amountPerDay = $doc.getElementById('amountPerDay');
amountPerDay.addEventListener('change', highlightBudget);


highlightBudget();

function highlightBudget() {
	var budgetField =$doc.getElementById('budgetAmount_Field');
	var budgetView =$doc.getElementById('budgetAmount_View');
	var budgetInput =$doc.getElementById('budgetAmount');
	var budgetAmount =0.0;
	if(budgetInput.value!=null){
		var numberValue = budgetInput.value.replace(',','');
		if(numberValue.indexOf('(')!=-1){
			//-ve numbers
			numberValue=numberValue.replace('(','');//-ve numbers in gwt number input
			numberValue=numberValue.replace(')','');
			numberValue='-'+numberValue;
			//$wnd.alert('Value ='+numberValue);
		}
		budgetAmount=parseFloat(numberValue);
		
	}
	setTotalClaim();

	//$wnd.alert('BudgetAmount='+budgetAmount+' ; totalClaim='+totalClaim);
	if(budgetAmount<0 || ((budgetAmount==0 || budgetAmount<totalClaim) && selectExpense.value!='')){
		budgetField.style.background='orange';
		
		var spnInsBudget = $doc.getElementById('insufficientBudgetStatement');
		if(spnInsBudget==undefined || spnInsBudget==null){
			budgetView.innerHTML=budgetView.innerHTML+"<span id='insufficientBudgetStatement'>&nbsp;&nbsp;Insufficient Budget</span>";
			budgetField.title='You have insufficient budget for this request';
		}
	}else{
		budgetField.style.background='white';
		budgetField.title='Available Budget';
	}

}

function setTotalClaim(){
	if (selectExpense.value == 'Meals') {
		totalClaim=parseFloat($doc.getElementById('totalMealsClaim').value.replace(',',''));
	} else if (selectExpense.value == 'Per diem') {
		totalClaim=parseFloat($doc.getElementById('totalPerDiemClaim').value.replace(',',''));
	} else if (selectExpense.value == 'Taxi') {
		totalClaim=parseFloat($doc.getElementById('totalTaxiClaim').value.replace(',',''));
	}
}