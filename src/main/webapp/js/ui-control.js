// refresh a part of the HTML specified by the given ID,
// by using the contents fetched from the given URL.
/*
 * The MIT License
 * 
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Kohsuke Kawaguchi,
 * Daniel Dyer, Yahoo! Inc., Alan Harder, InfraDNA, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
//
//
// JavaScript for customized controls in DTI
//

// multi-checkbox
function updateCheckbox(checkBox,url,config) {
	config = config || {};
	config = object(config);
	var originalOnSuccess = config.onSuccess;
	config.onSuccess = function(rsp) {
		var opts = eval('('+rsp.responseText+')').values;
		var c = $(checkBox);

		var checkboxpart = c.getElementsBySelector("DIV.checkboxpart")[0];
		var selectpart = c.getElementsBySelector("SELECT.selectpart")[0];
		checkboxpart.innerHTML = "";
		selectpart.innerHTML = "";

		var ps = c.getElementsBySelector("P.template");
		var selected1 = {};
		for (var i = 0;i!=ps.length;i++) {
			selected1[ps[i].innerHTML] = 1;
		}

		for (var i = 0;i!=opts.length;i++) {
			var input = document.createElement("input");
			input.setAttribute("type", "checkbox");  
			input.setAttribute("seq", opts[i].value);
			input.setAttribute("class", "checkboxpartitem");
			if (opts[i].checked===true) {
				input.setAttribute("checked", "checked");
			}
			
			if (selected1[opts[i].value] === 1) {
				input.setAttribute("checked", "checked");
			}

			var label = document.createElement("label");
			label.setAttribute("class" ,"attach-previous");
			label.innerHTML = opts[i].name;                    

			var br = document.createElement("br");
			
			checkboxpart.appendChild(input);
			checkboxpart.appendChild(label);
			checkboxpart.appendChild(br);

			var option = document.createElement("option");
			option.setAttribute("value", opts[i].value);
			option.innerHTML = opts[i].name;
			if (selected1[opts[i].value] === 1) {
				option.setAttribute("selected", "selected");
			}
			selectpart.appendChild(option);
			
			input.on("click", function(e) {
				var src = e.target?e.target:e.srcElement;

				var parent = src.parentElement;
				var inputs = parent.getElementsBySelector("input.checkboxpartitem");
				
				var next = parent.nextElementSibling.children[0];        
				next.innerHTML = "";
				
				for (var i = 0; i!=inputs.length;i++) {
					if (inputs[i].checked === true) {
						var option1 = document.createElement("option");
						option1.setAttribute("value", inputs[i].getAttribute("seq"));
						option1.setAttribute("selected", "selected");
						next.appendChild(option1);
					}
				}                       
			});
		}
	},
	config.onFailure = function(rsp) {
	}

	new Ajax.Request(url, config);
}

Behaviour.specify("Div.multicheckboxdiv", 'multicheckbox', 0, function(e) {
	console.log("Multicheckbox executes behaviour specify.");
    // controls that this SELECT box depends on
	refillOnChange(e,function(params) {
		var value = e.value;
		updateCheckbox(e,e.getAttribute("fillUrl"),{
			parameters: params,
			onSuccess: function() {
				if (value=="") {
				// reflect the initial value. if the control depends on several other SELECT.select,
				// it may take several updates before we get the right items, which is why all these precautions.
					var v = e.getAttribute("value");
					if (v) {
						e.value = v;
						if (e.value==v) e.removeAttribute("value"); 
						// we were able to apply our initial value
					}
				}
					
				fireEvent(e,"filled");
				// let other interested parties know that the items have changed

				// if the update changed the current selection, others listening to this control needs to be notified.
				if (e.value!=value) fireEvent(e,"change");
			}
		});
	});            
});

Behaviour.specify("INPUT.checkboxpartitem", 'checkboxpartitem', 0, function(e) {
	e.on("click", function() {                       
				var src = e;
				var parent = src.parentElement;
				var inputs = parent.getElementsBySelector("input.checkboxpartitem");
				
				var next = parent.nextElementSibling.children[0];        
				next.innerHTML = "";
				
				for (var i = 0; i!=inputs.length;i++) {
					if (inputs[i].checked === true) {
						var option1 = document.createElement("option");
						option1.setAttribute("value", inputs[i].getAttribute("seq"));
						option1.setAttribute("selected", "selected");
						next.appendChild(option1);
					}
				}     
	});
});
// end of multi-checkbox

// dynamic hetero list
function dynamicShowMore(obj) {
	var root = obj.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement;    
	var divs = root.children;
	var count = divs.length;
	if(obj.innerHTML === "Show More") {
		var showMoreCount = 5;
		for (var i = 0; i < count; i++) {
			if (divs[i].className == "repeated-chunk" && divs[i].getAttribute("showmore") === "true") {
				if (showMoreCount > 0 && divs[i].style.display === "none") {
					divs[i].style.display = "block";
					showMoreCount--;
				}
			}
		}
		var collapseId = true;
		for (var i = 0; i < count; i++) {
			if (divs[i].className == "repeated-chunk" && divs[i].getAttribute("showmore") === "true") {
				if (divs[i].style.display === "none") {
					collapseId = false;
				}
			}
		}
		if (collapseId === true) {
			obj.innerHTML = "Collapse";
		}
	}
	// collapse all
	else {
		var showMoreCount = 2;
		for (var i = 0; i < count; i++) {
			if (divs[i].className == "repeated-chunk" && divs[i].getAttribute("showmore") === "true") {
				if (showMoreCount > 0) {
					divs[i].style.display = "block";
					showMoreCount--;
				} else {
					divs[i].style.display = "none";
				}
			}
		}
		obj.innerHTML = "Show More";
	}
	// refresh the list
	dynamicRefreshList(root);
}

// refresh the show more button status
function dynamicRefreshList(listroot) {  
/*         
<!--            Items	            Button Show	Button Hidden
				   0 items		                           Yes
After delete on the screen:
				   1 show, 0 hidden		         Yes
				   2 show, 0 hidden          		Yes
				   0 show, > 1 hidden	Yes	
				   1 show, > 1 hidden	Yes	
				   > 2 items	                  Yes
-->  
*/
	var c = listroot.getElementsBySelector("div.showmore")[0];
	var divs = listroot.getElementsBySelector("div.repeated-chunk[showmore=true]");
				
	if (divs.length > 0 &&c!=null) {
		c.style.display = "block";
		var hiddenCount = 0;
		for (var i = 0;i!=divs.length;i++) {
			if (divs[i].style.display ==="none") {
				hiddenCount++;
			}
		}
		if (hiddenCount > 0) {
			c.style.display = "block";
			return ;
		} else if ( divs.length > 2) {
			c.style.display = "block";
		} else if (divs.length < 3 && hiddenCount === 0) {
			c.style.display = "none";
		}
	} else if(divs.length === 0 &&c!=null) {
		c.style.display = "none";
	}                
}
function dynamicActiveBorder(elem) {
	var legend = elem.parentElement.parentElement.parentElement.parentElement.parentElement;
	console.log(legend);
	var div = legend.nextElementSibling;
	console.log(div);
	console.log(div.style.display);
	if (div.style.display === "block" || div.style.display === "") {
		div.style.display = "none";
		elem.children[0].children[0].className = "icon-plus";
	} else if (div.style.display === "none") {
		div.style.display = "block";
		elem.children[0].children[0].className = "icon-minus";
	}
}
function dynamicActiveDiv(elem) {      
	var legend = elem.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement;            
	var div = legend.nextElementSibling;
	
	var originalState = div.style.display;

	var fieldset = legend.parentElement;

	var collapseAll = fieldset.getAttribute("collapseall");
	var expandUnique = fieldset.getAttribute("expandunique");

	if (expandUnique === "true") {
		var root = fieldset.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement;
		var divblocks = root.getElementsBySelector("fieldset > div");
		var length = divblocks.length;

		for (var i = 0;i<length;i++) {
			console.log(divblocks[i]);
			divblocks[i].style.display = "none";
		}
		var inputs = root.getElementsBySelector("div.dynamic-cc-handle > a");
		length = inputs.length;
		for (var i = 0;i<length;i++) {
			inputs[i].children[0].children[0].className = "icon-plus";
		}
	}
	  
	if (originalState === "block") {
		div.style.display = "none";
		elem.children[0].children[0].className = "icon-plus";
	} else {
		div.style.display = "block";                
		elem.children[0].children[0].className = "icon-minus";
	}
}

// end of dynamic hetero list