


function loadXMLDoc() {
    var xmlhttp = new XMLHttpRequest();
	xmlhttp.timeout = 1700;
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE) {   // XMLHttpRequest.DONE == 4
           if (xmlhttp.status == 200) {
               
               var diven = document.getElementById("myDiv");
               var x = diven.getAttribute("testcounter");
			   incrementTestCounter(diven);
               
               diven.innerHTML = xmlhttp.responseText;
               
               try {
                var data = JSON.parse(xmlhttp.responseText);
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
    
    xmlhttp.open("GET", "/greeting", true);
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

