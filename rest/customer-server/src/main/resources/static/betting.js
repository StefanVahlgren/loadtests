
function runBettingPage(){

 checkStatus();
loadXMLDoc();
    setInterval(function(){ loadXMLDoc(); }, 3000);

}
function checkStatus(){

    var xmlhttp = new XMLHttpRequest();
    
    xmlhttp.timeout = 1700;
    	
    xmlhttp.onreadystatechange = function() {
	        if (xmlhttp.readyState == XMLHttpRequest.DONE) {   // XMLHttpRequest.DONE == 4
           if (xmlhttp.status == 200) {
          	 var diven = document.getElementById("status");
          	 
          	 
          	 diven.setAttribute("status", "updated");
          	 diven.setAttribute("style", "background-color:lime; width:5em");
           diven.innerHTML = "Status: Up";
           }
           }
	
	}
		
	xmlhttp.ontimeout = function() {
		var diven = document.getElementById("myDiv");
		diven.innerHTML = "timeout";
		incrementTestCounter(diven);
	}
	
    xmlhttp.open("GET", "/api/status", true);
    xmlhttp.send();
}


function loadXMLDoc() {
    var xmlhttp = new XMLHttpRequest();
	xmlhttp.timeout = 1700;
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE) {   // XMLHttpRequest.DONE == 4
           if (xmlhttp.status == 200) {
               

	               var diven = document.getElementById("myDiv");
               
			   incrementTestCounter(diven);
               
               try {
                var data = JSON.parse(xmlhttp.responseText);
                var theList = data.oddsList;

				var theLength = theList.length;
               for(var i =0; i<theLength; i++){
    	           var match = document.getElementById("match" + i);
    	           var odds = theList[i];
    	           if (match === null){
		               match= document.createElement("div");
		               
		               var lika = document.createElement("div");
		               lika.setAttribute("style", "  display: inline-block; width: 10em");
		               lika.innerHTML = "X"; 
		               match.setAttribute("style", "margin-top:1.5em; border: 0.1em solid black;");
		               match.setAttribute("id", "match" + i);
						
						diven.appendChild(match);
						

		               var hemma = document.createElement("div");
						hemma.setAttribute("style", "  display: inline-block; width: 10em");
		             
		               var borta = document.createElement("div");
		               				borta.setAttribute("style", "  display: inline-block;");
		               var oddsElement = document.createElement("div");
						oddsElement.setAttribute("id", "odds"+i);
						
						match.appendChild(hemma);
						match.appendChild(lika);
						match.appendChild(borta);
						match.appendChild(oddsElement);

    	           		hemma.innerHTML = odds.hemma;
    	           		borta.innerHTML = odds.borta;
						
						var ett = document.createElement("div");
						ett.innerHTML = odds.ett;
						var kryss = document.createElement("div");
						kryss.innerHTML = odds.kryss;
						var tva = document.createElement("div");
						tva.innerHTML = odds.tva;
						oddsElement.appendChild(ett);
						ett.setAttribute("style", "  display: inline-block; width: 10em");
						ett.setAttribute("class", ".ett");
						oddsElement.appendChild(kryss);
						kryss.setAttribute("style", "  display: inline-block;width: 10em");
						kryss.setAttribute("class", ".kryss");
						oddsElement.appendChild(tva);	
						tva.setAttribute("style", "  display: inline-block;width: 5em");
						tva.setAttribute("class", ".tva");												
    	           		//oddsElement.innerHTML = "1:" + odds.ett + "   X:" + odds.kryss + "  2:" + odds.tva; 
    	           		
    	           }else {
    	           var ett = match.getElementsByClassName(".ett")[0];
    	           ett.innerHTML = odds.ett;
    	           var kryss = match.getElementsByClassName(".kryss")[0];
    	           kryss.innerHTML = odds.kryss;
    	           var tva = match.getElementsByClassName(".tva")[0];
    	           tva.innerHTML = odds.tva;
    	           	// var oddsDiv = document.getElementById("odds" + i);
    	           	//oddsDiv.innerHTML = "1:" + odds.ett + "   X:" + odds.kryss + "  2:" + odds.tva; 
    	           }
               }


            } catch(err) {
                console.log(err.message + " in " + xmlhttp.responseText);
                return;
            }
            
               
           }
           else if (xmlhttp.status == 400) {
              alert('There was an error 400');
           }
        }
    };
	
	xmlhttp.ontimeout = function() {
		var diven = document.getElementById("myDiv");
		diven.innerHTML = "timeout";
		incrementTestCounter(diven);
	}
    
    xmlhttp.open("GET", "/odds", true);
    xmlhttp.send();
}

function incrementTestCounter(diven) {
		var x = diven.getAttribute("testcounter");
        var newx = Number(x) + 1;
        diven.setAttribute("testcounter", newx); 
}

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

