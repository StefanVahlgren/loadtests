

function greet() {
    var xmlhttp = new XMLHttpRequest();
    
	xmlhttp.timeout = 1700;
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE) {   // XMLHttpRequest.DONE == 4
           if (xmlhttp.status == 200) {
               
	               var diven = document.getElementById("username");
	                var data = JSON.parse(xmlhttp.responseText);
	                diven.innerHTML = data.name;
	                
	                var privateData = document.getElementById("privatedata");
	                privateData.innerHTML = data.content;
               
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
    
    var namn = getUrlParam('namn','Empty');
    xmlhttp.open("GET", "/greeting?name=" + namn, true);
    xmlhttp.send();
}

function getUrlParam(parameter, defaultvalue){
    var urlparameter = defaultvalue;
    if(window.location.href.indexOf(parameter) > -1){
        urlparameter = getUrlVars()[parameter];
        }
    return urlparameter;
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
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

